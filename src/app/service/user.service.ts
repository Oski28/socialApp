import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DatePipe} from '@angular/common';

const API = 'http://localhost:9090/api/users/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private datePipe: DatePipe) {
  }

  getOne(userId: number): Observable<any> {
    return this.http.get(API.concat(userId.toString()), httpOptionsJson);
  }

  getAll(column: string = 'id', direction: string = 'ASC', filter: string = '', page: string = '0',
         size: string = '5') {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {column, direction, filter, page, size}
    };
    return this.http.get(API, httpOptions);
  }

  getAvatar(userId: number): Observable<any> {
    if (userId === undefined) {
      userId = 0;
    }
    return this.http.get(API.concat(userId.toString()).concat('/avatar'), httpOptionsJson);
  }

  updateAvatar(avatar: string, userId: number): Observable<any> {
    return this.http.patch(API.concat(userId.toString()).concat('/avatar'), {avatar}, httpOptionsJson);
  }

  updateUser(firstname: string, lastname: string, username: string, email: string, date: Date, id: number): Observable<any> {
    const dateOfBirth = this.datePipe.transform(date, 'yyyy-MM-dd');

    return this.http.put(API.concat(id.toString()), {
      firstname,
      lastname,
      username,
      email,
      dateOfBirth
    }, httpOptionsJson);
  }

  updatePassword(oldPassword: string, newPassword: string, id: number): Observable<any> {
    return this.http.patch(API.concat(id.toString()).concat('/password'),
      {oldPassword, newPassword}, httpOptionsJson);
  }

  removeUser(userId: number): Observable<any> {
    return this.http.delete(API.concat(userId.toString()), httpOptionsJson);
  }

  ban(date: Date, id: number): Observable<any> {
    const banExpirationDate = this.datePipe.transform(date, 'yyyy-MM-dd');
    return this.http.patch(API.concat(id.toString()).concat('/ban'), {banExpirationDate}, httpOptionsJson);
  }

  unban(id: number): Observable<any> {
    return this.http.patch(API.concat(id.toString()).concat('/unban'), {id}, httpOptionsJson);
  }

  updateRole(role: string, id: number): Observable<any> {
    const roles = [];
    switch (role) {
      default:
      case 'USER':
        roles.push(role);
        break;
      case 'MODERATOR':
        roles.push(role);
        roles.push('USER');
        break;
      case 'ADMIN':
        roles.push(role);
        roles.push('USER');
        roles.push('MODERATOR');
        break;
    }
    return this.http.patch(API.concat(id.toString()).concat('/role'), {roles}, httpOptionsJson);
  }

  create(avatar: string, firstname: string, lastname: string, username: string, email: string, password: string,
         date: Date, role: string): Observable<any> {
    const dateOfBirth = this.datePipe.transform(date, 'yyyy-MM-dd');
    const roles = [];
    switch (role) {
      default:
      case 'USER':
        roles.push(role);
        break;
      case 'MODERATOR':
        roles.push(role);
        roles.push('USER');
        break;
      case 'ADMIN':
        roles.push(role);
        roles.push('USER');
        roles.push('MODERATOR');
        break;
    }
    return this.http.post(API, {
      firstname,
      lastname,
      username,
      email,
      password,
      dateOfBirth,
      avatar,
      roles
    }, httpOptionsJson);
  }
}
