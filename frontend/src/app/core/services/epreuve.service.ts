import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Epreuve } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class EpreuveService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/epreuves`;

  getAll(): Observable<Epreuve[]> {
    return this.http.get<Epreuve[]>(this.baseUrl);
  }

  getById(id: number): Observable<Epreuve> {
    return this.http.get<Epreuve>(`${this.baseUrl}/${id}`);
  }

  create(epreuve: Epreuve): Observable<Epreuve> {
    return this.http.post<Epreuve>(this.baseUrl, epreuve);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}
