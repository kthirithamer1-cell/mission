import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NageurService } from '../../../core/services/nageur.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Nageur } from '../../../core/models/app.models';

@Component({
  selector: 'app-nageur-profile',
  imports: [FormsModule],
  templateUrl: './nageur-profile.component.html',
})
export class NageurProfileComponent implements OnInit {
  private readonly nageurService = inject(NageurService);
  private readonly ui = inject(AdminUiService);

  profile = signal<Nageur | null>(null);
  saving = signal(false);

  form = signal<Partial<Nageur> & { newPassword?: string }>({
    nom: '',
    prenom: '',
    email: '',
    age: undefined,
    sexe: '',
    categorie: '',
    newPassword: '',
  });

  ngOnInit(): void {
    this.nageurService.getMe().subscribe({
      next: (data) => {
        this.profile.set(data);
        this.form.set({
          nom: data.nom,
          prenom: data.prenom,
          email: data.email,
          age: data.age,
          sexe: data.sexe ?? '',
          categorie: data.categorie ?? '',
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
    const payload: Partial<Nageur> = {
      nom: f.nom,
      prenom: f.prenom,
      age: f.age,
      sexe: f.sexe,
      categorie: f.categorie,
    };
    if (f.newPassword && f.newPassword.trim() !== '') {
      payload.motDePasse = f.newPassword;
    }
    this.nageurService.updateMe(payload).subscribe({
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

  sexeLabel(s: string | undefined): string {
    if (s === 'M') return 'Masculin';
    if (s === 'F') return 'Féminin';
    return s ?? '—';
  }
}
