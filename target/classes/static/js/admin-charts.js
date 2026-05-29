/**
 * Graphiques tableau de bord: repartition (pie) + evolution par niveau (line).
 */
(function () {
  "use strict";

  var CATEGORIES = [
    "A VENIR",
    "POUSSIN",
    "BENJAMINS",
    "MINIMES",
    "CADETS",
    "JUNIORS",
    "SENIORS",
  ];

  var COLORS = [
    "#818cf8",
    "#22d3ee",
    "#34d399",
    "#00b8e6",
    "#fbbf24",
    "#fb923c",
    "#f87171",
  ];

  var LINE_LABELS = ["Jan", "Fev", "Mar", "Avr", "Mai", "Juin"];
  var pieChart = null;
  var lineChart = null;

  function stats() {
    return window.estDashboardStats || {};
  }

  function categoryCounts() {
    var repartition = stats().repartitionCategories || {};
    return CATEGORIES.map(function (cat) {
      return Number(repartition[cat] || 0);
    });
  }

  function categorySeries() {
    var evolution = stats().evolutionParCategorie || {};
    var counts = categoryCounts();
    var data = {};
    CATEGORIES.forEach(function (cat, i) {
      data[cat] = evolution[cat] || Array(6).fill(counts[i] || 0);
    });
    return data;
  }

  function chartColors() {
    var light = document.body.getAttribute("data-theme") === "light";
    return {
      text: light ? "#334155" : "#8b9cb3",
      grid: light ? "rgba(15,23,42,0.08)" : "rgba(255,255,255,0.06)",
      border: light ? "#e2e8f0" : "rgba(255,255,255,0.08)",
    };
  }

  function destroyCharts() {
    if (pieChart) {
      pieChart.destroy();
      pieChart = null;
    }
    if (lineChart) {
      lineChart.destroy();
      lineChart = null;
    }
  }

  function initPieChart() {
    var canvas = document.getElementById("chart-cat-pie");
    if (!canvas || typeof Chart === "undefined") return;

    var c = chartColors();
    var counts = categoryCounts();
    var total = counts.reduce(function (a, b) { return a + b; }, 0);
    var chartLabels = total ? CATEGORIES : ["Aucune donnee"];
    var chartCounts = total ? counts : [1];
    var chartColorsList = total ? COLORS : ["rgba(139,156,179,0.18)"];
    pieChart = new Chart(canvas, {
      type: "pie",
      data: {
        labels: chartLabels,
        datasets: [{
          data: chartCounts,
          backgroundColor: chartColorsList,
          borderColor: c.border,
          borderWidth: 2,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (ctx) {
                if (!total) return "Aucune donnee";
                var pct = total ? Math.round((ctx.raw / total) * 100) : 0;
                return ctx.label + ": " + ctx.raw + " (" + pct + "%)";
              },
            },
          },
        },
      },
    });

    var legend = document.getElementById("cat-pie-legend");
    if (legend) {
      if (!total) {
        legend.innerHTML = '<span class="pie-legend-item"><span class="pie-legend-dot" style="background:rgba(139,156,179,0.35)"></span>Aucun nageur dans la liste API</span>';
        return;
      }
      legend.innerHTML = CATEGORIES.map(function (label, i) {
        var pct = total ? Math.round((counts[i] / total) * 100) : 0;
        return '<span class="pie-legend-item"><span class="pie-legend-dot" style="background:' +
          COLORS[i] + '"></span>' + label + " <em>" + pct + "%</em></span>";
      }).join("");
    }
  }

  function initLineChart() {
    var canvas = document.getElementById("chart-cat-line");
    if (!canvas || typeof Chart === "undefined") return;

    var c = chartColors();
    var liveData = categorySeries();
    var datasets = CATEGORIES.map(function (cat, i) {
      return {
        label: cat,
        data: liveData[cat],
        borderColor: COLORS[i],
        backgroundColor: COLORS[i] + (document.body.getAttribute("data-theme") === "light" ? "22" : "33"),
        borderWidth: 2,
        tension: 0.35,
        fill: false,
        pointRadius: 3,
        pointHoverRadius: 5,
      };
    });

    lineChart = new Chart(canvas, {
      type: "line",
      data: { labels: LINE_LABELS, datasets: datasets },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: "index", intersect: false },
        plugins: {
          legend: {
            position: "bottom",
            labels: { color: c.text, boxWidth: 10, font: { size: 10 }, padding: 12 },
          },
        },
        scales: {
          x: { ticks: { color: c.text }, grid: { color: c.grid } },
          y: {
            beginAtZero: true,
            ticks: { color: c.text, stepSize: 1 },
            grid: { color: c.grid },
            title: { display: true, text: "Nageurs actifs", color: c.text },
          },
        },
      },
    });
  }

  function init() {
    if (document.body.getAttribute("data-page") !== "dashboard") return;
    initPieChart();
    initLineChart();
  }

  window.estRefreshCharts = function () {
    destroyCharts();
    init();
  };

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
