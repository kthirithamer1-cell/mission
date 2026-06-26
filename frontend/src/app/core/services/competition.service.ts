import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Competition } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class CompetitionService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/competitions`;

  getAll(filters?: { statut?: string; type?: string; saison?: string }): Observable<Competition[]> {
    let params = new HttpParams();
    if (filters) {
      if (filters.statut) params = params.set('statut', filters.statut);
      if (filters.type) params = params.set('type', filters.type);
      if (filters.saison) params = params.set('saison', filters.saison);
    }
    return this.http.get<Competition[]>(this.baseUrl, { params });
  }

  getEnCours(): Observable<Competition[]> {
    return this.http.get<Competition[]>(`${this.baseUrl}/en-cours`);
  }

  getAVenir(): Observable<Competition[]> {
    return this.http.get<Competition[]>(`${this.baseUrl}/a-venir`);
  }

  getById(id: number): Observable<Competition> {
    return this.http.get<Competition>(`${this.baseUrl}/${id}`);
  }

  create(competition: Competition): Observable<Competition> {
    return this.http.post<Competition>(this.baseUrl, competition);
  }

  update(id: number, competition: Competition): Observable<Competition> {
    return this.http.put<Competition>(`${this.baseUrl}/${id}`, competition);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}
