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

export interface Epreuve {
  id?: number;
  distance: number;
  style: string;
  categorie: string;
  competitionId?: number;
}

export interface ResultDetailDTO {
  id: number;
  temps: string;
  classement: number;
  epreuveStyle: string;
  epreuveDistance: number;
  competitionNom: string;
}

export interface SwimmerRecordDTO {
  epreuveLabel: string;
  temps: string;
  date: string;
}

export interface CoachDashboardStats {
  seancesThisWeek: number;
  nageursCount: number;
  presencesCount: number;
  nextSeance?: Seance;
  weekSessions: Seance[];
  groupSwimmers: Nageur[];
  attendanceRateBySession: { [key: string]: number };
}

export interface NageurDashboardStats {
  resultatsCount: number;
  presencesCount: number;
  nextSeance?: Seance;
  categorie: string;
  recentResults: ResultDetailDTO[];
  upcomingSessions: Seance[];
  personalRecords: SwimmerRecordDTO[];
  progressionData: { [key: string]: number[] };
}

export interface CalendarEvent {
  id?: number;
  titre: string;
  date: string;
  heureDebut: string;
  heureFin: string;
  description?: string;
  clubId?: number;
  clubNom?: string;
  entraineurId?: number;
  entraineurNom?: string;
  reservationId?: number;
  piscineNom?: string;
  couloirsLabel?: string;
  studentsTotal?: number;
  presentCount?: number;
  absentCount?: number;
  justifieCount?: number;
  attendanceRate?: number;
  studentNames?: string[];
  myPresenceStatus?: string;
  relevantToMe?: boolean;
  categorie?: string;
}

export interface CalendarResponse {
  from: string;
  to: string;
  totalEvents: number;
  upcomingCount: number;
  totalHours: number;
  events: CalendarEvent[];
}

export interface Resultat {
  id?: number;
  temps: string;
  classement?: number;
  points?: number;
  record?: boolean;
  dateCompetition?: string;
  nageurId?: number;
  nageurNom?: string;
  epreuveId?: number;
  epreuveNom?: string;
  competitionNom?: string;
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
  repartitionStyles: { [key: string]: number };
  topSwimmers: SwimmerSummary[];
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
  progressions: { [key: string]: Resultat[] };
}

export interface Competition {
  id?: number;
  nom: string;
  lieu?: string;
  dateDebut?: string;
  dateFin?: string;
  type?: string;
  statut?: string;
  niveau?: string;
  organisateur?: string;
  description?: string;
  specialite?: string;
  epreuve?: string;
  saison?: string;
}

export interface ParticipationDTO {
  id?: number;
  nageurId?: number;
  nageurNom?: string;
  nageurPrenom?: string;
  nageurEmail?: string;
  competitionId?: number;
  competitionNom?: string;
  clubId?: number;
  clubNom?: string;
  dateInscription?: string;
  statut?: string;
  dateCreation?: string;
  dateModification?: string;
}

export interface CompetitionInternationale {
  id?: number;
  nom: string;
  lieu?: string;
  dates?: string;
  type?: string;
  resultatsPrincipaux?: string;
}

export interface NageurInternational {
  id?: number;
  nom: string;
  prenom: string;
  pays?: string;
  club?: string;
  nationalite?: string;
  specialite?: string;
  palmares?: string;
  recordsPersonnels?: string;
  photoUrl?: string;
}

export interface RecordMondial {
  id?: number;
  epreuve?: string;
  temps?: string;
  nageur?: string;
  nageurNom?: string;
  pays?: string;
  date?: string;
  nationalite?: string;
}

export interface LiveResultEvent {
  id?: number;
  competitionId?: number;
  epreuveId?: number;
  epreuveNom?: string;
  nageurId?: number;
  nageurNom?: string;
  temps?: string;
  points?: number;
  classement?: number;
  dateCompetition?: string;
  record?: boolean;
  timestamp?: number;
}
