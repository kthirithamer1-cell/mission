import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SeanceService } from '../../../core/services/seance.service';
import { PresenceService, PresenceDTO } from '../../../core/services/presence.service';
import { Seance } from '../../../core/models/app.models';

type StatutKey = 'PRESENT' | 'ABSENT' | 'JUSTIFIE';

@Component({
  selector: 'app-coach-presence',
  imports: [CommonModule, FormsModule],
  templateUrl: './coach-presence.component.html',
})
export class CoachPresenceComponent implements OnInit {
  private readonly seanceService = inject(SeanceService);
  private readonly presenceService = inject(PresenceService);

  seances = signal<Seance[]>([]);
  selectedSeance = signal<Seance | null>(null);
  presences = signal<PresenceDTO[]>([]);

  loadingSeances = signal(true);
  loadingPresences = signal(false);
  saving = signal(false);
  saved = signal(false);
  error = signal<string | null>(null);

  searchQuery = signal('');
  filterStatut = signal<StatutKey | 'ALL'>('ALL');

  readonly STATUTS: StatutKey[] = ['PRESENT', 'ABSENT', 'JUSTIFIE'];

  ngOnInit(): void {
    this.seanceService.getMesSeances().subscribe({
      next: (data) => {
        const sorted = [...data].sort((a, b) => (a.date ?? '') > (b.date ?? '') ? -1 : 1);
        this.seances.set(sorted);
        this.loadingSeances.set(false);
        if (sorted.length > 0) this.selectSeance(sorted[0]);
      },
      error: () => { this.error.set('Impossible de charger les séances.'); this.loadingSeances.set(false); },
    });
  }

  selectSeance(s: Seance): void {
    this.selectedSeance.set(s);
    this.presences.set([]);
    this.saved.set(false);
    this.loadingPresences.set(true);
    this.presenceService.getBySeance(s.id!).subscribe({
      next: (p) => { this.presences.set(p); this.loadingPresences.set(false); },
      error: () => { this.error.set('Impossible de charger les présences.'); this.loadingPresences.set(false); },
    });
  }

  setStatut(p: PresenceDTO, statut: StatutKey): void {
    const list = this.presences();
    const idx = list.findIndex(x => x.nageurId === p.nageurId);
    if (idx === -1) return;
    const updated = [...list];
    updated[idx] = { ...updated[idx], statut };
    this.presences.set(updated);
  }

  saveAll(): void {
    const seance = this.selectedSeance();
    if (!seance?.id) return;
    this.saving.set(true);
    this.presenceService.saveAll(seance.id, this.presences()).subscribe({
      next: (saved) => {
        this.presences.set(saved);
        this.saving.set(false);
        this.saved.set(true);
        setTimeout(() => this.saved.set(false), 2500);
      },
      error: () => { this.error.set('Erreur lors de la sauvegarde.'); this.saving.set(false); },
    });
  }

  filteredPresences = computed(() => {
    const q = this.searchQuery().toLowerCase();
    const f = this.filterStatut();
    return this.presences().filter(p => {
      const matchQ = !q || `${p.nageurPrenom} ${p.nageurNom}`.toLowerCase().includes(q);
      const matchF = f === 'ALL' || p.statut === f;
      return matchQ && matchF;
    });
  });

  stats = computed(() => {
    const list = this.presences();
    const present = list.filter(p => p.statut === 'PRESENT').length;
    const absent = list.filter(p => p.statut === 'ABSENT').length;
    const justifie = list.filter(p => p.statut === 'JUSTIFIE').length;
    const total = list.length;
    const tauxPresence = total > 0 ? Math.round((present / total) * 100) : 0;
    return { present, absent, justifie, total, tauxPresence };
  });

  statutLabel(s: StatutKey): string {
    return s === 'PRESENT' ? 'Présent' : s === 'ABSENT' ? 'Absent' : 'Justifié';
  }

  statutClass(s: StatutKey): string {
    return s === 'PRESENT' ? 'is-present' : s === 'ABSENT' ? 'is-absent' : 'is-justifie';
  }

  formatDate(d: string | undefined): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('fr-FR', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' });
  }

  markAll(statut: StatutKey): void {
    this.presences.set(this.presences().map(p => ({ ...p, statut })));
  }
}
