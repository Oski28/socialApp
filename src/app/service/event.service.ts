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

  getAllNonParticipate(column: string = 'id', direction: string = 'ASC', filter: string = '', page: string = '0',
                       size: string = '5', activeDate: string = 'true'): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {column, direction, filter, page, size, activeDate}
    };
    return this.http.get(API.concat('user/nonparticipating'), httpOptions);
  }

  join(id: number): Observable<any> {
    return this.http.patch(API.concat(id.toString()).concat('/join'), httpOptionsJson);
  }
}
