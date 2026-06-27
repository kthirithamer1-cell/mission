import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompetitionService } from '../../../core/services/competition.service';
import { ParticipationService } from '../../../core/services/participation.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { AuthService } from '../../../core/services/auth.service';
import { Competition, ParticipationDTO } from '../../../core/models/app.models';

@Component({
  selector: 'app-nageur-competitions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nageur-competitions.component.html',
})
export class NageurCompetitionsComponent implements OnInit {
  private readonly competitionService = inject(CompetitionService);
  private readonly participationService = inject(ParticipationService);
  private readonly ui = inject(AdminUiService);
  private readonly auth = inject(AuthService);

  readonly competitions = signal<Competition[]>([]);
  readonly myRegistrations = signal<ParticipationDTO[]>([]);
  readonly registrationCounts = signal<{ [key: number]: number }>({});

  readonly filterStatut = signal<string>('A_VENIR');
  readonly filterType = signal<string>('TOUS');

  readonly currentUser = this.auth.currentUser;

  ngOnInit(): void {
    this.loadOpenCompetitions();
    this.loadMyRegistrations();
  }

  loadOpenCompetitions(): void {
    this.competitionService.getAll().subscribe({
      next: (data) => {
        const filtered = data.filter(c => c.statut === 'A_VENIR' || c.statut === 'EN_COURS');
        this.competitions.set(filtered);
        this.loadAllCounts(filtered);
      },
      error: () => this.ui.toast('Erreur chargement compétitions', 'error'),
    });
  }

  loadMyRegistrations(): void {
    const uid = this.currentUser()?.id;
    if (!uid) return;
    this.participationService.getByNageur(uid).subscribe({
      next: (data) => this.myRegistrations.set(data),
      error: () => {},
    });
  }

  loadAllCounts(competitions: Competition[]): void {
    const counts: { [key: number]: number } = {};
    let pending = 0;
    competitions.forEach(c => {
      if (!c.id) return;
      this.participationService.countByCompetition(c.id).subscribe({
        next: (count) => {
          counts[c.id!] = count;
          this.registrationCounts.set(counts);
          pending++;
        },
        error: () => { pending++; },
      });
    });
  }

  getFilteredCompetitions(): Competition[] {
    let list = this.competitions();
    const status = this.filterStatut();
    const type = this.filterType();
    if (status !== 'TOUS') {
      list = list.filter(c => c.statut === status);
    }
    if (type !== 'TOUS') {
      list = list.filter(c => c.type === type);
    }
    return list.sort((a, b) => (a.dateDebut || '').localeCompare(b.dateDebut || ''));
  }

  isRegistered(competitionId: number): boolean {
    return this.myRegistrations().some(r => r.competitionId === competitionId && r.statut !== 'ANNULE');
  }

  getRegistrationFor(competitionId: number): ParticipationDTO | undefined {
    return this.myRegistrations().find(r => r.competitionId === competitionId && r.statut !== 'ANNULE');
  }

  register(competitionId: number): void {
    this.participationService.register(competitionId).subscribe({
      next: () => {
        this.ui.toast('Inscription confirmée !', 'success');
        this.loadMyRegistrations();
        this.loadOpenCompetitions();
      },
      error: (err) => this.ui.toast(err?.error || 'Erreur inscription', 'error'),
    });
  }

  cancel(competitionId: number): void {
    if (!confirm('Annuler votre inscription à cette compétition ?')) return;
    this.participationService.cancel(competitionId).subscribe({
      next: () => {
        this.ui.toast('Inscription annulée', 'info');
        this.loadMyRegistrations();
        this.loadOpenCompetitions();
      },
      error: (err) => this.ui.toast(err?.error || 'Erreur annulation', 'error'),
    });
  }

  getStatusBadge(statut: string): string {
    switch (statut) {
      case 'INSCRIT': return 'badge-pending';
      case 'PRESENT': return 'badge-active';
      case 'ABSENT': return 'badge-inactive';
      case 'ANNULE': return 'badge-inactive';
      default: return 'badge-pending';
    }
  }

  getStatusText(statut: string): string {
    switch (statut) {
      case 'INSCRIT': return 'Inscrit';
      case 'PRESENT': return 'Présent';
      case 'ABSENT': return 'Absent';
      case 'ANNULE': return 'Annulé';
      default: return statut;
    }
  }
}
