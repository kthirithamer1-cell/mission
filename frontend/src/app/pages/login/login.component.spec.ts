import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { LoginComponent } from './login.component';
import { AuthService } from '../../core/services/auth.service';

describe('LoginComponent', () => {
  const authSpy = {
    login: vi.fn(),
    isAuthenticated: vi.fn(() => false),
  };

  beforeEach(async () => {
    authSpy.login.mockReset();

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authSpy },
      ],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should show error when fields are empty', () => {
    const fixture = TestBed.createComponent(LoginComponent);
    fixture.componentInstance.onSubmit();
    expect(fixture.componentInstance.alertMessage().toLowerCase()).toMatch(/email/);
    expect(authSpy.login).not.toHaveBeenCalled();
  });

  it('should call auth.login on valid submit', () => {
    authSpy.login.mockReturnValue(
      of({
        token: 't',
        utilisateur: {
          nom: 'D',
          prenom: 'J',
          email: 'j@e.com',
          role: 'NAGEUR',
        },
      })
    );

    const fixture = TestBed.createComponent(LoginComponent);
    const comp = fixture.componentInstance;
    comp.email = 'j@e.com';
    comp.motDePasse = 'pass123';
    comp.onSubmit();

    expect(authSpy.login).toHaveBeenCalledWith({
      email: 'j@e.com',
      motDePasse: 'pass123',
    });
  });

  it('should show error on login failure', () => {
    authSpy.login.mockReturnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            error: 'Invalid credentials',
            status: 401,
          })
      )
    );

    const fixture = TestBed.createComponent(LoginComponent);
    const comp = fixture.componentInstance;
    comp.email = 'j@e.com';
    comp.motDePasse = 'wrong';
    comp.onSubmit();

    expect(comp.alertMessage()).toBe('Invalid credentials');
    // French or English error from API
    expect(comp.alertMessage().length).toBeGreaterThan(0);
  });
});
