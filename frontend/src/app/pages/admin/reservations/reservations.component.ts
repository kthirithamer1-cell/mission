import { Component, inject } from '@angular/core';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
})
export class ReservationsComponent {
  private readonly ui = inject(AdminUiService);

  requestSlot(): void {
    this.ui.toast('Demande de créneau envoyée (aperçu)', 'info');
  }

  refresh(): void {
    this.ui.toast('Réservations actualisées', 'success');
  }
}
