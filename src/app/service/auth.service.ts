import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DatePipe} from '@angular/common';

const AUTH_API = 'http://localhost:9090/api/auth/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private datePipe: DatePipe) {
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {username, password}, httpOptionsJson);
  }

  register(avatar: string, firstname: string, lastname: string, username: string, email: string, password: string,
           date: Date): Observable<any> {

    const dateOfBirth = this.datePipe.transform(date, 'yyyy-MM-dd');

    return this.http.post(AUTH_API + 'signup', {
      firstname,
      lastname,
      username,
      email,
      password,
      dateOfBirth,
      avatar
    }, httpOptionsJson);
  }

  logout(userId: number, token: string | null) {
    return this.http.post(AUTH_API + 'logout', {userId, token}, httpOptionsJson);
  }

  refreshToken(refreshToken: string) {
    return this.http.post(AUTH_API + 'refreshToken', {refreshToken}, httpOptionsJson);
  }

  confirmAccount(token: string | null): Observable<any> {
    return this.http.get(AUTH_API + 'confirm-account?token=' + token, httpOptionsJson);
  }
}
