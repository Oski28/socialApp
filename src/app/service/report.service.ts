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
}
