import { Component, inject } from '@angular/core';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-athletes',
  templateUrl: './athletes.component.html',
})
export class AthletesComponent {
  private readonly ui = inject(AdminUiService);

  refresh(): void {
    this.ui.toast('Liste des athlètes actualisée', 'success');
  }

  addAthlete(): void {
    this.ui.toast('Ajout athlète — aperçu (API à brancher)', 'info');
  }
}
