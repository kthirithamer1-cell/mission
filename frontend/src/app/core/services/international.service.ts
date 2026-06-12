import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RecordMondial, NageurInternational, CompetitionInternationale } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class InternationalService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/international`;

  getRecords(bassin?: string, sexe?: string): Observable<RecordMondial[]> {
    let params = new HttpParams();
    if (bassin) params = params.set('bassin', bassin);
    if (sexe) params = params.set('sexe', sexe);
    return this.http.get<RecordMondial[]>(`${this.baseUrl}/records`, { params });
  }

  getSwimmers(): Observable<NageurInternational[]> {
    return this.http.get<NageurInternational[]>(`${this.baseUrl}/nageurs`);
  }

  getCompetitions(): Observable<CompetitionInternationale[]> {
    return this.http.get<CompetitionInternationale[]>(`${this.baseUrl}/competitions`);
  }

  triggerScrape(): Observable<string> {
    return this.http.post(`${this.baseUrl}/scrape`, {}, { responseType: 'text' });
  }
}
