import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  viewChild,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminChartsService } from '../../../core/services/admin-charts.service';
import { AdminUiService } from '../../../core/services/admin-ui.service';

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterLink],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent implements AfterViewInit, OnDestroy {
  private readonly charts = inject(AdminChartsService);
  private readonly ui = inject(AdminUiService);

  private readonly pieCanvas = viewChild<ElementRef<HTMLCanvasElement>>('pieCanvas');
  private readonly lineCanvas = viewChild<ElementRef<HTMLCanvasElement>>('lineCanvas');
  private readonly legendEl = viewChild<ElementRef<HTMLElement>>('pieLegend');

  ngAfterViewInit(): void {
    const pie = this.pieCanvas()?.nativeElement;
    const line = this.lineCanvas()?.nativeElement;
    const legend = this.legendEl()?.nativeElement;
    if (pie && line && legend) {
      this.charts.init(pie, line, legend);
    }
  }

  ngOnDestroy(): void {
    this.charts.destroy();
  }

  refresh(): void {
    this.ui.toast('Tableau de bord actualisé', 'success');
    this.charts.refresh();
  }

  requestSlot(): void {
    this.ui.toast('Demande de créneau — aperçu (API à brancher)', 'info');
  }
}
