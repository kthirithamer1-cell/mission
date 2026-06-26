import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompetitionService } from '../../../core/services/competition.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Competition } from '../../../core/models/app.models';

@Component({
  selector: 'app-competitions',
  imports: [CommonModule, FormsModule],
  templateUrl: './competitions.component.html',
})
export class CompetitionsComponent implements OnInit {
  private readonly competitionService = inject(CompetitionService);
  private readonly ui = inject(AdminUiService);

  readonly competitions = signal<Competition[]>([]);
  
  // Filter states
  readonly filterStatut = signal<string>('TOUS');
  readonly filterType = signal<string>('TOUS');

  // Modal / Form state
  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  // Form fields
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

    if (status !== 'TOUS') {
      list = list.filter(c => c.statut === status);
    }
    if (type !== 'TOUS') {
      list = list.filter(c => c.type === type);
    }

    // Sort by start date descending
    return list.sort((a, b) => {
      if (!a.dateDebut) return 1;
      if (!b.dateDebut) return -1;
      return b.dateDebut.localeCompare(a.dateDebut);
    });
  }

  openAddModal(): void {
    this.isEditMode.set(false);
    this.formCompetition.set({
      nom: '',
      lieu: '',
      dateDebut: new Date().toISOString().split('T')[0],
      dateFin: new Date().toISOString().split('T')[0],
      type: 'CHAMPIONNAT',
      statut: 'A_VENIR',
      niveau: 'LOCAL',
      organisateur: 'Club Aquapulse',
      description: '',
      saison: '2025-2026'
    });
    this.showModal.set(true);
  }

  openEditModal(comp: Competition): void {
    this.isEditMode.set(true);
    this.formCompetition.set({
      id: comp.id,
      nom: comp.nom,
      lieu: comp.lieu,
      dateDebut: comp.dateDebut,
      dateFin: comp.dateFin,
      type: comp.type,
      statut: comp.statut,
      niveau: comp.niveau,
      organisateur: comp.organisateur,
      description: comp.description || '',
      saison: comp.saison || '2025-2026'
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
        next: () => {
          this.ui.toast('Compétition mise à jour avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la modification', 'error')
      });
    } else {
      this.competitionService.create(compData).subscribe({
        next: () => {
          this.ui.toast('Compétition créée avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la création', 'error')
      });
    }
  }

  deleteCompetition(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer cette compétition ? Toutes les épreuves et résultats associés seront également supprimés.')) {
      this.competitionService.delete(id).subscribe({
        next: () => {
          this.ui.toast('Compétition supprimée avec succès', 'success');
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la suppression', 'error')
      });
    }
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'TERMINE': return 'badge-inactive'; // Grey/Red
      case 'EN_COURS': return 'badge-active';   // Blue/Green
      case 'A_VENIR': return 'badge-pending';   // Yellow
      default: return 'badge-pending';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'TERMINE': return 'Terminée';
      case 'EN_COURS': return 'En cours 🔴';
      case 'A_VENIR': return 'À venir';
      default: return status;
    }
  }
}
