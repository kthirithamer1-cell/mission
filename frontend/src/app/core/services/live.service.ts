import { Injectable, NgZone, inject } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LiveResultEvent } from '../models/app.models';
import { Client, Message } from '@stomp/stompjs';

@Injectable({ providedIn: 'root' })
export class LiveService {
  private readonly zone = inject(NgZone);
  private client: Client | null = null;
  private readonly events$ = new Subject<LiveResultEvent>();

  connect(competitionId?: number): Observable<LiveResultEvent> {
    const wsUrl = environment.apiUrl
      .replace('http://', 'ws://')
      .replace('https://', 'wss://')
      .replace('/api', '/ws/websocket');

    this.client = new Client({
      brokerURL: wsUrl,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('Connected to WebSocket');
        const topic = competitionId ? `/topic/competition/${competitionId}` : '/topic/live';
        this.client?.subscribe(topic, (message: Message) => {
          this.zone.run(() => {
            const event: LiveResultEvent = JSON.parse(message.body);
            this.events$.next(event);
          });
        });
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      },
      onStompError: (frame) => {
        console.error('STOMP error', frame);
      }
    });

    this.client.activate();
    return this.events$.asObservable();
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
      this.client = null;
    }
  }
}
