import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login.component').then((m) => m.LoginComponent),
    canActivate: [guestGuard],
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./pages/register/register.component').then(
        (m) => m.RegisterComponent
      ),
    canActivate: [guestGuard],
  },
  {
    path: 'forgot-password',
    loadComponent: () =>
      import('./pages/forgot-password/forgot-password.component').then(
        (m) => m.ForgotPasswordComponent
      ),
    canActivate: [guestGuard],
  },
  {
    path: 'reset-password',
    loadComponent: () =>
      import('./pages/reset-password/reset-password.component').then(
        (m) => m.ResetPasswordComponent
      ),
    canActivate: [guestGuard],
  },
  {
    path: 'verify-email',
    loadComponent: () =>
      import('./pages/verify-email/verify-email.component').then(
        (m) => m.VerifyEmailComponent
      ),
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./layout/admin-shell/admin-shell.component').then(
        (m) => m.AdminShellComponent
      ),
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/admin/admin-dashboard/admin-dashboard.component').then(
            (m) => m.AdminDashboardComponent
          ),
      },
      {
        path: 'athletes',
        loadComponent: () =>
          import('./pages/admin/athletes/athletes.component').then(
            (m) => m.AthletesComponent
          ),
      },
      {
        path: 'entraineurs',
        loadComponent: () =>
          import('./pages/admin/entraineurs/entraineurs.component').then(
            (m) => m.EntraineursComponent
          ),
      },
      {
        path: 'reservations',
        loadComponent: () =>
          import('./pages/admin/reservations/reservations.component').then(
            (m) => m.ReservationsComponent
          ),
      },
      {
        path: 'resultats',
        loadComponent: () =>
          import('./pages/admin/resultats/resultats.component').then(
            (m) => m.ResultatsComponent
          ),
      },
      {
        path: 'mon-profil',
        loadComponent: () =>
          import('./pages/coach/coach-profile/coach-profile.component').then(
            (m) => m.CoachProfileComponent
          ),
      },
      {
        path: 'mon-planning',
        loadComponent: () =>
          import('./pages/coach/coach-planning/coach-planning.component').then(
            (m) => m.CoachPlanningComponent
          ),
      },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
