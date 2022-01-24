import {Injectable} from '@angular/core';
import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {TokenStorageService} from './token-storage.service';
import {AuthService} from './auth.service';
import {catchError, filter, switchMap, take} from 'rxjs/operators';
import {Router} from '@angular/router';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private tokenStorageService: TokenStorageService, public authService: AuthService, private router: Router) {
  }

  private static addJwtToken(request: HttpRequest<any>, jwt: string | null) {
    return request.clone({headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + jwt)});
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const jwt = this.tokenStorageService.getJwtToken();
    if (jwt != null) {
      authReq = AuthInterceptorService.addJwtToken(req, jwt);
    }
    return next.handle(authReq).pipe(catchError(error => {
      if (error instanceof HttpErrorResponse && !authReq.url.includes('auth/signin') && error.status === 401) {
        return this.handle401Error(authReq, next);
      }
      if (error instanceof HttpErrorResponse && error.status === 403){
        this.router.navigate(['forbidden']);
      }
      if (error instanceof HttpErrorResponse && error.status === 404){
        this.router.navigate(['notfound']);
      }
      return throwError(error);
    }));
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const token = this.tokenStorageService.getRefreshToken();

      if (token)
        return this.authService.refreshToken(token).pipe(
          // tslint:disable-next-line:no-shadowed-variable
          switchMap((token: any) => {
            this.isRefreshing = false;
            this.tokenStorageService.saveJwtToken(token.accessToken);
            this.refreshTokenSubject.next(token.accessToken);

            return next.handle(AuthInterceptorService.addJwtToken(request, token.accessToken));
          }),
          catchError((err) => {
            this.isRefreshing = false;
            this.tokenStorageService.signOut();
            window.location.reload();
            return throwError(err);
          })
        );
    }

    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap((token) => next.handle(AuthInterceptorService.addJwtToken(request, token)))
    );
  }
}

export const authInterceptorProviders = [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}];
