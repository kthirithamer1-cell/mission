import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';
import { UserRole } from '../../core/models/auth.models';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink, AuthBrandComponent],
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit, OnDestroy {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly ui = inject(AdminUiService);

  nom = '';
  prenom = '';
  email = '';
  motDePasse = '';
  confirmPassword = '';
  userType: UserRole = 'NAGEUR';

  loading = signal(false);
  alertMessage = signal('');
  alertType = signal<'error' | 'success'>('error');

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();
  }

  ngOnDestroy(): void {
    document.body.classList.remove('dash-body');
  }

  readonly roles: { value: UserRole; label: string }[] = [
    { value: 'NAGEUR', label: 'Nageur' },
    { value: 'ENTRAINEUR', label: 'Entraîneur' },
    { value: 'ADMIN', label: 'Administrateur' },
  ];

  onSubmit(): void {
    this.alertMessage.set('');

    const nom = this.nom.trim();
    const prenom = this.prenom.trim();
    const email = this.email.trim();

    if (!nom || !prenom || !email || !this.motDePasse) {
      this.showError('Veuillez remplir tous les champs obligatoires.');
      return;
    }
    if (this.motDePasse.length < 6) {
      this.showError('Le mot de passe doit contenir au moins 6 caractères.');
      return;
    }
    if (this.motDePasse !== this.confirmPassword) {
      this.showError('Les mots de passe ne correspondent pas.');
      return;
    }

    this.loading.set(true);
    this.auth
      .register({
        utilisateurDTO: { nom, prenom, email, motDePasse: this.motDePasse, role: this.userType },
        userType: this.userType,
      })
      .subscribe({
        next: (res) => {
          this.alertType.set('success');
          this.alertMessage.set(
            res.message ||
              'Compte créé. Vérifiez votre email pour activer votre compte.'
          );
          this.loading.set(false);
        },
        error: (err) => {
          this.showError(AuthService.parseError(err));
          this.loading.set(false);
        },
      });
  }

  private showError(message: string): void {
    this.alertType.set('error');
    this.alertMessage.set(message);
  }
}
