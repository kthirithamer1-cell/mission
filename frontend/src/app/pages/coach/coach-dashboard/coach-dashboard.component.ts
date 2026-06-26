import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  viewChild,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { CoachDashboardService } from '../../../core/services/coach-dashboard.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { AuthService } from '../../../core/services/auth.service';
import { EntraineurService } from '../../../core/services/entraineur.service';
import { PhotoUploadComponent } from '../../../shared/photo-upload/photo-upload.component';
import { SeanceWeekCalendarComponent } from '../../../shared/seance-week-calendar/seance-week-calendar.component';
import { CoachDashboardStats } from '../../../core/models/app.models';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-coach-dashboard',
  standalone: true,
  imports: [RouterLink, PhotoUploadComponent, SeanceWeekCalendarComponent],
  templateUrl: './coach-dashboard.component.html',
  styleUrls: ['./coach-dashboard.component.css']
})
export class CoachDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly dashboardService = inject(CoachDashboardService);
  private readonly entraineurService = inject(EntraineurService);
  private readonly ui = inject(AdminUiService);
  private readonly auth = inject(AuthService);

  private readonly barCanvas = viewChild<ElementRef<HTMLCanvasElement>>('barCanvas');

  readonly stats = signal<CoachDashboardStats | null>(null);
  readonly uploadingPhoto = signal<boolean>(false);
  readonly currentUser = this.auth.currentUser;

  private attendanceChart: Chart<'bar'> | null = null;

  ngOnInit(): void {
    this.loadData();
  }

  ngAfterViewInit(): void {
    this.initChart();
  }

  ngOnDestroy(): void {
    this.attendanceChart?.destroy();
  }

  loadData(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        setTimeout(() => this.initChart(), 0);
      },
      error: (err) => this.ui.toast('Erreur lors du chargement des statistiques', 'error'),
    });
  }

  onPhotoSelected(file: File): void {
    this.uploadingPhoto.set(true);
    this.entraineurService.uploadPhoto(file).subscribe({
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

  refresh(): void {
    this.loadData();
    this.ui.toast('Statistiques actualisées', 'success');
  }

  private initChart(): void {
    const canvas = this.barCanvas()?.nativeElement;
    const data = this.stats();
    if (!canvas || !data || !data.attendanceRateBySession) return;

    this.attendanceChart?.destroy();

    const labels = Object.keys(data.attendanceRateBySession);
    const values = Object.values(data.attendanceRateBySession);

    const isLight = document.body.getAttribute('data-theme') === 'light';
    const textColor = isLight ? '#4b5563' : '#9ca3af';
    const gridColor = isLight ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)';

    this.attendanceChart = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Taux de présence (%)',
          data: values,
          backgroundColor: 'rgba(217, 119, 6, 0.75)',
          borderColor: '#d97706',
          borderWidth: 2,
          borderRadius: 8,
          hoverBackgroundColor: 'rgba(217, 119, 6, 0.95)'
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
            min: 0,
            max: 100,
            ticks: { color: textColor, font: { family: 'Poppins' } },
            grid: { color: gridColor }
          }
        }
      }
    });
  }
}
