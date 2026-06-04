import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ReservationService } from '../../../core/services/reservation.service';
import { PiscineService } from '../../../core/services/piscine.service';
import { ClubService } from '../../../core/services/club.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';
import { Reservation, Piscine, Club } from '../../../core/models/app.models';

@Component({
  selector: 'app-reservations',
  imports: [RouterLink, FormsModule],
  templateUrl: './reservations.component.html',
})
export class ReservationsComponent implements OnInit {
  private readonly reservationService = inject(ReservationService);
  private readonly piscineService = inject(PiscineService);
  private readonly clubService = inject(ClubService);
  private readonly ui = inject(AdminUiService);

  readonly reservations = signal<Reservation[]>([]);
  readonly piscines = signal<Piscine[]>([]);
  readonly clubs = signal<Club[]>([]);

  // Modal / Form state
  showModal = signal<boolean>(false);

  // Form fields
  formReservation = signal<Partial<Reservation>>({
    piscineId: undefined,
    clubId: undefined,
    date: '',
    heureDebut: '',
    heureFin: '',
    couloirDebut: 1,
    couloirFin: 2,
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.reservationService.getAll().subscribe({
      next: (data) => this.reservations.set(data),
      error: () => this.ui.toast('Erreur chargement réservations', 'error'),
    });

    this.piscineService.getAll().subscribe({
      next: (data) => {
        this.piscines.set(data);
        if (data.length > 0 && !this.formReservation().piscineId) {
          this.formReservation.update(f => ({ ...f, piscineId: data[0].id }));
        }
      },
      error: () => this.ui.toast('Erreur chargement piscines', 'error'),
    });

    this.clubService.getAll().subscribe({
      next: (data) => {
        this.clubs.set(data);
        if (data.length > 0 && !this.formReservation().clubId) {
          this.formReservation.update(f => ({ ...f, clubId: data[0].id }));
        }
      },
      error: () => this.ui.toast('Erreur chargement clubs', 'error'),
    });
  }

  refresh(): void {
    this.loadData();
    this.ui.toast('Réservations actualisées', 'success');
  }

  openAddModal(): void {
    this.formReservation.set({
      piscineId: this.piscines().length > 0 ? this.piscines()[0].id : undefined,
      clubId: this.clubs().length > 0 ? this.clubs()[0].id : undefined,
      date: new Date().toISOString().split('T')[0],
      heureDebut: '18:00',
      heureFin: '19:45',
      couloirDebut: 1,
      couloirFin: 2,
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveReservation(): void {
    const resData = this.formReservation() as Reservation;
    if (!resData.piscineId || !resData.date || !resData.heureDebut || !resData.heureFin) {
      this.ui.toast('Veuillez remplir tous les champs obligatoires', 'error');
      return;
    }

    resData.statut = 'EN_ATTENTE';

    this.reservationService.create(resData).subscribe({
      next: () => {
        this.ui.toast('Demande de réservation créée avec succès (En attente)', 'success');
        this.closeModal();
        this.loadData();
      },
      error: (err) => {
        const msg = err?.error || 'Erreur lors de la création de la réservation';
        this.ui.toast(msg, 'error');
      },
    });
  }

  cancelReservation(id: number): void {
    if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
      this.reservationService.updateStatus(id, 'ANNULE').subscribe({
        next: () => {
          this.ui.toast('Réservation annulée avec succès', 'success');
          this.loadData();
        },
        error: () => this.ui.toast('Erreur lors de l\'annulation', 'error'),
      });
    }
  }
}
