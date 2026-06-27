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

export interface VerifyEmailResponse {
  message: string;
  utilisateur: Utilisateur;
  profileSetupToken: string;
}

export interface ProfileSetupRequest {
  token: string;
  nom?: string;
  prenom?: string;
  age?: number;
  sexe?: string;
  categorie?: string;
  clubId?: number;
  groupes?: string;
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
