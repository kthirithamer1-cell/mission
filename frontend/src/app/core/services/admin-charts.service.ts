import { Injectable } from '@angular/core';
import { Chart } from 'chart.js';

const CATEGORIES = [
  'A VENIR',
  'POUSSIN',
  'BENJAMINS',
  'MINIMES',
  'CADETS',
  'JUNIORS',
  'SENIORS',
];

const COLORS = [
  '#818cf8',
  '#22d3ee',
  '#34d399',
  '#00b8e6',
  '#fbbf24',
  '#fb923c',
  '#f87171',
];

const PIE_COUNTS = [2, 4, 5, 4, 3, 3, 3];
const LINE_LABELS = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'];

@Injectable({ providedIn: 'root' })
export class AdminChartsService {
  private pieChart: Chart<'pie'> | null = null;
  private lineChart: Chart<'line'> | null = null;

  init(pieCanvas: HTMLCanvasElement, lineCanvas: HTMLCanvasElement, legendEl: HTMLElement): void {
    this.destroy();
    const c = this.chartColors();
    this.pieChart = new Chart(pieCanvas, {
      type: 'pie',
      data: {
        labels: CATEGORIES,
        datasets: [
          {
            data: PIE_COUNTS,
            backgroundColor: COLORS,
            borderColor: c.border,
            borderWidth: 2,
          },
        ],
      },
      options: {
        plugins: { legend: { display: false } },
        responsive: true,
        maintainAspectRatio: true,
      },
    });

    legendEl.innerHTML = CATEGORIES.map(
      (label, i) =>
        `<span class="pie-legend-item"><span class="pie-legend-dot" style="background:${COLORS[i]}"></span>${label} <em>${PIE_COUNTS[i]}</em></span>`
    ).join('');

    const datasets = CATEGORIES.map((cat, i) => ({
      label: cat,
      data: this.lineDataFor(cat),
      borderColor: COLORS[i],
      backgroundColor: COLORS[i] + '33',
      tension: 0.35,
      fill: false,
    }));

    this.lineChart = new Chart(lineCanvas, {
      type: 'line',
      data: { labels: LINE_LABELS, datasets },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { labels: { color: c.text } } },
        scales: {
          x: { ticks: { color: c.text }, grid: { color: c.grid } },
          y: { ticks: { color: c.text }, grid: { color: c.grid } },
        },
      },
    });
  }

  refresh(): void {
    if (this.pieChart) this.pieChart.update();
    if (this.lineChart) this.lineChart.update();
  }

  destroy(): void {
    this.pieChart?.destroy();
    this.lineChart?.destroy();
    this.pieChart = null;
    this.lineChart = null;
  }

  private lineDataFor(cat: string): number[] {
    const data: Record<string, number[]> = {
      'A VENIR': [1, 1, 2, 2, 2, 2],
      POUSSIN: [2, 3, 3, 4, 4, 4],
      BENJAMINS: [3, 4, 4, 5, 5, 5],
      MINIMES: [2, 3, 3, 4, 4, 4],
      CADETS: [2, 2, 3, 3, 3, 3],
      JUNIORS: [2, 2, 2, 3, 3, 3],
      SENIORS: [2, 2, 3, 3, 3, 3],
    };
    return data[cat] ?? [0, 0, 0, 0, 0, 0];
  }

  private chartColors() {
    const light = document.body.getAttribute('data-theme') === 'light';
    return {
      text: light ? '#334155' : '#8b9cb3',
      grid: light ? 'rgba(15,23,42,0.08)' : 'rgba(255,255,255,0.06)',
      border: light ? '#e2e8f0' : 'rgba(255,255,255,0.08)',
    };
  }
}
