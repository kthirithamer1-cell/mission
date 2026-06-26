import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Club, Entraineur } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class ClubService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Club[]> {
    return this.http.get<Club[]>(`${environment.apiUrl}/clubs`);
  }

  getById(id: number): Observable<Club> {
    return this.http.get<Club>(`${environment.apiUrl}/clubs/${id}`);
  }

  getCoaches(id: number): Observable<Entraineur[]> {
    return this.http.get<Entraineur[]>(`${environment.apiUrl}/clubs/${id}/entraineurs`);
  }

  create(club: Club): Observable<Club> {
    return this.http.post<Club>(`${environment.apiUrl}/clubs`, club);
  }

  update(id: number, club: Club): Observable<Club> {
    return this.http.put<Club>(`${environment.apiUrl}/clubs/${id}`, club);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/clubs/${id}`, { responseType: 'text' });
  }
}
