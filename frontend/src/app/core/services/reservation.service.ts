import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Reservation } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${environment.apiUrl}/reservations`);
  }

  getById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${environment.apiUrl}/reservations/${id}`);
  }

  create(reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(`${environment.apiUrl}/reservations`, reservation);
  }

  update(id: number, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${environment.apiUrl}/reservations/${id}`, reservation);
  }

  updateStatus(id: number, statut: string): Observable<Reservation> {
    return this.http.patch<Reservation>(`${environment.apiUrl}/reservations/${id}/statut`, { statut });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/reservations/${id}`, { responseType: 'text' });
  }
}
