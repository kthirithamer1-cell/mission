import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Resultat } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class ResultatService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/resultats`;

  getAll(): Observable<Resultat[]> {
    return this.http.get<Resultat[]>(this.baseUrl);
  }

  getById(id: number): Observable<Resultat> {
    return this.http.get<Resultat>(`${this.baseUrl}/${id}`);
  }

  getByNageur(nageurId: number): Observable<Resultat[]> {
    return this.http.get<Resultat[]>(`${this.baseUrl}/nageur/${nageurId}`);
  }

  getByCompetition(competitionId: number): Observable<Resultat[]> {
    return this.http.get<Resultat[]>(`${this.baseUrl}/competition/${competitionId}`);
  }

  create(resultat: Resultat): Observable<Resultat> {
    return this.http.post<Resultat>(this.baseUrl, resultat);
  }

  update(id: number, resultat: Resultat): Observable<Resultat> {
    return this.http.put<Resultat>(`${this.baseUrl}/${id}`, resultat);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  submitLive(resultat: Resultat): Observable<Resultat> {
    return this.http.post<Resultat>(`${this.baseUrl}/live`, resultat);
  }
}
