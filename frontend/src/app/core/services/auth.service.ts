import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ForgotPasswordRequest,
  LoginRequest,
  LoginResponse,
  MessageResponse,
  ProfileSetupRequest,
  RegisterRequest,
  RegisterResponse,
  ResetPasswordRequest,
  Utilisateur,
  VerifyEmailResponse,
} from '../models/auth.models';

const TOKEN_KEY = 'token';
const USER_KEY = 'user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  readonly currentUser = signal<Utilisateur | null>(this.loadUser());
  readonly isAuthenticated = signal(!!this.getToken());

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(tap((res) => this.setSession(res.token, res.utilisateur)));
  }

  register(payload: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(
      `${environment.apiUrl}/auth/register`,
      payload
    );
  }

  verifyEmail(token: string): Observable<MessageResponse> {
    return this.http.get<VerifyEmailResponse>(
      `${environment.apiUrl}/auth/verify-email`,
      { params: { token } }
    );
  }

  getProfileSetup(token: string): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(
      `${environment.apiUrl}/auth/profile-setup`,
      { params: { token } }
    );
  }

  completeProfileSetup(data: ProfileSetupRequest): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(
      `${environment.apiUrl}/auth/profile-setup`,
      data
    );
  }

  resendVerification(email: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `${environment.apiUrl}/auth/resend-verification`,
      { email }
    );
  }

  forgotPassword(payload: ForgotPasswordRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `${environment.apiUrl}/auth/forgot-password`,
      payload
    );
  }

  resetPassword(payload: ResetPasswordRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(
      `${environment.apiUrl}/auth/reset-password`,
      payload
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private setSession(token: string, user: Utilisateur): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUser.set(user);
    this.isAuthenticated.set(true);
  }

  updateUserSession(user: Utilisateur): void {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUser.set(user);
  }

  dashboardRouteForRole(role?: string | null): string {
    if (role === 'ENTRAINEUR') return '/coach-dashboard';
    if (role === 'NAGEUR') return '/nageur-dashboard';
    return '/dashboard';
  }

  private loadUser(): Utilisateur | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as Utilisateur;
    } catch {
      return null;
    }
  }

  static parseError(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (typeof error.error === 'string' && error.error) {
        return error.error;
      }
      if (error.error?.message) {
        return error.error.message;
      }
    }
    return 'Une erreur est survenue';
  }

  static isEmailNotVerified(error: unknown): boolean {
    return (
      error instanceof HttpErrorResponse &&
      error.status === 403 &&
      typeof error.error?.message === 'string'
    );
  }
}
