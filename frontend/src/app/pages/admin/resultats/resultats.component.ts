import { Component, inject, signal } from '@angular/core';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-resultats',
  templateUrl: './resultats.component.html',
})
export class ResultatsComponent {
  private readonly ui = inject(AdminUiService);
  readonly activeYear = signal('Toutes');

  setYear(year: string): void {
    this.activeYear.set(year);
  }

  viewResults(title: string): void {
    this.ui.toast(`Résultats : ${title}`, 'info');
  }
}
