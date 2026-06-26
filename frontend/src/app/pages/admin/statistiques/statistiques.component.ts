import { Component, OnInit, OnDestroy, inject, signal, viewChild, ElementRef, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StatistiqueService } from '../../../core/services/statistique.service';
import { NageurService } from '../../../core/services/nageur.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Nageur, ClubStats, SwimmerStats, Resultat } from '../../../core/models/app.models';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-statistiques',
  imports: [CommonModule, FormsModule],
  templateUrl: './statistiques.component.html',
})
export class StatistiquesComponent implements OnInit, OnDestroy {
  private readonly statsService = inject(StatistiqueService);
  private readonly nageurService = inject(NageurService);
  private readonly ui = inject(AdminUiService);

  private readonly stylesCanvas = viewChild<ElementRef<HTMLCanvasElement>>('stylesCanvas');
  private readonly progressionCanvas = viewChild<ElementRef<HTMLCanvasElement>>('progressionCanvas');

  readonly swimmers = signal<Nageur[]>([]);
  readonly selectedSwimmerId = signal<number | null>(null);

  readonly clubStats = signal<ClubStats | null>(null);
  readonly swimmerStats = signal<SwimmerStats | null>(null);

  // Selected event for progression chart
  readonly selectedEvent = signal<string>('');
  readonly swimmerEvents = computed(() => {
    const stats = this.swimmerStats();
    if (!stats || !stats.progressions) return [];
    return Object.keys(stats.progressions);
  });

  private stylesChartInstance: any = null;
  private progressionChartInstance: any = null;

  constructor() {
    // Redraw charts when data changes
    effect(() => {
      this.drawStylesChart();
    });

    effect(() => {
      this.drawProgressionChart();
    });
  }

  ngOnInit(): void {
    this.loadSwimmers();
    this.loadClubStats();
  }

  ngOnDestroy(): void {
    this.destroyCharts();
  }

  loadSwimmers(): void {
    this.nageurService.getAll().subscribe({
      next: (data) => {
        this.swimmers.set(data);
        if (data.length > 0) {
          this.selectSwimmer(data[0].id!);
        }
      },
      error: () => this.ui.toast('Erreur lors du chargement des nageurs', 'error')
    });
  }

  loadClubStats(): void {
    this.statsService.getClubStats().subscribe({
      next: (data) => this.clubStats.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des statistiques du club', 'error')
    });
  }

  selectSwimmer(swimmerId: number): void {
    this.selectedSwimmerId.set(swimmerId);
    this.statsService.getSwimmerStats(swimmerId).subscribe({
      next: (data) => {
        this.swimmerStats.set(data);
        const events = Object.keys(data.progressions || {});
        if (events.length > 0) {
          this.selectedEvent.set(events[0]);
        } else {
          this.selectedEvent.set('');
        }
      },
      error: () => this.ui.toast('Erreur lors du chargement du profil statistique', 'error')
    });
  }

  onEventChange(event: string): void {
    this.selectedEvent.set(event);
    this.drawProgressionChart();
  }

  private destroyCharts(): void {
    if (this.stylesChartInstance) {
      this.stylesChartInstance.destroy();
      this.stylesChartInstance = null;
    }
    if (this.progressionChartInstance) {
      this.progressionChartInstance.destroy();
      this.progressionChartInstance = null;
    }
  }

  private drawStylesChart(): void {
    const canvas = this.stylesCanvas()?.nativeElement;
    const stats = this.clubStats();
    if (!canvas || !stats || !stats.repartitionStyles) return;

    if (this.stylesChartInstance) {
      this.stylesChartInstance.destroy();
    }

    const labels = Object.keys(stats.repartitionStyles);
    const data = Object.values(stats.repartitionStyles);

    const isLight = document.body.getAttribute('data-theme') === 'light';
    const textColor = isLight ? '#475569' : '#94a3b8';

    this.stylesChartInstance = new Chart(canvas, {
      type: 'doughnut',
      data: {
        labels: labels.map(l => this.formatStyleName(l)),
        datasets: [{
          data: data,
          backgroundColor: ['#818cf8', '#34d399', '#f87171', '#fbbf24', '#22d3ee'],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { color: textColor }
          }
        }
      } as any
    });
  }

  private drawProgressionChart(): void {
    const canvas = this.progressionCanvas()?.nativeElement;
    const stats = this.swimmerStats();
    const event = this.selectedEvent();
    
    if (!canvas) return;

    if (this.progressionChartInstance) {
      this.progressionChartInstance.destroy();
      this.progressionChartInstance = null;
    }

    if (!stats || !event || !stats.progressions || !stats.progressions[event]) {
      return;
    }

    const resultsList: Resultat[] = stats.progressions[event];
    
    // Sort in case they aren't sorted
    const sortedResults = [...resultsList].sort((a, b) => {
      if (!a.dateCompetition) return -1;
      if (!b.dateCompetition) return 1;
      return a.dateCompetition.localeCompare(b.dateCompetition);
    });

    const labels = sortedResults.map(r => r.dateCompetition ? new Date(r.dateCompetition).toLocaleDateString() : 'Date inconnue');
    const pointsData = sortedResults.map(r => r.points || 0);

    const isLight = document.body.getAttribute('data-theme') === 'light';
    const textColor = isLight ? '#475569' : '#94a3b8';
    const gridColor = isLight ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)';

    this.progressionChartInstance = new Chart(canvas, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Points FINA',
            data: pointsData,
            borderColor: '#6366f1',
            backgroundColor: 'rgba(99, 102, 241, 0.1)',
            fill: true,
            tension: 0.3,
            borderWidth: 3,
            pointBackgroundColor: '#4f46e5',
            pointRadius: 6
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: {
            grid: { color: gridColor },
            ticks: { color: textColor }
          },
          y: {
            grid: { color: gridColor },
            ticks: { color: textColor },
            title: {
              display: true,
              text: 'Points FINA',
              color: textColor
            }
          }
        }
      } as any
    });
  }

  formatStyleName(style: string): string {
    switch (style) {
      case 'NAGE_LIBRE': return 'Nage Libre';
      case 'PAPILLON': return 'Papillon';
      case 'DOS': return 'Dos';
      case 'BRASSE': return 'Brasse';
      case '4_NAGES': return '4 Nages';
      default: return style;
    }
  }
}
