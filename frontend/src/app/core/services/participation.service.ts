import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ParticipationDTO } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class ParticipationService {
  private readonly http = inject(HttpClient);

  getByCompetition(competitionId: number): Observable<ParticipationDTO[]> {
    return this.http.get<ParticipationDTO[]>(`${environment.apiUrl}/participations/competition/${competitionId}`);
  }

  getByNageur(nageurId: number): Observable<ParticipationDTO[]> {
    return this.http.get<ParticipationDTO[]>(`${environment.apiUrl}/participations/nageur/${nageurId}`);
  }

  getByClub(clubId: number): Observable<ParticipationDTO[]> {
    return this.http.get<ParticipationDTO[]>(`${environment.apiUrl}/participations/club/${clubId}`);
  }

  countByCompetition(competitionId: number): Observable<number> {
    return this.http.get<number>(`${environment.apiUrl}/participations/competition/${competitionId}/count`);
  }

  isRegistered(competitionId: number, nageurId: number): Observable<boolean> {
    return this.http.get<boolean>(`${environment.apiUrl}/participations/check?competitionId=${competitionId}&nageurId=${nageurId}`);
  }

  register(competitionId: number): Observable<ParticipationDTO> {
    return this.http.post<ParticipationDTO>(`${environment.apiUrl}/participations/register/${competitionId}`, {});
  }

  cancel(competitionId: number): Observable<ParticipationDTO> {
    return this.http.delete<ParticipationDTO>(`${environment.apiUrl}/participations/cancel/${competitionId}`);
  }

  updateStatus(participationId: number, statut: string): Observable<ParticipationDTO> {
    return this.http.patch<ParticipationDTO>(`${environment.apiUrl}/participations/${participationId}/statut`, { statut });
  }

  remove(participationId: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/participations/${participationId}`, { responseType: 'text' });
  }
}
