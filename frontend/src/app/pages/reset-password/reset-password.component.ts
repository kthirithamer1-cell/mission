import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';

@Component({
  selector: 'app-reset-password',
  imports: [FormsModule, RouterLink, AuthBrandComponent],
  templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  private readonly ui = inject(AdminUiService);

  token = '';
  newPassword = '';
  confirmPassword = '';
  loading = signal(false);
  alertMessage = signal('');
  alertType = signal<'error' | 'success'>('error');

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();

    const routeToken = this.route.snapshot.queryParamMap.get('token');
    if (routeToken) {
      this.token = routeToken;
    } else {
      this.showError('Lien de réinitialisation invalide ou manquant.');
    }
  }

  ngOnDestroy(): void {
    document.body.classList.remove('dash-body');
  }

  onSubmit(): void {
    this.alertMessage.set('');
    if (!this.token) {
      this.showError('Lien de réinitialisation invalide.');
      return;
    }
    if (!this.newPassword) {
      this.showError('Veuillez saisir votre nouveau mot de passe.');
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.showError('Les mots de passe ne correspondent pas.');
      return;
    }

    this.loading.set(true);
    this.auth.resetPassword({ token: this.token, newPassword: this.newPassword }).subscribe({
      next: (res) => {
        this.alertType.set('success');
        this.alertMessage.set(res.message);
        this.loading.set(false);
        setTimeout(() => this.router.navigate(['/login']), 2000);
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
