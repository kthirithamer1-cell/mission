import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  viewChild,
  computed
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { NageurDashboardService } from '../../../core/services/nageur-dashboard.service';
import { NageurService } from '../../../core/services/nageur.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { AuthService } from '../../../core/services/auth.service';
import { PhotoUploadComponent } from '../../../shared/photo-upload/photo-upload.component';
import { SeanceWeekCalendarComponent } from '../../../shared/seance-week-calendar/seance-week-calendar.component';
import { NageurDashboardStats } from '../../../core/models/app.models';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-nageur-dashboard',
  standalone: true,
  imports: [RouterLink, PhotoUploadComponent, SeanceWeekCalendarComponent],
  templateUrl: './nageur-dashboard.component.html',
  styleUrls: ['./nageur-dashboard.component.css']
})
export class NageurDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly dashboardService = inject(NageurDashboardService);
  private readonly nageurService = inject(NageurService);
  private readonly ui = inject(AdminUiService);
  private readonly auth = inject(AuthService);

  private readonly lineCanvas = viewChild<ElementRef<HTMLCanvasElement>>('lineCanvas');

  readonly stats = signal<NageurDashboardStats | null>(null);
  readonly uploadingPhoto = signal<boolean>(false);
  readonly currentUser = this.auth.currentUser;
  
  readonly selectedEvent = signal<string>('');
  readonly availableEvents = computed(() => {
    const data = this.stats();
    if (!data || !data.progressionData) return [];
    return Object.keys(data.progressionData);
  });

  private progressionChart: Chart<'line'> | null = null;

  ngOnInit(): void {
    this.loadData();
  }

  ngAfterViewInit(): void {
    this.initChart();
  }

  ngOnDestroy(): void {
    this.progressionChart?.destroy();
  }

  loadData(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        if (data.progressionData) {
          const keys = Object.keys(data.progressionData);
          if (keys.length > 0 && !this.selectedEvent()) {
            this.selectedEvent.set(keys[0]);
          }
        }
        setTimeout(() => this.initChart(), 0);
      },
      error: (err) => this.ui.toast('Erreur lors du chargement des statistiques', 'error'),
    });
  }

  onPhotoSelected(file: File): void {
    this.uploadingPhoto.set(true);
    this.nageurService.uploadPhoto(file).subscribe({
      next: (updatedUser) => {
        this.uploadingPhoto.set(false);
        this.ui.toast('Photo de profil mise à jour', 'success');
        
        // Update user session
        const current = this.auth.currentUser();
        if (current) {
          this.auth.updateUserSession({
            ...current,
            photoUrl: updatedUser.photoUrl
          });
        }
        
        this.loadData();
      },
      error: (err) => {
        this.uploadingPhoto.set(false);
        this.ui.toast('Erreur lors du chargement de la photo', 'error');
      }
    });
  }

  onEventChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.selectedEvent.set(select.value);
    this.initChart();
  }

  refresh(): void {
    this.loadData();
    this.ui.toast('Statistiques actualisées', 'success');
  }

  private initChart(): void {
    const canvas = this.lineCanvas()?.nativeElement;
    const data = this.stats();
    const activeEvent = this.selectedEvent();
    
    if (!canvas || !data || !data.progressionData || !activeEvent) return;

    this.progressionChart?.destroy();

    const times = data.progressionData[activeEvent] || [];
    // Generate chronological trial labels: Run 1, Run 2, Run 3...
    const labels = times.map((_, index) => `Course ${index + 1}`);

    const isLight = document.body.getAttribute('data-theme') === 'light';
    const textColor = isLight ? '#4b5563' : '#9ca3af';
    const gridColor = isLight ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)';

    this.progressionChart = new Chart(canvas, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Temps (secondes)',
          data: times,
          borderColor: '#10b981',
          backgroundColor: 'rgba(16, 185, 129, 0.1)',
          borderWidth: 3,
          tension: 0.3,
          fill: true,
          pointBackgroundColor: '#10b981',
          pointBorderColor: '#ffffff',
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: {
            ticks: { color: textColor, font: { family: 'Poppins' } },
            grid: { color: gridColor }
          },
          y: {
            ticks: { 
              color: textColor, 
              font: { family: 'Poppins' },
              callback: (val) => {
                // Convert seconds back to mm:ss format if necessary or keep numeric
                const totalSeconds = Number(val);
                if (totalSeconds >= 60) {
                  const minutes = Math.floor(totalSeconds / 60);
                  const seconds = Math.round((totalSeconds % 60) * 100) / 100;
                  return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
                }
                return `${totalSeconds}s`;
              }
            },
            grid: { color: gridColor }
          }
        }
      }
    });
  }
}
