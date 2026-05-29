import { Component, inject } from '@angular/core';
import { AuthBrandComponent } from '../../shared/auth-brand/auth-brand.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [AuthBrandComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  private readonly auth = inject(AuthService);

  readonly user = this.auth.currentUser;

  logout(): void {
    this.auth.logout();
  }
}
