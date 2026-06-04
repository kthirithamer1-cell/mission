import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Piscine } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class PiscineService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Piscine[]> {
    return this.http.get<Piscine[]>(`${environment.apiUrl}/piscines`);
  }

  getById(id: number): Observable<Piscine> {
    return this.http.get<Piscine>(`${environment.apiUrl}/piscines/${id}`);
  }

  create(piscine: Piscine): Observable<Piscine> {
    return this.http.post<Piscine>(`${environment.apiUrl}/piscines`, piscine);
  }

  update(id: number, piscine: Piscine): Observable<Piscine> {
    return this.http.put<Piscine>(`${environment.apiUrl}/piscines/${id}`, piscine);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${environment.apiUrl}/piscines/${id}`, { responseType: 'text' });
  }
}
