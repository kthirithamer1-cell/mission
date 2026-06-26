import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CalendarResponse } from '../models/app.models';

@Injectable({ providedIn: 'root' })
export class CalendarService {
  private readonly http = inject(HttpClient);

  getCoachCalendar(from?: string, to?: string): Observable<CalendarResponse> {
    return this.http.get<CalendarResponse>(`${environment.apiUrl}/dashboard/coach/calendar`, {
      params: this.buildParams(from, to),
    });
  }

  getNageurCalendar(from?: string, to?: string): Observable<CalendarResponse> {
    return this.http.get<CalendarResponse>(`${environment.apiUrl}/dashboard/nageur/calendar`, {
      params: this.buildParams(from, to),
    });
  }

  private buildParams(from?: string, to?: string): HttpParams {
    let params = new HttpParams();
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return params;
  }
}
