import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';

@Component({
  selector: 'app-forgot-password',
  imports: [FormsModule, RouterLink, AuthBrandComponent],
  templateUrl: './forgot-password.component.html',
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {
  private readonly auth = inject(AuthService);
  private readonly ui = inject(AdminUiService);

  email = '';
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

  onSubmit(): void {
    this.alertMessage.set('');
    const email = this.email.trim();
    if (!email) {
      this.showError('Veuillez saisir votre adresse email.');
      return;
    }

    this.loading.set(true);
    this.auth.forgotPassword({ email }).subscribe({
      next: (res) => {
        this.alertType.set('success');
        this.alertMessage.set(res.message);
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
