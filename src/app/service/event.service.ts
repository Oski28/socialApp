import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import DateTimeFormat = Intl.DateTimeFormat;

const API = 'http://localhost:9090/api/events/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) {
  }

  create(name: string, description: string, ageLimit: number, maxNumberOfParticipant: number, location: string, date: DateTimeFormat,
         freeJoin: string, categories: any, chatName: string): Observable<any> {
    return this.http.post(API, {
      name, description, ageLimit, maxNumberOfParticipant, location, date,
      freeJoin, categories, chatName
    }, httpOptionsJson);
  }
}
