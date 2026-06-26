import { Component, HostListener, computed, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AdminUiService } from '../../core/services/admin-ui.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-admin-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgClass],
  templateUrl: './admin-shell.component.html',
})
export class AdminShellComponent implements OnInit, OnDestroy {
  private readonly ui = inject(AdminUiService);
  private readonly auth = inject(AuthService);

  readonly profileMenuOpen = signal(false);

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

  readonly currentUser = this.auth.currentUser;

  isCoach(): boolean {
    return this.currentUser()?.role === 'ENTRAINEUR';
  }

  isNageur(): boolean {
    return this.currentUser()?.role === 'NAGEUR';
  }

  get roleLabel(): string {
    const r = this.currentUser()?.role;
    if (r === 'ADMIN') return 'Admin';
    if (r === 'ENTRAINEUR') return 'Coach';
    if (r === 'NAGEUR') return 'Nageur';
    return '';
  }

  get roleClass(): string {
    const r = this.currentUser()?.role;
    if (r === 'ADMIN') return 'role-admin';
    if (r === 'ENTRAINEUR') return 'role-coach';
    if (r === 'NAGEUR') return 'role-nageur';
    return '';
  }

  readonly profileRoute = computed((): string | null => {
    const r = this.currentUser()?.role;
    if (r === 'ENTRAINEUR') return '/mon-profil';
    if (r === 'NAGEUR') return '/nageur-profil';
    return null;
  });

  toggleProfileMenu(event: Event): void {
    event.stopPropagation();
    this.profileMenuOpen.update((open) => !open);
  }

  closeProfileMenu(): void {
    this.profileMenuOpen.set(false);
  }

  logout(): void {
    this.closeProfileMenu();
    this.auth.logout();
  }

  @HostListener('document:click')
  onDocumentClick(): void {
    this.closeProfileMenu();
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    this.closeProfileMenu();
  }
}
