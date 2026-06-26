import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CoachDashboardStats } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class CoachDashboardService {
  private readonly http = inject(HttpClient);

  getStats(): Observable<CoachDashboardStats> {
    return this.http.get<CoachDashboardStats>(`${environment.apiUrl}/dashboard/coach`);
  }
}
