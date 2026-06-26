import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Entraineur } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class EntraineurService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Entraineur[]> {
    return this.http.get<Entraineur[]>(`${environment.apiUrl}/entraineurs`);
  }

  getById(id: number): Observable<Entraineur> {
    return this.http.get<Entraineur>(`${environment.apiUrl}/entraineurs/${id}`);
  }

  create(entraineur: Entraineur): Observable<Entraineur> {
    return this.http.post<Entraineur>(`${environment.apiUrl}/entraineurs`, entraineur);
  }

  update(id: number, entraineur: Entraineur): Observable<Entraineur> {
    return this.http.put<Entraineur>(`${environment.apiUrl}/entraineurs/${id}`, entraineur);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/entraineurs/${id}`, { responseType: 'text' });
  }

  getMe(): Observable<Entraineur> {
    return this.http.get<Entraineur>(`${environment.apiUrl}/entraineurs/me`);
  }

  updateMe(entraineur: Partial<Entraineur>): Observable<Entraineur> {
    return this.http.put<Entraineur>(`${environment.apiUrl}/entraineurs/me`, entraineur);
  }

  uploadPhoto(file: File): Observable<Entraineur> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Entraineur>(`${environment.apiUrl}/entraineurs/me/photo`, formData);
  }
}
