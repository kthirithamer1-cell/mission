import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, AuthBrandComponent],
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, OnDestroy {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly ui = inject(AdminUiService);

  email = '';
  motDePasse = '';
  loading = signal(false);
  resendLoading = signal(false);
  showResend = signal(false);
  alertMessage = signal('');
  alertType = signal<'error' | 'success'>('error');

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();
  }

  ngOnDestroy(): void {
    document.body.classList.remove('dash-body');
  }

  onSubmit(): void {
    this.alertMessage.set('');
    const email = this.email.trim();
    if (!email || !this.motDePasse) {
      this.showError('Veuillez saisir votre email et mot de passe.');
      return;
    }

    this.loading.set(true);
    this.auth.login({ email, motDePasse: this.motDePasse }).subscribe({
      next: () => {
        this.alertType.set('success');
        this.alertMessage.set('Connexion réussie. Redirection…');
        setTimeout(() => this.router.navigate(['/dashboard']), 500);
      },
      error: (err) => {
        this.showError(AuthService.parseError(err));
        this.showResend.set(AuthService.isEmailNotVerified(err));
        this.loading.set(false);
      },
    });
  }

  resendVerification(): void {
    const email = this.email.trim();
    if (!email) {
      this.showError('Saisissez votre email pour renvoyer la vérification.');
      return;
    }

    this.resendLoading.set(true);
    this.auth.resendVerification(email).subscribe({
      next: (res) => {
        this.alertType.set('success');
        this.alertMessage.set(res.message);
        this.showResend.set(false);
        this.resendLoading.set(false);
      },
      error: (err) => {
        this.showError(AuthService.parseError(err));
        this.resendLoading.set(false);
      },
    });
  }

  private showError(message: string): void {
    this.alertType.set('error');
    this.alertMessage.set(message);
  }
}
