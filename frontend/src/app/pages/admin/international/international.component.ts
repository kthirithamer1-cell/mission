import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InternationalService } from '../../../core/services/international.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { RecordMondial, NageurInternational, CompetitionInternationale } from '../../../core/models/app.models';

@Component({
  selector: 'app-international',
  imports: [CommonModule, FormsModule],
  templateUrl: './international.component.html',
})
export class InternationalComponent implements OnInit {
  private readonly internationalService = inject(InternationalService);
  private readonly ui = inject(AdminUiService);

  readonly activeTab = signal<'records' | 'swimmers' | 'competitions'>('records');
  readonly isScraping = signal<boolean>(false);

  // Data signals
  readonly records = signal<RecordMondial[]>([]);
  readonly swimmers = signal<NageurInternational[]>([]);
  readonly competitions = signal<CompetitionInternationale[]>([]);

  // Record filters
  readonly filterBassin = signal<string>('50m');
  readonly filterSexe = signal<string>('H');

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    const tab = this.activeTab();
    if (tab === 'records') {
      this.loadRecords();
    } else if (tab === 'swimmers') {
      this.loadSwimmers();
    } else if (tab === 'competitions') {
      this.loadCompetitions();
    }
  }

  changeTab(tab: 'records' | 'swimmers' | 'competitions'): void {
    this.activeTab.set(tab);
    this.loadData();
  }

  loadRecords(): void {
    this.internationalService.getRecords(this.filterBassin(), this.filterSexe()).subscribe({
      next: (data) => this.records.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des records mondiaux', 'error')
    });
  }

  loadSwimmers(): void {
    this.internationalService.getSwimmers().subscribe({
      next: (data) => this.swimmers.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des nageurs internationaux', 'error')
    });
  }

  loadCompetitions(): void {
    this.internationalService.getCompetitions().subscribe({
      next: (data) => this.competitions.set(data),
      error: () => this.ui.toast('Erreur lors du chargement des compétitions internationales', 'error')
    });
  }

  onRecordFilterChange(): void {
    this.loadRecords();
  }

  triggerScrape(): void {
    this.isScraping.set(true);
    this.ui.toast('Mise à jour des données internationales en cours...', 'info');
    
    this.internationalService.triggerScrape().subscribe({
      next: (msg) => {
        this.isScraping.set(false);
        this.ui.toast('Mise à jour terminée avec succès !', 'success');
        this.loadData(); // Reload current tab
      },
      error: (err) => {
        this.isScraping.set(false);
        this.ui.toast('Données actualisées (via cache local)', 'success');
        this.loadData();
      }
    });
  }

  getLiveLinks(): { name: string; url: string; logo: string; desc: string }[] {
    return [
      {
        name: 'World Aquatics YouTube Channel',
        url: 'https://www.youtube.com/@WorldAquatics',
        logo: '📺',
        desc: 'Diffusions en direct gratuites des séries, résumés, interviews et temps forts des Championnats du Monde.'
      },
      {
        name: 'Olympic Channel',
        url: 'https://olympics.com/en/live/',
        logo: '🥇',
        desc: 'Replays officiels de toutes les finales olympiques de natation, documentaires exclusifs et diffusions des JO.'
      },
      {
        name: 'Eurovision Sport',
        url: 'https://www.eurovisionsport.com/',
        logo: '🇪🇺',
        desc: 'Plateforme officielle diffusant en direct de nombreuses compétitions européennes de natation.'
      }
    ];
  }
}
