import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SocketClientService} from './socket-client.service';
import {first} from 'rxjs/operators';

const API = 'http://localhost:9090/api/messages/';
const httpOptionsJson = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient, private socketClient: SocketClientService) {
  }

  findAll(page: string = '0', id: number): Observable<any> {
    return this.socketClient
      .onMessage(`/topic/chat/${id}/get`)
      .pipe(first());
  }

  save(content: string, file: string, id: number, userId: number) {
    return this.socketClient.send(`/topic/chat/${id}`, {content, file, userId});
  }

  onPost(id: number): Observable<any> {
    return this.socketClient.onMessage(`/topic/chat/${id}/sent`).pipe();
  }

  getAll(page: string = '0', chatId: number): Observable<any> {
    const httpOptions = {
      headers: {'Content-Type': 'application/json'},
      params: {page}
    };
    return this.http.get(API.concat(chatId.toString()), httpOptions);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(API.concat(id.toString()), httpOptionsJson);
  }
}
