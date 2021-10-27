import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {TokenStorageService} from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  guard = '';

  constructor(private tokenStorage: TokenStorageService, private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const isAuth = this.tokenStorage.isLoggedIn();
    if (!isAuth) {
      this.guard = 'Brak dostępu do strony. Wymagane logowanie'
      this.router.navigate(['signin', this.guard]);
    }
    return isAuth;
  }
}
