import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const API = 'http://localhost:9090/api/categories/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) {
  }

  getAll(filter: string): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {filter}
    };
    return this.http.get(API, httpOptions);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(API.concat(id.toString()), httpOptionsJson);
  }

  update(name: string, id: number): Observable<any> {
    return this.http.put(API.concat(id.toString()), {name, id}, httpOptionsJson);
  }

  create(name: string): Observable<any> {
    return this.http.post(API, {name}, httpOptionsJson);
  }
}
