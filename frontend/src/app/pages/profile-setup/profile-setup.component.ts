import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';
import { Nageur } from '../../core/models/app.models';
import { ProfileSetupRequest } from '../../core/models/auth.models';

@Component({
  selector: 'app-profile-setup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AuthBrandComponent],
  templateUrl: './profile-setup.component.html',
})
export class ProfileSetupComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly ui = inject(AdminUiService);
  private readonly router = inject(Router);

  loading = signal(true);
  saving = signal(false);
  alertMessage = signal('');
  alertType = signal<'error' | 'success'>('error');
  token = '';

  user = signal<Nageur | null>(null);

  form = signal<{
    nom: string;
    prenom: string;
    age?: number;
    sexe: string;
    categorie: string;
    groupes: string;
  }>({
    nom: '',
    prenom: '',
    age: undefined,
    sexe: '',
    categorie: '',
    groupes: '',
  });

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();

    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.loading.set(false);
      this.showError('Lien de configuration du profil invalide.');
      return;
    }

    this.auth.getProfileSetup(this.token).subscribe({
      next: (data) => {
        this.user.set(data as Nageur);
        this.form.set({
          nom: data.nom,
          prenom: data.prenom,
          age: (data as Nageur).age,
          sexe: (data as Nageur).sexe ?? '',
          categorie: (data as Nageur).categorie ?? '',
          groupes: '',
        });
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showError('Session de configuration expirée ou invalide.');
      },
    });
  }

  isNageur(): boolean {
    const u = this.user();
    return u != null && u.role === 'NAGEUR';
  }

  save(): void {
    const f = this.form();
    if (!f.nom || !f.prenom) {
      this.showError('Nom et prénom sont obligatoires');
      return;
    }

    this.saving.set(true);
    const payload: ProfileSetupRequest = {
      token: this.token,
      nom: f.nom,
      prenom: f.prenom,
    };

    if (this.isNageur()) {
      payload.age = f.age;
      payload.sexe = f.sexe;
      payload.categorie = f.categorie;
    } else {
      payload.groupes = f.groupes;
    }

    this.auth.completeProfileSetup(payload).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: () => {
        this.saving.set(false);
        this.showError('Erreur lors de la sauvegarde du profil');
      },
    });
  }

  private showError(message: string): void {
    this.alertType.set('error');
    this.alertMessage.set(message);
  }
}
