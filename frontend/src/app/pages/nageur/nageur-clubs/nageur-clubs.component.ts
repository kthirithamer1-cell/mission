import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Club, Entraineur } from '../../../core/models/app.models';
import { AuthService } from '../../../core/services/auth.service';
import { ClubService } from '../../../core/services/club.service';
import { NageurService } from '../../../core/services/nageur.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-nageur-clubs',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './nageur-clubs.component.html',
  styleUrls: ['./nageur-clubs.component.css'],
})
export class NageurClubsComponent implements OnInit {
  private readonly clubService = inject(ClubService);
  private readonly nageurService = inject(NageurService);
  private readonly auth = inject(AuthService);
  private readonly ui = inject(AdminUiService);

  readonly clubs = signal<Club[]>([]);
  readonly coaches = signal<Entraineur[]>([]);
  readonly selectedClubId = signal<number | null>(null);
  readonly loading = signal(true);
  readonly loadingCoaches = signal(false);
  readonly following = signal(false);
  readonly query = signal('');
  readonly currentUser = this.auth.currentUser;

  readonly selectedClub = computed(() => {
    const id = this.selectedClubId();
    return this.clubs().find((club) => club.id === id) ?? null;
  });

  readonly filteredClubs = computed(() => {
    const text = this.query().trim().toLowerCase();
    if (!text) return this.clubs();
    return this.clubs().filter((club) =>
      [club.nom, club.adresse].some((value) => value?.toLowerCase().includes(text))
    );
  });

  ngOnInit(): void {
    this.loadClubs();
  }

  loadClubs(): void {
    this.loading.set(true);
    this.clubService.getAll().subscribe({
      next: (clubs) => {
        this.clubs.set(clubs);
        const currentClubId = this.currentUser()?.clubId ?? clubs[0]?.id ?? null;
        this.selectedClubId.set(currentClubId);
        this.loading.set(false);
        if (currentClubId) this.loadCoaches(currentClubId);
      },
      error: () => {
        this.loading.set(false);
        this.ui.toast('Erreur lors du chargement des clubs', 'error');
      },
    });
  }

  selectClub(club: Club): void {
    if (!club.id || club.id === this.selectedClubId()) return;
    this.selectedClubId.set(club.id);
    this.loadCoaches(club.id);
  }

  followClub(club: Club): void {
    if (!club.id || club.id === this.currentUser()?.clubId) return;
    this.following.set(true);
    this.nageurService.updateMe({ clubId: club.id }).subscribe({
      next: (updated) => {
        this.following.set(false);
        const current = this.currentUser();
        this.auth.updateUserSession({
          ...(current ?? updated),
          clubId: updated.clubId,
          clubNom: updated.clubNom,
        });
        this.ui.toast(`Vous suivez maintenant ${updated.clubNom ?? club.nom}`, 'success');
      },
      error: () => {
        this.following.set(false);
        this.ui.toast('Impossible de suivre ce club', 'error');
      },
    });
  }

  isFollowing(club: Club): boolean {
    return !!club.id && club.id === this.currentUser()?.clubId;
  }

  initials(coach: Entraineur): string {
    return `${coach.prenom?.[0] ?? ''}${coach.nom?.[0] ?? ''}`.toUpperCase() || 'CO';
  }

  trackByClub(_index: number, club: Club): number | string {
    return club.id ?? club.nom;
  }

  private loadCoaches(clubId: number): void {
    this.loadingCoaches.set(true);
    this.clubService.getCoaches(clubId).subscribe({
      next: (coaches) => {
        this.coaches.set(coaches);
        this.loadingCoaches.set(false);
      },
      error: () => {
        this.coaches.set([]);
        this.loadingCoaches.set(false);
        this.ui.toast('Erreur lors du chargement des coaches', 'error');
      },
    });
  }
}
