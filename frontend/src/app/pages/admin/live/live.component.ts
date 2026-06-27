import { Component, OnInit, OnDestroy, inject, signal, computed, viewChild, ElementRef, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CompetitionService } from '../../../core/services/competition.service';
import { EpreuveService } from '../../../core/services/epreuve.service';
import { NageurService } from '../../../core/services/nageur.service';
import { ResultatService } from '../../../core/services/resultat.service';
import { LiveService } from '../../../core/services/live.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Competition, Epreuve, Nageur, LiveResultEvent, Resultat } from '../../../core/models/app.models';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-live',
  imports: [CommonModule, FormsModule],
  templateUrl: './live.component.html',
})
export class LiveComponent implements OnInit, OnDestroy {
  private readonly competitionService = inject(CompetitionService);
  private readonly epreuveService = inject(EpreuveService);
  private readonly nageurService = inject(NageurService);
  private readonly resultatService = inject(ResultatService);
  private readonly liveService = inject(LiveService);
  private readonly ui = inject(AdminUiService);

  readonly competitions = signal<Competition[]>([]);
  readonly epreuves = signal<Epreuve[]>([]);
  readonly nageurs = signal<Nageur[]>([]);

  readonly selectedCompetitionId = signal<number | null>(null);
  readonly selectedEpreuveId = signal<number | null>(null);
  readonly selectedNageurId = signal<number | null>(null);

  readonly temps = signal<string>('');
  readonly classement = signal<number | null>(null);
  readonly points = signal<number | null>(null);
  readonly record = signal<boolean>(false);

  readonly liveEvents = signal<LiveResultEvent[]>([]);
  readonly isConnecting = signal<boolean>(false);

  private liveSub: Subscription | null = null;
  private chartInstance: any = null;
  private readonly liveChartCanvas = viewChild<ElementRef<HTMLCanvasElement>>('liveChartCanvas');

  protected readonly Number = Number;

  constructor() {
    effect(() => {
      this.updateChart();
    });
  }

  readonly epreuvesForCompetition = computed(() => {
    const selectedId = this.selectedCompetitionId();
    const list = this.epreuves();
    if (!selectedId) {
      return list;
    }
    return list.filter((e) => e.competitionId === selectedId);
  });

  ngOnInit(): void {
    this.loadCompetitions();
    this.loadEpreuves();
    this.loadNageurs();
  }

  ngOnDestroy(): void {
    this.disconnect();
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }
  }

  loadCompetitions(): void {
    this.competitionService.getEnCours().subscribe({
      next: (data) => {
        if (data.length === 0) {
          this.competitionService.getAll().subscribe({
            next: (all) => {
              this.competitions.set(all);
              this.selectCompetition(all[0]?.id ?? null);
            },
            error: () => this.ui.toast('Erreur lors du chargement des competitions', 'error')
          });
          return;
        }
        this.competitions.set(data);
        this.selectCompetition(data[0]?.id ?? null);
      },
      error: () => this.ui.toast('Erreur lors du chargement des competitions', 'error')
    });
  }

  loadEpreuves(): void {
    this.epreuveService.getAll().subscribe({
      next: (data) => {
        this.epreuves.set(data);
        this.syncEpreuveSelection();
      },
      error: () => this.ui.toast('Erreur lors du chargement des epreuves', 'error')
    });
  }

  loadNageurs(): void {
    this.nageurService.getAll().subscribe({
      next: (data) => {
        this.nageurs.set(data);
        if (data.length > 0 && !this.selectedNageurId()) {
          this.selectedNageurId.set(data[0].id ?? null);
        }
      },
      error: () => this.ui.toast('Erreur lors du chargement des nageurs', 'error')
    });
  }

  selectCompetition(competitionId: number | null): void {
    this.selectedCompetitionId.set(competitionId);
    this.syncEpreuveSelection();
    this.connect(competitionId ?? undefined);
  }

  connect(competitionId?: number): void {
    this.disconnect();
    this.isConnecting.set(true);
    this.liveSub = this.liveService.connect(competitionId).subscribe({
      next: (event) => {
        const list = [event, ...this.liveEvents()].slice(0, 50);
        this.liveEvents.set(list);
      },
      error: () => this.ui.toast('Erreur de connexion au live', 'error')
    });
    this.isConnecting.set(false);
  }

  disconnect(): void {
    if (this.liveSub) {
      this.liveSub.unsubscribe();
      this.liveSub = null;
    }
    this.liveService.disconnect();
  }

  submitLiveResult(): void {
    if (!this.selectedEpreuveId() || !this.selectedNageurId()) {
      this.ui.toast('Veuillez choisir une epreuve et un nageur', 'error');
      return;
    }
    if (!this.temps().trim() || !this.classement()) {
      this.ui.toast('Le temps et le classement sont requis', 'error');
      return;
    }

    const today = new Date().toISOString().split('T')[0];
    const payload: Resultat = {
      temps: this.temps().trim(),
      classement: this.classement() ?? 0,
      points: this.points() ?? undefined,
      record: this.record(),
      dateCompetition: today,
      nageurId: this.selectedNageurId() ?? undefined,
      epreuveId: this.selectedEpreuveId() ?? undefined
    };

    this.resultatService.submitLive(payload).subscribe({
      next: () => {
        this.ui.toast('Resultat live publie', 'success');
        this.temps.set('');
        this.classement.set(null);
        this.points.set(null);
        this.record.set(false);
      },
      error: () => this.ui.toast('Erreur lors de la publication live', 'error')
    });
  }

  private syncEpreuveSelection(): void {
    const list = this.epreuvesForCompetition();
    const current = this.selectedEpreuveId();
    if (!current || !list.find((e) => e.id === current)) {
      this.selectedEpreuveId.set(list[0]?.id ?? null);
    }
  }

  private updateChart(): void {
    const canvas = this.liveChartCanvas()?.nativeElement;
    const events = this.liveEvents();
    if (!canvas) return;

    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }

    if (events.length === 0) return;

    // Group by swimmer for points
    const pointsMap = new Map<string, number>();
    events.forEach(e => {
      if (e.points && e.nageurNom) {
        const current = pointsMap.get(e.nageurNom) || 0;
        pointsMap.set(e.nageurNom, current + e.points);
      }
    });

    const labels = Array.from(pointsMap.keys());
    const data = Array.from(pointsMap.values());

    if (labels.length === 0) return;

    const isLight = document.body.getAttribute('data-theme') === 'light';
    const textColor = isLight ? '#475569' : '#94a3b8';
    const gridColor = isLight ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)';

    this.chartInstance = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Total Points FINA (Live)',
          data: data,
          backgroundColor: '#3b82f6',
          borderRadius: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: { ticks: { color: textColor }, grid: { display: false } },
          y: { ticks: { color: textColor }, grid: { color: gridColor } }
        }
      } as any
    });
  }
}
