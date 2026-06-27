import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompetitionService } from '../../../core/services/competition.service';
import { ParticipationService } from '../../../core/services/participation.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Competition, ParticipationDTO } from '../../../core/models/app.models';

@Component({
  selector: 'app-competitions',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './competitions.component.html',
})
export class CompetitionsComponent implements OnInit {
  private readonly competitionService = inject(CompetitionService);
  private readonly participationService = inject(ParticipationService);
  private readonly ui = inject(AdminUiService);

  readonly competitions = signal<Competition[]>([]);

  readonly filterStatut = signal<string>('TOUS');
  readonly filterType = signal<string>('TOUS');

  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  formCompetition = signal<Partial<Competition>>({
    nom: '',
    lieu: '',
    dateDebut: '',
    dateFin: '',
    type: 'CHAMPIONNAT',
    statut: 'A_VENIR',
    niveau: 'LOCAL',
    organisateur: '',
    description: '',
    saison: '2025-2026'
  });

  // Participant panel state
  expandedCompetitionId = signal<number | null>(null);
  participants = signal<ParticipationDTO[]>([]);
  participantFilter = signal<string>('TOUS');
  loadingParticipants = signal<boolean>(false);

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.competitionService.getAll().subscribe({
      next: (data) => this.competitions.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des compétitions', 'error')
    });
  }

  getFilteredCompetitions(): Competition[] {
    let list = this.competitions();
    const status = this.filterStatut();
    const type = this.filterType();
    if (status !== 'TOUS') list = list.filter(c => c.statut === status);
    if (type !== 'TOUS') list = list.filter(c => c.type === type);
    return list.sort((a, b) => (b.dateDebut || '').localeCompare(a.dateDebut || ''));
  }

  openAddModal(): void {
    this.isEditMode.set(false);
    this.formCompetition.set({
      nom: '', lieu: '', dateDebut: new Date().toISOString().split('T')[0],
      dateFin: new Date().toISOString().split('T')[0], type: 'CHAMPIONNAT',
      statut: 'A_VENIR', niveau: 'LOCAL', organisateur: '', description: '', saison: '2025-2026'
    });
    this.showModal.set(true);
  }

  openEditModal(comp: Competition): void {
    this.isEditMode.set(true);
    this.formCompetition.set({
      id: comp.id, nom: comp.nom, lieu: comp.lieu, dateDebut: comp.dateDebut,
      dateFin: comp.dateFin, type: comp.type, statut: comp.statut, niveau: comp.niveau,
      organisateur: comp.organisateur, description: comp.description || '', saison: comp.saison || '2025-2026'
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveCompetition(): void {
    const compData = this.formCompetition() as Competition;
    if (!compData.nom || !compData.lieu || !compData.dateDebut || !compData.dateFin) {
      this.ui.toast('Veuillez remplir les champs obligatoires', 'error');
      return;
    }
    if (this.isEditMode()) {
      this.competitionService.update(compData.id!, compData).subscribe({
        next: () => { this.ui.toast('Compétition mise à jour', 'success'); this.closeModal(); this.loadData(); },
        error: () => this.ui.toast('Erreur lors de la modification', 'error')
      });
    } else {
      this.competitionService.create(compData).subscribe({
        next: () => { this.ui.toast('Compétition créée avec succès', 'success'); this.closeModal(); this.loadData(); },
        error: () => this.ui.toast('Erreur lors de la création', 'error')
      });
    }
  }

  deleteCompetition(id: number): void {
    if (!confirm('Supprimer cette compétition ? Les inscriptions associées seront aussi supprimées.')) return;
    this.competitionService.delete(id).subscribe({
      next: () => { this.ui.toast('Compétition supprimée', 'success'); this.loadData(); },
      error: () => this.ui.toast('Erreur lors de la suppression', 'error')
    });
  }

  // --- Participant management (inline) ---

  toggleParticipants(compId: number): void {
    if (this.expandedCompetitionId() === compId) {
      this.expandedCompetitionId.set(null);
      return;
    }
    this.expandedCompetitionId.set(compId);
    this.participantFilter.set('TOUS');
    this.loadingParticipants.set(true);
    this.participants.set([]);
    this.participationService.getByCompetition(compId).subscribe({
      next: (data) => { this.participants.set(data); this.loadingParticipants.set(false); },
      error: () => { this.ui.toast('Erreur chargement participants', 'error'); this.loadingParticipants.set(false); }
    });
  }

  getFilteredParticipants(): ParticipationDTO[] {
    const status = this.participantFilter();
    if (status === 'TOUS') return this.participants();
    return this.participants().filter(p => p.statut === status);
  }

  getStatusBadge(status: string): string {
    switch (status) {
      case 'TERMINE': return 'badge-inactive';
      case 'EN_COURS': return 'badge-active';
      case 'A_VENIR': return 'badge-pending';
      default: return 'badge-pending';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'TERMINE': return 'Terminée';
      case 'EN_COURS': return 'En cours';
      case 'A_VENIR': return 'À venir';
      default: return status;
    }
  }

  getParticipantBadge(statut: string): string {
    switch (statut) {
      case 'INSCRIT': return 'badge-pending';
      case 'PRESENT': return 'badge-active';
      case 'ABSENT': return 'badge-inactive';
      case 'ANNULE': return 'badge-inactive';
      default: return 'badge-pending';
    }
  }

  getParticipantText(statut: string): string {
    switch (statut) {
      case 'INSCRIT': return 'Inscrit';
      case 'PRESENT': return 'Présent';
      case 'ABSENT': return 'Absent';
      case 'ANNULE': return 'Annulé';
      default: return statut;
    }
  }

  updateStatus(participation: ParticipationDTO, statut: string): void {
    if (!participation.id) return;
    this.participationService.updateStatus(participation.id, statut).subscribe({
      next: () => {
        this.ui.toast('Statut mis à jour', 'success');
        this.loadParticipantsForCurrent();
      },
      error: () => this.ui.toast('Erreur mise à jour', 'error')
    });
  }

  removeParticipant(participation: ParticipationDTO): void {
    if (!confirm(`Retirer ${participation.nageurPrenom} ${participation.nageurNom} ?`)) return;
    if (!participation.id) return;
    this.participationService.remove(participation.id).subscribe({
      next: () => { this.ui.toast('Participant retiré', 'success'); this.loadParticipantsForCurrent(); },
      error: () => this.ui.toast('Erreur suppression', 'error')
    });
  }

  private loadParticipantsForCurrent(): void {
    const cid = this.expandedCompetitionId();
    if (cid == null) return;
    this.loadingParticipants.set(true);
    this.participationService.getByCompetition(cid).subscribe({
      next: (data) => { this.participants.set(data); this.loadingParticipants.set(false); },
      error: () => this.loadingParticipants.set(false)
    });
  }
}
