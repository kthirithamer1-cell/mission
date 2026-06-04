import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NageurService } from '../../../core/services/nageur.service';
import { ClubService } from '../../../core/services/club.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Nageur, Club } from '../../../core/models/app.models';

@Component({
  selector: 'app-athletes',
  imports: [RouterLink, FormsModule],
  templateUrl: './athletes.component.html',
})
export class AthletesComponent implements OnInit {
  private readonly nageurService = inject(NageurService);
  private readonly clubService = inject(ClubService);
  private readonly ui = inject(AdminUiService);

  readonly athletes = signal<Nageur[]>([]);
  readonly clubs = signal<Club[]>([]);

  // Modal / Form state
  showModal = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  // Form fields
  formAthlete = signal<Partial<Nageur>>({
    nom: '',
    prenom: '',
    email: '',
    motDePasse: '',
    age: undefined,
    sexe: 'M',
    categorie: 'CADETS',
    clubId: undefined,
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.nageurService.getAll().subscribe({
      next: (data) => this.athletes.set(data),
      error: () => this.ui.toast('Erreur chargement athlètes', 'error'),
    });

    this.clubService.getAll().subscribe({
      next: (data) => {
        this.clubs.set(data);
        if (data.length > 0 && !this.formAthlete().clubId) {
          this.formAthlete.update(f => ({ ...f, clubId: data[0].id }));
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
    this.formAthlete.set({
      nom: '',
      prenom: '',
      email: '',
      motDePasse: 'nageur123',
      age: 18,
      sexe: 'M',
      categorie: 'CADETS',
      clubId: this.clubs().length > 0 ? this.clubs()[0].id : undefined,
    });
    this.showModal.set(true);
  }

  openEditModal(athlete: Nageur): void {
    this.isEditMode.set(true);
    this.formAthlete.set({
      id: athlete.id,
      nom: athlete.nom,
      prenom: athlete.prenom,
      email: athlete.email,
      age: athlete.age,
      sexe: athlete.sexe || 'M',
      categorie: athlete.categorie || 'CADETS',
      clubId: athlete.clubId,
      role: athlete.role || 'NAGEUR',
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveAthlete(): void {
    const athleteData = this.formAthlete() as Nageur;
    if (!athleteData.nom || !athleteData.prenom || !athleteData.email) {
      this.ui.toast('Veuillez remplir les champs obligatoires', 'error');
      return;
    }

    if (this.isEditMode()) {
      this.nageurService.update(athleteData.id!, athleteData).subscribe({
        next: () => {
          this.ui.toast('Athlète mis à jour avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la mise à jour', 'error'),
      });
    } else {
      this.nageurService.create(athleteData).subscribe({
        next: () => {
          this.ui.toast('Athlète créé avec succès', 'success');
          this.closeModal();
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la création', 'error'),
      });
    }
  }

  deleteAthlete(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer cet athlète ?')) {
      this.nageurService.delete(id).subscribe({
        next: () => {
          this.ui.toast('Athlète supprimé avec succès', 'success');
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de la suppression', 'error'),
      });
    }
  }
}
