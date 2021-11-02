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

  getAvatar(userId: number): Observable<any> {
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
}
