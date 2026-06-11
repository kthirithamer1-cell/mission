import { Utilisateur } from './auth.models';

export interface Nageur extends Utilisateur {
  age?: number;
  sexe?: string;
  categorie?: string;
  clubId?: number;
  clubNom?: string;
  licenceId?: number;
}

export interface Entraineur extends Utilisateur {
  clubId?: number;
  clubNom?: string;
  groupes?: string;
}

export interface Reservation {
  id?: number;
  piscineId: number;
  piscineNom?: string;
  clubId?: number;
  clubNom?: string;
  date: string;
  heureDebut: string;
  heureFin: string;
  couloirDebut: number;
  couloirFin: number;
  statut?: string; // 'CONFIRME' | 'EN_ATTENTE' | 'ANNULE'
}

export interface Seance {
  id?: number;
  clubId?: number;
  clubNom?: string;
  reservationId?: number;
  entraineurId: number;
  entraineurNom?: string;
  titre: string;
  date: string;
  heureDebut: string;
  heureFin: string;
  description?: string;
}

export interface Club {
  id?: number;
  nom: string;
  adresse?: string;
  dateAffiliation?: string;
}

export interface Piscine {
  id?: number;
  nom: string;
  adresse?: string;
  ville?: string;
  nombreCouloirs: number;
  longueurMetres: number;
  active?: boolean;
}

export interface RecordDTO {
  epreuve: string;
  temps: string;
  nageurNom: string;
  categorie: string;
}

export interface DashboardStats {
  nageursCount: number;
  entraineursCount: number;
  couloirsReserves: number;
  creneauxAVenir: number;
  reservationsEnAttente: number;
  piscinesCount: number;
  clubsCount: number;
  repartitionCategories: { [key: string]: number };
  evolutionParCategorie: { [key: string]: number[] };
  records: RecordDTO[];
}
