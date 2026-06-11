import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Seance } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class SeanceService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Seance[]> {
    return this.http.get<Seance[]>(`${environment.apiUrl}/seances`);
  }

  getMesSeances(): Observable<Seance[]> {
    return this.http.get<Seance[]>(`${environment.apiUrl}/seances/mes-seances`);
  }

  getById(id: number): Observable<Seance> {
    return this.http.get<Seance>(`${environment.apiUrl}/seances/${id}`);
  }

  create(seance: Seance): Observable<Seance> {
    return this.http.post<Seance>(`${environment.apiUrl}/seances`, seance);
  }

  update(id: number, seance: Seance): Observable<Seance> {
    return this.http.put<Seance>(`${environment.apiUrl}/seances/${id}`, seance);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/seances/${id}`, { responseType: 'text' });
  }
}
