import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API = 'http://localhost:9090/api/chats/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor(private http: HttpClient) {
  }

  getOne(id: number): Observable<any> {
    return this.http.get(API.concat(id.toString()), httpOptionsJson);
  }

  editName(name: string, id: number): Observable<any> {
    return this.http.put(API.concat(id.toString()), {name}, httpOptionsJson);
  }

  getAll(column: string = 'id', direction: string = 'ASC', filter: string = '', page: string = '0',
         size: string = '10') {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {column, direction, filter, page, size}
    };
    return this.http.get(API, httpOptions);
  }
}
