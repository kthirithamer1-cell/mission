import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  computed,
  viewChild,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminChartsService } from '../../../core/services/admin-charts.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { DashboardService } from '../../../core/services/dashboard.service';
import { ReservationService } from '../../../core/services/reservation.service';
import { SeanceService } from '../../../core/services/seance.service';
import { DashboardStats, Reservation, Seance } from '../../../core/models/app.models';

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterLink],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly charts = inject(AdminChartsService);
  private readonly ui = inject(AdminUiService);
  private readonly dashboardService = inject(DashboardService);
  private readonly reservationService = inject(ReservationService);
  private readonly seanceService = inject(SeanceService);

  private readonly pieCanvas = viewChild<ElementRef<HTMLCanvasElement>>('pieCanvas');
  private readonly lineCanvas = viewChild<ElementRef<HTMLCanvasElement>>('lineCanvas');
  private readonly legendEl = viewChild<ElementRef<HTMLElement>>('pieLegend');

  readonly stats = signal<DashboardStats | null>(null);
  readonly reservations = signal<Reservation[]>([]);
  readonly seances = signal<Seance[]>([]);

  readonly pendingReservations = computed(() =>
    this.reservations().filter((r) => r.statut === 'EN_ATTENTE')
  );

  readonly upcomingSeance = computed(() => {
    const list = this.seances();
    if (list.length === 0) return null;
    const sorted = [...list].sort((a, b) => new Date(a.date + 'T' + a.heureDebut).getTime() - new Date(b.date + 'T' + b.heureDebut).getTime());
    return sorted[0];
  });

  readonly calendarWeek = computed(() => {
    const today = new Date();
    const dayOfWeek = today.getDay();
    const monday = new Date(today);
    monday.setDate(today.getDate() - ((dayOfWeek + 6) % 7));
    const days = [];
    for (let i = 0; i < 7; i++) {
      const d = new Date(monday);
      d.setDate(monday.getDate() + i);
      const dateStr = d.toISOString().split('T')[0];
      days.push({
        date: dateStr,
        label: d.toLocaleDateString('fr-FR', { weekday: 'short', day: 'numeric' }),
        isToday: dateStr === today.toISOString().split('T')[0],
        reservations: this.reservations().filter(r => r.date === dateStr),
      });
    }
    return days;
  });

  readonly piscinesFromReservations = computed(() => {
    const names = new Set<string>();
    this.reservations().forEach(r => { if (r.piscineNom) names.add(r.piscineNom); });
    return Array.from(names);
  });

  ngOnInit(): void {
    window.scrollTo({ top: 0, behavior: 'instant' });
    this.loadData();
  }

  ngAfterViewInit(): void {
    this.initCharts();
  }

  ngOnDestroy(): void {
    this.charts.destroy();
  }

  loadData(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.initCharts();
      },
      error: (err) => this.ui.toast('Erreur chargement statistiques', 'error'),
    });

    this.reservationService.getAll().subscribe({
      next: (data) => this.reservations.set(data),
      error: (err) => this.ui.toast('Erreur chargement réservations', 'error'),
    });

    this.seanceService.getAll().subscribe({
      next: (data) => this.seances.set(data),
      error: (err) => this.ui.toast('Erreur chargement séances', 'error'),
    });
  }

  private initCharts(): void {
    const pie = this.pieCanvas()?.nativeElement;
    const line = this.lineCanvas()?.nativeElement;
    const legend = this.legendEl()?.nativeElement;
    const data = this.stats();
    if (pie && line && legend && data) {
      this.charts.init(pie, line, legend, data);
    }
  }

  refresh(): void {
    this.loadData();
    this.ui.toast('Tableau de bord actualisé', 'success');
  }

  requestSlot(): void {
    this.ui.toast('Redirection vers la gestion des réservations pour demander un créneau', 'info');
  }
}
