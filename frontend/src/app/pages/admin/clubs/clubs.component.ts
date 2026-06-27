import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClubService } from '../../../core/services/club.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Club } from '../../../core/models/app.models';

@Component({
  selector: 'app-clubs',
  imports: [FormsModule],
  templateUrl: './clubs.component.html',
})
export class ClubsComponent implements OnInit {
  private readonly clubService = inject(ClubService);
  private readonly ui = inject(AdminUiService);

  readonly clubs = signal<Club[]>([]);

  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  formClub = signal<Partial<Club>>({
    nom: '',
    adresse: '',
    dateAffiliation: '',
  });

  ngOnInit(): void {
    this.loadClubs();
  }

  loadClubs(): void {
    this.clubService.getAll().subscribe({
      next: (data) => this.clubs.set(data),
      error: () => this.ui.toast('Erreur chargement clubs', 'error'),
    });
  }

  refresh(): void {
    this.loadClubs();
    this.ui.toast('Liste actualisée', 'success');
  }

  openAddModal(): void {
    this.isEditMode.set(false);
    this.formClub.set({ nom: '', adresse: '', dateAffiliation: '' });
    this.showModal.set(true);
  }

  openEditModal(club: Club): void {
    this.isEditMode.set(true);
    this.formClub.set({
      id: club.id,
      nom: club.nom,
      adresse: club.adresse,
      dateAffiliation: club.dateAffiliation,
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveClub(): void {
    const clubData = this.formClub() as Club;
    if (!clubData.nom) {
      this.ui.toast('Veuillez remplir le nom du club', 'error');
      return;
    }

    if (this.isEditMode() && clubData.id) {
      this.clubService.update(clubData.id, clubData).subscribe({
        next: () => {
          this.ui.toast('Club mis à jour avec succès', 'success');
          this.closeModal();
          this.loadClubs();
        },
        error: () => this.ui.toast('Erreur lors de la mise à jour', 'error'),
      });
    } else {
      this.clubService.create(clubData).subscribe({
        next: () => {
          this.ui.toast('Club créé avec succès', 'success');
          this.closeModal();
          this.loadClubs();
        },
        error: () => this.ui.toast('Erreur lors de la création', 'error'),
      });
    }
  }

  deleteClub(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer ce club ?')) {
      this.clubService.delete(id).subscribe({
        next: () => {
          this.ui.toast('Club supprimé avec succès', 'success');
          this.loadClubs();
        },
        error: () => this.ui.toast('Erreur lors de la suppression', 'error'),
      });
    }
  }
}
