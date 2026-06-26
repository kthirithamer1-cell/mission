import { Component } from '@angular/core';
import { SeanceWeekCalendarComponent } from '../../../shared/seance-week-calendar/seance-week-calendar.component';

@Component({
  selector: 'app-coach-planning',
  imports: [SeanceWeekCalendarComponent],
  template: `
    <div class="cp-plan-page">
      <div class="cp-plan-head">
        <div>
          <h1 class="cp-plan-title">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" width="28" height="28" aria-hidden="true">
              <rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
            Mon Planning
          </h1>
          <p class="cp-plan-sub">Vos séances d'entraînement, nageurs et présences</p>
        </div>
      </div>

      <app-seance-week-calendar
        role="coach"
        [compact]="false"
        [showListToggle]="true"
        [showStats]="true"
        [showFullPageLink]="false" />
    </div>
  `,
})
export class CoachPlanningComponent {}
