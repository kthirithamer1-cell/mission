import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EntraineurService } from '../../../core/services/entraineur.service';
import { ClubService } from '../../../core/services/club.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Entraineur, Club } from '../../../core/models/app.models';

@Component({
  selector: 'app-entraineurs',
  imports: [RouterLink, FormsModule],
  templateUrl: './entraineurs.component.html',
})
export class EntraineursComponent implements OnInit {
  private readonly entraineurService = inject(EntraineurService);
  private readonly clubService = inject(ClubService);
  private readonly ui = inject(AdminUiService);

  readonly coaches = signal<Entraineur[]>([]);
  readonly clubs = signal<Club[]>([]);

  // Modal / Form state
  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  // Form fields
  formCoach = signal<Partial<Entraineur>>({
    nom: '',
    prenom: '',
    email: '',
    motDePasse: '',
    groupes: '',
    clubId: undefined,
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.entraineurService.getAll().subscribe({
      next: (data) => this.coaches.set(data),
      error: () => this.ui.toast('Erreur chargement entraîneurs', 'error'),
    });

    this.clubService.getAll().subscribe({
      next: (data) => {
        this.clubs.set(data);
        if (data.length > 0 && !this.formCoach().clubId) {
          this.formCoach.update(f => ({ ...f, clubId: data[0].id }));
        }
      },
      error: () => this.ui.toast('Erreur chargement clubs', 'error'),
    });
  }

  refresh(): void {
    this.loadData();
    this.ui.toast('Liste actualisée', 'success');
  }

  openAddModal(): void {
    this.isEditMode.set(false);
    this.formCoach.set({
      nom: '',
      prenom: '',
      email: '',
      motDePasse: 'coach123',
      groupes: '',
      clubId: this.clubs().length > 0 ? this.clubs()[0].id : undefined,
    });
    this.showModal.set(true);
  }

  openEditModal(coach: Entraineur): void {
    this.isEditMode.set(true);
    this.formCoach.set({
      id: coach.id,
      nom: coach.nom,
      prenom: coach.prenom,
      email: coach.email,
      groupes: coach.groupes || '',
      clubId: coach.clubId,
      role: coach.role || 'ENTRAINEUR',
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveCoach(): void {
    const coachData = this.formCoach() as Entraineur;
    if (!coachData.nom || !coachData.prenom || !coachData.email) {
      this.ui.toast('Veuillez remplir les champs obligatoires', 'error');
      return;
    }

    if (this.isEditMode()) {
      this.entraineurService.update(coachData.id!, coachData).subscribe({
        next: () => {
          this.ui.toast('Entraîneur mis à jour avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la mise à jour', 'error'),
      });
    } else {
      this.entraineurService.create(coachData).subscribe({
        next: () => {
          this.ui.toast('Entraîneur créé avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la création', 'error'),
      });
    }
  }

  deleteCoach(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer cet entraîneur ?')) {
      this.entraineurService.delete(id).subscribe({
        next: () => {
          this.ui.toast('Entraîneur supprimé avec succès', 'success');
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la suppression', 'error'),
      });
    }
  }
}
