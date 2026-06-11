import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SeanceService } from '../../../core/services/seance.service';
import { Seance } from '../../../core/models/app.models';

type ViewMode = 'week' | 'list';

@Component({
  selector: 'app-coach-planning',
  imports: [CommonModule],
  templateUrl: './coach-planning.component.html',
})
export class CoachPlanningComponent implements OnInit {
  private readonly seanceService = inject(SeanceService);

  seances = signal<Seance[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  viewMode = signal<ViewMode>('week');
  selectedWeekOffset = signal(0);

  readonly DAYS = ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'];
  readonly HOURS = Array.from({ length: 14 }, (_, i) => i + 7); // 07h – 20h

  ngOnInit(): void {
    this.seanceService.getMesSeances().subscribe({
      next: (data) => { this.seances.set(data); this.loading.set(false); },
      error: () => { this.error.set('Impossible de charger les séances.'); this.loading.set(false); },
    });
  }

  setView(m: ViewMode): void { this.viewMode.set(m); }
  prevWeek(): void { this.selectedWeekOffset.update(o => o - 1); }
  nextWeek(): void { this.selectedWeekOffset.update(o => o + 1); }
  resetWeek(): void { this.selectedWeekOffset.set(0); }

  weekStart = computed(() => {
    const today = new Date();
    const day = today.getDay() === 0 ? 6 : today.getDay() - 1;
    const mon = new Date(today);
    mon.setDate(today.getDate() - day + this.selectedWeekOffset() * 7);
    mon.setHours(0, 0, 0, 0);
    return mon;
  });

  weekDates = computed(() =>
    Array.from({ length: 7 }, (_, i) => {
      const d = new Date(this.weekStart());
      d.setDate(d.getDate() + i);
      return d;
    })
  );

  weekLabel = computed(() => {
    const dates = this.weekDates();
    const fmt = (d: Date) => d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' });
    return `${fmt(dates[0])} – ${fmt(dates[6])} ${dates[0].getFullYear()}`;
  });

  seancesForDay = computed(() => {
    const byDay: Map<string, Seance[]> = new Map();
    for (const s of this.seances()) {
      if (!s.date) continue;
      byDay.set(s.date, [...(byDay.get(s.date) ?? []), s]);
    }
    return byDay;
  });

  getSeancesForDate(date: Date): Seance[] {
    const key = this.toISODate(date);
    return this.seancesForDay().get(key) ?? [];
  }

  isToday(date: Date): boolean {
    const t = new Date();
    return date.toDateString() === t.toDateString();
  }

  seanceTop(s: Seance): number {
    const [h, m] = (s.heureDebut ?? '07:00').split(':').map(Number);
    return ((h - 7) * 60 + m) / (13 * 60) * 100;
  }

  seanceHeight(s: Seance): number {
    const [h1, m1] = (s.heureDebut ?? '07:00').split(':').map(Number);
    const [h2, m2] = (s.heureFin ?? '08:00').split(':').map(Number);
    const duration = (h2 * 60 + m2) - (h1 * 60 + m1);
    return Math.max(duration / (13 * 60) * 100, 4);
  }

  formatTime(t: string): string {
    return t?.slice(0, 5) ?? '';
  }

  formatDateFull(d: Date): string {
    return d.toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' });
  }

  sortedByDate = computed(() =>
    [...this.seances()].sort((a, b) => (a.date ?? '') < (b.date ?? '') ? -1 : 1)
  );

  upcomingOnly = computed(() => {
    const today = this.toISODate(new Date());
    return this.sortedByDate().filter(s => s.date >= today);
  });

  pastOnly = computed(() => {
    const today = this.toISODate(new Date());
    return this.sortedByDate().filter(s => s.date < today).reverse();
  });

  private toISODate(d: Date): string {
    return d.toISOString().slice(0, 10);
  }

  totalHours = computed(() => {
    let mins = 0;
    for (const s of this.seances()) {
      if (!s.heureDebut || !s.heureFin) continue;
      const [h1, m1] = s.heureDebut.split(':').map(Number);
      const [h2, m2] = s.heureFin.split(':').map(Number);
      mins += (h2 * 60 + m2) - (h1 * 60 + m1);
    }
    return Math.round(mins / 60);
  });

  upcomingCount = computed(() => this.upcomingOnly().length);
}
