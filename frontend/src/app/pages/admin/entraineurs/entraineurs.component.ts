import { Component, inject } from '@angular/core';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-entraineurs',
  templateUrl: './entraineurs.component.html',
})
export class EntraineursComponent {
  private readonly ui = inject(AdminUiService);

  refresh(): void {
    this.ui.toast('Liste actualisée', 'success');
  }

  addCoach(): void {
    this.ui.toast('Ajout entraîneur — aperçu (API à brancher)', 'info');
  }
}
