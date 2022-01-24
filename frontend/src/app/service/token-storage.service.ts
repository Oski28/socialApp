import {Injectable} from '@angular/core';

const JWT_TOKEN = 'JWT_TOKEN';
const REFRESH_TOKEN = 'REFRESH_TOKEN';
const USER_KEY = 'AUTH_USER';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  signOut(): void {
    this.removeTokens();
    this.removeUser();
  }

  private removeTokens() {
    window.sessionStorage.removeItem(JWT_TOKEN);
    window.sessionStorage.removeItem(REFRESH_TOKEN);
  }

  private removeUser() {
    window.sessionStorage.removeItem(USER_KEY);
  }

  public saveJwtToken(jwt: string): void {
    window.sessionStorage.removeItem(JWT_TOKEN);
    window.sessionStorage.setItem(JWT_TOKEN, jwt);
  }


  isLoggedIn() {
    return !!this.getJwtToken();
  }

  isMod(){
    return this.getUser().roles.includes('ROLE_MODERATOR');
  }

  isAdmin(){
    return this.getUser().roles.includes('ROLE_ADMIN');
  }

  getJwtToken(): string | null {
    return window.sessionStorage.getItem(JWT_TOKEN);
  }

  public saveRefreshToken(refreshToken: string): void {
    window.sessionStorage.removeItem(REFRESH_TOKEN);
    window.sessionStorage.setItem(REFRESH_TOKEN, refreshToken);
  }

  getRefreshToken(): string | null {
    return window.sessionStorage.getItem(REFRESH_TOKEN);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }
}
