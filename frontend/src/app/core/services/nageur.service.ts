import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Nageur } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class NageurService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Nageur[]> {
    return this.http.get<Nageur[]>(`${environment.apiUrl}/nageurs`);
  }

  getById(id: number): Observable<Nageur> {
    return this.http.get<Nageur>(`${environment.apiUrl}/nageurs/${id}`);
  }

  create(nageur: Nageur): Observable<Nageur> {
    return this.http.post<Nageur>(`${environment.apiUrl}/nageurs`, nageur);
  }

  update(id: number, nageur: Nageur): Observable<Nageur> {
    return this.http.put<Nageur>(`${environment.apiUrl}/nageurs/${id}`, nageur);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/nageurs/${id}`, { responseType: 'text' });
  }

  getMe(): Observable<Nageur> {
    return this.http.get<Nageur>(`${environment.apiUrl}/nageurs/me`);
  }

  updateMe(nageur: Partial<Nageur>): Observable<Nageur> {
    return this.http.put<Nageur>(`${environment.apiUrl}/nageurs/me`, nageur);
  }
}
