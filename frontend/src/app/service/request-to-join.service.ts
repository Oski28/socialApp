import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API = 'http://localhost:9090/api/requestToJoin/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class RequestToJoinService {

  constructor(private http: HttpClient) {
  }

  request(eventId: number): Observable<any> {
    return this.http.post(API, {eventId}, httpOptionsJson);
  }


  getAllForEvent(page: string = '0', size: string = '5', eventId: number): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {page, size}
    };
    return this.http.get(API.concat(eventId.toString()), httpOptions);
  }

  edit(id: number, status: string): Observable<any> {
    return this.http.patch(API.concat(id.toString()), {status}, httpOptionsJson);
  }

  getAllForUser(column: string = 'id', direction: string = 'ASC', page: string = '0',
                size: string = '5'): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {column, direction, page, size}
    };
    return this.http.get(API.concat('user'), httpOptions);
  }

  resend(id: number) {
    return this.http.patch(API.concat(id.toString().concat('/resend')), httpOptionsJson);
  }
}
