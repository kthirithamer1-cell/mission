import { Injectable } from '@angular/core';
import { Chart } from 'chart.js';
import { DashboardStats } from '../models/app.models';

const COLORS = [
  '#818cf8',
  '#22d3ee',
  '#34d399',
  '#00b8e6',
  '#fbbf24',
  '#fb923c',
  '#f87171',
];

const LINE_LABELS = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'];

@Injectable({ providedIn: 'root' })
export class AdminChartsService {
  private pieChart: Chart<'pie'> | null = null;
  private lineChart: Chart<'line'> | null = null;

  init(pieCanvas: HTMLCanvasElement, lineCanvas: HTMLCanvasElement, legendEl: HTMLElement, stats: DashboardStats): void {
    this.destroy();
    const c = this.chartColors();

    const categories = Object.keys(stats.repartitionCategories || {});
    const counts = Object.values(stats.repartitionCategories || {});

    this.pieChart = new Chart(pieCanvas, {
      type: 'pie',
      data: {
        labels: categories,
        datasets: [
          {
            data: counts,
            backgroundColor: COLORS.slice(0, categories.length),
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

    legendEl.innerHTML = categories.map(
      (label, i) =>
        `<span class="pie-legend-item"><span class="pie-legend-dot" style="background:${COLORS[i % COLORS.length]}"></span>${label} <em>${counts[i]}</em></span>`
    ).join('');

    const datasets = Object.keys(stats.evolutionParCategorie || {}).map((cat, i) => ({
      label: cat,
      data: stats.evolutionParCategorie[cat] || [],
      borderColor: COLORS[i % COLORS.length],
      backgroundColor: COLORS[i % COLORS.length] + '33',
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

  private chartColors() {
    const light = document.body.getAttribute('data-theme') === 'light';
    return {
      text: light ? '#334155' : '#8b9cb3',
      grid: light ? 'rgba(15,23,42,0.08)' : 'rgba(255,255,255,0.06)',
      border: light ? '#e2e8f0' : 'rgba(255,255,255,0.08)',
    };
  }
}
