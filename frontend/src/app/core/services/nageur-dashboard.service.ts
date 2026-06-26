import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NageurDashboardStats } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class NageurDashboardService {
  private readonly http = inject(HttpClient);

  getStats(): Observable<NageurDashboardStats> {
    return this.http.get<NageurDashboardStats>(`${environment.apiUrl}/dashboard/nageur`);
  }
}
