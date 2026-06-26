export type UserRole = 'ADMIN' | 'NAGEUR' | 'ENTRAINEUR';

export interface Utilisateur {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  motDePasse?: string;
  role: UserRole | string;
  clubId?: number;
  clubNom?: string;
  photoUrl?: string;
}

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface LoginResponse {
  token: string;
  utilisateur: Utilisateur;
}

export interface RegisterRequest {
  utilisateurDTO: Utilisateur;
  userType: UserRole;
}

export interface RegisterResponse {
  message: string;
  utilisateur: Utilisateur;
}

export interface MessageResponse {
  message: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}
