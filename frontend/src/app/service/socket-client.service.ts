import {Injectable, OnDestroy} from '@angular/core';
import {Client, Message, over, StompSubscription} from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {BehaviorSubject, Observable} from 'rxjs';
import {SocketClientState} from '../model/SocketClientState';
import {environment} from '../../environments/environment';
import {filter, first, switchMap} from 'rxjs/operators';
import {TokenStorageService} from './token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class SocketClientService implements OnDestroy {
  private readonly client: Client;
  private state: BehaviorSubject<SocketClientState>;

  constructor(private tokenService: TokenStorageService) {
    this.client = over(new SockJS(environment.api));
    this.state = new BehaviorSubject<SocketClientState>(SocketClientState.ATTEMPTING);
    this.client.connect({Authorization: this.tokenService.getJwtToken()}, () => {
      this.state.next(SocketClientState.CONNECTED);
    });
  }

  static jsonHandler(message: Message): any {
    return JSON.parse(message.body);
  }

  static textHandler(message: Message): string {
    return message.body;
  }

  connect(): Observable<Client> {
    return new Observable<Client>(observer => {
      this.state.pipe(filter(state => state === SocketClientState.CONNECTED))
        .subscribe(() => {
          observer.next(this.client);
        });
    });
  }

  ngOnDestroy(): void {
    this.connect().pipe(first()).subscribe(inst => inst.disconnect(null));
  }

  onMessage(topic: string, handler = SocketClientService.jsonHandler): Observable<any> {
    return this.connect().pipe(first(), switchMap(inst => {
      return new Observable<any>(observer => {
        const subscription: StompSubscription = inst.subscribe(topic, message => {
          observer.next(handler(message));
        }, {Authorization: this.tokenService.getJwtToken()});
        return () => inst.unsubscribe(subscription.id);
      });
    }));
  }

  onPlainMessage(topic: string): Observable<string> {
    return this.onMessage(topic, SocketClientService.textHandler);
  }

  send(topic: string, payload: any): void {
    this.connect()
      .pipe(first())
      .subscribe(inst => {
        inst.send(topic, {Authorization: this.tokenService.getJwtToken()}, JSON.stringify(payload))
      });
  }
}
