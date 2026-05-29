import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';
import { AdminUiService } from '../../core/services/admin-ui.service';

@Component({
  selector: 'app-verify-email',
  imports: [RouterLink, AuthBrandComponent],
  templateUrl: './verify-email.component.html',
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly auth = inject(AuthService);
  private readonly ui = inject(AdminUiService);

  loading = signal(true);
  alertMessage = signal('');
  alertType = signal<'error' | 'success'>('error');

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();

    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.loading.set(false);
      this.showError('Lien de vérification invalide.');
      return;
    }

    this.auth.verifyEmail(token).subscribe({
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

  ngOnDestroy(): void {
    document.body.classList.remove('dash-body');
  }

  private showError(message: string): void {
    this.alertType.set('error');
    this.alertMessage.set(message);
  }
}
