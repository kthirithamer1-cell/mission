import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PresenceDTO {
  id?: number;
  seanceId: number;
  nageurId: number;
  nageurNom: string;
  nageurPrenom: string;
  nageurCategorie?: string;
  statut: 'PRESENT' | 'ABSENT' | 'JUSTIFIE';
}

@Injectable({ providedIn: 'root' })
export class PresenceService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/presences`;

  getBySeance(seanceId: number): Observable<PresenceDTO[]> {
    return this.http.get<PresenceDTO[]>(`${this.base}/seance/${seanceId}`);
  }

  mark(seanceId: number, nageurId: number, statut: string): Observable<PresenceDTO> {
    return this.http.put<PresenceDTO>(
      `${this.base}/seance/${seanceId}/nageur/${nageurId}?statut=${statut}`, {}
    );
  }

  saveAll(seanceId: number, dtos: PresenceDTO[]): Observable<PresenceDTO[]> {
    return this.http.post<PresenceDTO[]>(`${this.base}/seance/${seanceId}/bulk`, dtos);
  }
}
