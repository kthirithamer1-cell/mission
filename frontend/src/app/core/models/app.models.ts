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

export interface Competition {
  id?: number;
  nom: string;
  lieu: string;
  dateDebut: string;
  dateFin: string;
  type: 'CHAMPIONNAT' | 'COUPE' | 'MEETING' | 'CRITÉRIUM';
  statut: 'A_VENIR' | 'EN_COURS' | 'TERMINE';
  niveau: 'LOCAL' | 'REGIONAL' | 'NATIONAL' | 'INTERNATIONAL';
  organisateur: string;
  description?: string;
  specialite?: string;
  epreuve?: string;
  saison?: string;
  nombreEpreuves?: number;
}

export interface Resultat {
  id?: number;
  temps: string;
  classement: number;
  points?: number;
  record?: boolean;
  dateCompetition?: string;
  nageurId?: number;
  nageurNom?: string;
  epreuveId?: number;
  epreuveNom?: string;
  competitionNom?: string;
}

export interface SwimmerStats {
  nageurId: number;
  nageurNom: string;
  totalCourses: number;
  medaillesOr: number;
  medaillesArgent: number;
  medaillesBronze: number;
  averagePoints: number;
  recordsPersonnels: Resultat[];
  progressions: { [eventName: string]: Resultat[] };
}

export interface SwimmerSummary {
  nageurNom: string;
  totalMedaillesOr: number;
  totalPoints: number;
}

export interface ClubStats {
  totalCompetitions: number;
  totalResultats: number;
  totalRecords: number;
  repartitionStyles: { [style: string]: number };
  topSwimmers: SwimmerSummary[];
}

export interface RecordMondial {
  id?: number;
  epreuve: string;
  temps: string;
  nageur: string;
  nationalite: String;
  date: string;
  bassin: string;
  sexe: string;
}

export interface NageurInternational {
  id?: number;
  nom: string;
  nationalite: string;
  palmares: string;
  photoUrl: string;
  specialite: string;
  recordsPersonnels: string;
}

export interface CompetitionInternationale {
  id?: number;
  nom: string;
  lieu: string;
  dates: string;
  type: string;
  resultatsPrincipaux: string;
}

export interface LiveResultEvent {
  nageurNom: string;
  epreuveNom: string;
  temps: string;
  classement: number;
  points?: number;
  record?: boolean;
  competitionId?: number;
  timestamp: number;
}

export interface Epreuve {
  id?: number;
  distance: number;
  style: string;
  categorie: string;
  competitionId?: number;
}
