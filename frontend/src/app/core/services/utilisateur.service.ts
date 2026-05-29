import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Utilisateur } from '../models/auth.models';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private readonly http = inject(HttpClient);

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${environment.apiUrl}/utilisateurs`);
  }

  getById(id: number): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(
      `${environment.apiUrl}/utilisateurs/${id}`
    );
  }
}
