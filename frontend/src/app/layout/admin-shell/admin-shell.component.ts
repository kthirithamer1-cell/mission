import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AdminUiService } from '../../core/services/admin-ui.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-admin-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin-shell.component.html',
})
export class AdminShellComponent implements OnInit, OnDestroy {
  private readonly ui = inject(AdminUiService);
  private readonly auth = inject(AuthService);

  ngOnInit(): void {
    document.body.classList.add('dash-body');
    this.ui.loadTheme();
  }

  ngOnDestroy(): void {
    document.body.classList.remove('dash-body');
    document.body.removeAttribute('data-theme');
  }

  toggleTheme(): void {
    this.ui.toggleTheme();
  }

  logout(): void {
    this.auth.logout();
  }
}
