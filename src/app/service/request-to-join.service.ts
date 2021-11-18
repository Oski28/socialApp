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
}
