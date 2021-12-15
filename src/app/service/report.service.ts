import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API = 'http://localhost:9090/api/reports/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) {
  }

  create(reason: string, userId: number, eventId: number): Observable<any> {
    return this.http.post(API, {userId, eventId, reason}, httpOptionsJson);
  }

  createForUser(reason: string, userId: number): Observable<any> {
    return this.http.post(API, {userId, reason}, httpOptionsJson);
  }

  getAllNotReceived(column: string = 'id', direction: string = 'ASC', page: string = '0',
                    size: string = '5'): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {column, direction, page, size}
    };
    return this.http.get(API.concat('notReceived'), httpOptions);
  }

  getOne(id: number): Observable<any> {
    return this.http.get(API.concat(id.toString()), httpOptionsJson);
  }

  getAll(page: string = '0',
         size: string = '5') {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {page, size}
    };
    return this.http.get(API, httpOptions);
  }

  remove(id: number): Observable<any> {
    return this.http.delete(API.concat(id.toString()), httpOptionsJson);
  }

  received(id: number): Observable<any> {
    return this.getOne(id);
  }
}
