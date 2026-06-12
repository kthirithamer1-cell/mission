import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EntraineurService } from '../../../core/services/entraineur.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Entraineur } from '../../../core/models/app.models';

@Component({
  selector: 'app-coach-profile',
  imports: [FormsModule],
  templateUrl: './coach-profile.component.html',
})
export class CoachProfileComponent implements OnInit {
  private readonly entraineurService = inject(EntraineurService);
  private readonly ui = inject(AdminUiService);

  profile = signal<Entraineur | null>(null);
  saving = signal(false);

  form = signal<Partial<Entraineur> & { newPassword?: string }>({
    nom: '',
    prenom: '',
    email: '',
    groupes: '',
    newPassword: '',
  });

  ngOnInit(): void {
    this.entraineurService.getMe().subscribe({
      next: (data) => {
        this.profile.set(data);
        this.form.set({
          nom: data.nom,
          prenom: data.prenom,
          email: data.email,
          groupes: data.groupes ?? '',
          newPassword: '',
        });
      },
      error: () => this.ui.toast('Erreur chargement du profil', 'error'),
    });
  }

  save(): void {
    const f = this.form();
    if (!f.nom || !f.prenom) {
      this.ui.toast('Nom et prénom sont obligatoires', 'error');
      return;
    }
    this.saving.set(true);
    const payload: Partial<Entraineur> = {
      nom: f.nom,
      prenom: f.prenom,
      groupes: f.groupes,
    };
    if (f.newPassword && f.newPassword.trim() !== '') {
      payload.motDePasse = f.newPassword;
    }
    this.entraineurService.updateMe(payload).subscribe({
      next: (updated) => {
        this.profile.set(updated);
        this.form.update(v => ({ ...v, newPassword: '' }));
        this.saving.set(false);
        this.ui.toast('Profil mis à jour avec succès', 'success');
      },
      error: () => {
        this.saving.set(false);
        this.ui.toast('Erreur lors de la mise à jour', 'error');
      },
    });
  }
}
