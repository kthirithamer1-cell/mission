import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompetitionService } from '../../../core/services/competition.service';
import { ResultatService } from '../../../core/services/resultat.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Competition, Resultat } from '../../../core/models/app.models';

@Component({
  selector: 'app-resultats',
  imports: [CommonModule, FormsModule],
  templateUrl: './resultats.component.html',
})
export class ResultatsComponent implements OnInit {
  private readonly competitionService = inject(CompetitionService);
  private readonly resultatService = inject(ResultatService);
  private readonly ui = inject(AdminUiService);

  readonly competitions = signal<Competition[]>([]);
  readonly activeYear = signal<string>('Toutes');
  readonly searchQuery = signal<string>('');

  // Selected competition and its results
  readonly selectedCompetition = signal<Competition | null>(null);
  readonly results = signal<Resultat[]>([]);
  readonly searchResultQuery = signal<string>('');

  // Grouped and filtered competitions
  readonly filteredCompetitions = computed(() => {
    let list = this.competitions();
    const query = this.searchQuery().toLowerCase().trim();
    const year = this.activeYear();

    if (query) {
      list = list.filter(c => c.nom.toLowerCase().includes(query) || (c.lieu ?? '').toLowerCase().includes(query));
    }

    if (year !== 'Toutes') {
      list = list.filter(c => c.dateDebut && c.dateDebut.startsWith(year));
    }

    return list;
  });

  // Grouped by season map
  readonly competitionsBySeason = computed(() => {
    const map = new Map<string, Competition[]>();
    for (const c of this.filteredCompetitions()) {
      const season = c.saison || 'Autre';
      if (!map.has(season)) {
        map.set(season, []);
      }
      map.get(season)!.push(c);
    }
    return Array.from(map.entries()).sort((a, b) => b[0].localeCompare(a[0]));
  });

  readonly filteredResults = computed(() => {
    let list = this.results();
    const query = this.searchResultQuery().toLowerCase().trim();

    if (query) {
      list = list.filter(r => 
        (r.nageurNom && r.nageurNom.toLowerCase().includes(query)) ||
        (r.epreuveNom && r.epreuveNom.toLowerCase().includes(query))
      );
    }

    return list.sort((a, b) => (a.classement || 99) - (b.classement || 99));
  });

  ngOnInit(): void {
    this.loadCompetitions();
  }

  loadCompetitions(): void {
    this.competitionService.getAll({ statut: 'TERMINE' }).subscribe({
      next: (data) => this.competitions.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des compétitions', 'error'),
    });
  }

  setYear(year: string): void {
    this.activeYear.set(year);
  }

  viewResults(competition: Competition): void {
    this.selectedCompetition.set(competition);
    this.results.set([]);
    if (competition.id) {
      this.resultatService.getByCompetition(competition.id).subscribe({
        next: (data) => {
          this.results.set(data);
          this.ui.toast(`Résultats de "${competition.nom}" chargés`, 'success');
        },
        error: () => this.ui.toast('Erreur lors du chargement des résultats', 'error'),
      });
    }
  }

  closeResults(): void {
    this.selectedCompetition.set(null);
    this.results.set([]);
  }
}
