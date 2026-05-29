import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter, Routes } from '@angular/router';
import { AuthService } from './auth.service';

const testRoutes: Routes = [{ path: 'login', children: [] }];

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService, provideRouter(testRoutes)],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should login and store session', () => {
    const mockResponse = {
      token: 'test-token',
      utilisateur: {
        id: 1,
        nom: 'Doe',
        prenom: 'John',
        email: 'john@example.com',
        role: 'NAGEUR',
      },
    };

    service.login({ email: 'john@example.com', motDePasse: 'secret' }).subscribe();

    const req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);

    expect(service.getToken()).toBe('test-token');
    expect(service.isAuthenticated()).toBe(true);
    expect(service.currentUser()?.email).toBe('john@example.com');
  });

  it('should register a user', () => {
    service
      .register({
        utilisateurDTO: {
          nom: 'Doe',
          prenom: 'Jane',
          email: 'jane@example.com',
          motDePasse: 'secret12',
          role: 'NAGEUR',
        },
        userType: 'NAGEUR',
      })
      .subscribe();

    const req = httpMock.expectOne('/api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush({
      message: 'Compte créé. Vérifiez votre email pour activer votre compte.',
      utilisateur: { id: 2, nom: 'Doe', prenom: 'Jane', email: 'jane@example.com', role: 'NAGEUR' },
    });
  });

  it('should logout and clear session', () => {
    localStorage.setItem('token', 'x');
    localStorage.setItem('user', JSON.stringify({ email: 'a@b.com' }));
    service.logout();
    expect(service.getToken()).toBeNull();
    expect(service.isAuthenticated()).toBe(false);
  });
});
