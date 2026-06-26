import { Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CalendarService } from '../../core/services/calendar.service';
import { CalendarEvent, CalendarResponse } from '../../core/models/app.models';

type ViewMode = 'week' | 'list';
type CalendarRole = 'coach' | 'nageur';

@Component({
  selector: 'app-seance-week-calendar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './seance-week-calendar.component.html',
})
export class SeanceWeekCalendarComponent {
  private readonly calendarService = inject(CalendarService);

  readonly role = input.required<CalendarRole>();
  readonly compact = input(false);
  readonly showListToggle = input(true);
  readonly showStats = input(true);
  readonly showFullPageLink = input(false);

  readonly weekChanged = output<{ from: string; to: string }>();

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly calendar = signal<CalendarResponse | null>(null);
  readonly viewMode = signal<ViewMode>('week');
  readonly selectedWeekOffset = signal(0);

  readonly DAYS = ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'];
  readonly HOURS = Array.from({ length: 14 }, (_, i) => i + 7);

  readonly weekStart = computed(() => {
    const today = new Date();
    const day = today.getDay() === 0 ? 6 : today.getDay() - 1;
    const mon = new Date(today);
    mon.setDate(today.getDate() - day + this.selectedWeekOffset() * 7);
    mon.setHours(0, 0, 0, 0);
    return mon;
  });

  readonly weekDates = computed(() =>
    Array.from({ length: 7 }, (_, i) => {
      const d = new Date(this.weekStart());
      d.setDate(d.getDate() + i);
      return d;
    })
  );

  readonly weekLabel = computed(() => {
    const dates = this.weekDates();
    const fmt = (d: Date) => d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' });
    return `${fmt(dates[0])} – ${fmt(dates[6])} ${dates[0].getFullYear()}`;
  });

  readonly weekRange = computed(() => ({
    from: this.toISODate(this.weekDates()[0]),
    to: this.toISODate(this.weekDates()[6]),
  }));

  readonly events = computed(() => this.calendar()?.events ?? []);

  readonly seancesForDay = computed(() => {
    const byDay = new Map<string, CalendarEvent[]>();
    for (const e of this.events()) {
      if (!e.date) continue;
      byDay.set(e.date, [...(byDay.get(e.date) ?? []), e]);
    }
    return byDay;
  });

  readonly sortedByDate = computed(() =>
    [...this.events()].sort((a, b) => (a.date ?? '') < (b.date ?? '') ? -1 : 1)
  );

  readonly upcomingOnly = computed(() => {
    const today = this.toISODate(new Date());
    return this.sortedByDate().filter((s) => s.date >= today);
  });

  readonly pastOnly = computed(() => {
    const today = this.toISODate(new Date());
    return this.sortedByDate().filter((s) => s.date < today).reverse();
  });

  constructor() {
    effect(() => {
      const range = this.weekRange();
      this.role();
      this.loadCalendar(range.from, range.to);
    });
  }

  setView(mode: ViewMode): void {
    this.viewMode.set(mode);
  }

  prevWeek(): void {
    this.selectedWeekOffset.update((o) => o - 1);
  }

  nextWeek(): void {
    this.selectedWeekOffset.update((o) => o + 1);
  }

  resetWeek(): void {
    this.selectedWeekOffset.set(0);
  }

  reload(): void {
    const range = this.weekRange();
    this.loadCalendar(range.from, range.to);
  }

  getSeancesForDate(date: Date): CalendarEvent[] {
    return this.seancesForDay().get(this.toISODate(date)) ?? [];
  }

  isToday(date: Date): boolean {
    return date.toDateString() === new Date().toDateString();
  }

  seanceTop(s: CalendarEvent): number {
    const [h, m] = (s.heureDebut ?? '07:00').split(':').map(Number);
    return ((h - 7) * 60 + m) / (13 * 60) * 100;
  }

  seanceHeight(s: CalendarEvent): number {
    const [h1, m1] = (s.heureDebut ?? '07:00').split(':').map(Number);
    const [h2, m2] = (s.heureFin ?? '08:00').split(':').map(Number);
    const duration = h2 * 60 + m2 - (h1 * 60 + m1);
    return Math.max(duration / (13 * 60) * 100, 4);
  }

  formatTime(t: string): string {
    return t?.slice(0, 5) ?? '';
  }

  presenceLabel(status?: string): string {
    switch (status?.toUpperCase()) {
      case 'PRESENT': return 'Présent';
      case 'ABSENT': return 'Absent';
      case 'JUSTIFIE': return 'Justifié';
      default: return 'Non marqué';
    }
  }

  presenceClass(status?: string): string {
    switch (status?.toUpperCase()) {
      case 'PRESENT': return 'is-present';
      case 'ABSENT': return 'is-absent';
      case 'JUSTIFIE': return 'is-justifie';
      default: return 'is-unknown';
    }
  }

  themeClass(): string {
    return this.role() === 'nageur' ? 'cal-theme-nageur' : 'cal-theme-coach';
  }

  fullPageLink(): string {
    return this.role() === 'nageur' ? '/mon-calendrier' : '/mon-planning';
  }

  private loadCalendar(from: string, to: string): void {
    this.loading.set(true);
    this.error.set(null);
    this.weekChanged.emit({ from, to });

    const request =
      this.role() === 'coach'
        ? this.calendarService.getCoachCalendar(from, to)
        : this.calendarService.getNageurCalendar(from, to);

    request.subscribe({
      next: (data) => {
        this.calendar.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger le calendrier.');
        this.loading.set(false);
      },
    });
  }

  private toISODate(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }
}
