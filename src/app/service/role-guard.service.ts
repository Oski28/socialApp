import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {TokenStorageService} from './token-storage.service';
import {Observable} from 'rxjs';
import decode from 'jwt-decode'

@Injectable({
  providedIn: 'root'
})
export class RoleGuardService implements CanActivate {

  guard = '';

  constructor(private tokenStorage: TokenStorageService, private router: Router) {
  }

  // tslint:disable-next-line:max-line-length
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const expectedRoleMod = route.data.expectedRoleMod;
    const expectedRoleAdmin = route.data.expectedRoleAdmin;
    const token = this.tokenStorage.getJwtToken();
    const tokenPayload = decode(token);
    const isAuth = this.tokenStorage.isLoggedIn();
    if (!isAuth) {
      this.guard = 'Brak dostępu do strony. Wymagane logowanie'
      this.router.navigate(['signin', this.guard]);
      return false;
    }
    // @ts-ignore
    if (tokenPayload.sub !== expectedRoleMod && tokenPayload.sub !== expectedRoleAdmin) {
      this.guard = 'Brak dostępu do strony. Brak uprawnień'
      this.router.navigate(['dashboard', this.guard]);
      return false;
    }
    return true;
  }
}
