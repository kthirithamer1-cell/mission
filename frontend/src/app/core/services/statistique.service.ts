import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ClubStats, SwimmerStats } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class StatistiqueService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/resultats`;

  getClubStats(): Observable<ClubStats> {
    return this.http.get<ClubStats>(`${this.baseUrl}/statistiques`);
  }

  getSwimmerStats(nageurId: number): Observable<SwimmerStats> {
    return this.http.get<SwimmerStats>(`${this.baseUrl}/nageur/${nageurId}/statistiques`);
  }
}
