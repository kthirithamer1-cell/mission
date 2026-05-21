/**
 * Graphiques tableau de bord — répartition (pie) + évolution par niveau (line).
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

  var PIE_COUNTS = [2, 4, 5, 4, 3, 3, 3];

  var LINE_LABELS = ["Jan", "Fév", "Mar", "Avr", "Mai", "Juin"];
  var LINE_DATA = {
    "A VENIR": [1, 1, 2, 2, 2, 2],
    POUSSIN: [2, 3, 3, 4, 4, 4],
    BENJAMINS: [3, 4, 4, 5, 5, 5],
    MINIMES: [2, 3, 3, 4, 4, 4],
    CADETS: [2, 2, 3, 3, 3, 3],
    JUNIORS: [2, 2, 2, 3, 3, 3],
    SENIORS: [2, 2, 3, 3, 3, 3],
  };

  function chartColors() {
    var light = document.body.getAttribute("data-theme") === "light";
    return {
      text: light ? "#334155" : "#8b9cb3",
      grid: light ? "rgba(15,23,42,0.08)" : "rgba(255,255,255,0.06)",
      border: light ? "#e2e8f0" : "rgba(255,255,255,0.08)",
    };
  }

  var pieChart = null;
  var lineChart = null;

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
    pieChart = new Chart(canvas, {
      type: "pie",
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
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (ctx) {
                var total = PIE_COUNTS.reduce(function (a, b) {
                  return a + b;
                }, 0);
                var pct = Math.round((ctx.raw / total) * 100);
                return ctx.label + ": " + ctx.raw + " (" + pct + "%)";
              },
            },
          },
        },
      },
    });

    var legend = document.getElementById("cat-pie-legend");
    if (legend) {
      var total = PIE_COUNTS.reduce(function (a, b) {
        return a + b;
      }, 0);
      legend.innerHTML = CATEGORIES.map(function (label, i) {
        var pct = Math.round((PIE_COUNTS[i] / total) * 100);
        return (
          '<span class="pie-legend-item"><span class="pie-legend-dot" style="background:' +
          COLORS[i] +
          '"></span>' +
          label +
          " <em>" +
          pct +
          "%</em></span>"
        );
      }).join("");
    }
  }

  function initLineChart() {
    var canvas = document.getElementById("chart-cat-line");
    if (!canvas || typeof Chart === "undefined") return;

    var c = chartColors();
    var datasets = CATEGORIES.map(function (cat, i) {
      return {
        label: cat,
        data: LINE_DATA[cat],
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
      data: {
        labels: LINE_LABELS,
        datasets: datasets,
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: "index", intersect: false },
        plugins: {
          legend: {
            position: "bottom",
            labels: {
              color: c.text,
              boxWidth: 10,
              font: { size: 10 },
              padding: 12,
            },
          },
        },
        scales: {
          x: {
            ticks: { color: c.text },
            grid: { color: c.grid },
          },
          y: {
            beginAtZero: true,
            ticks: { color: c.text, stepSize: 1 },
            grid: { color: c.grid },
            title: {
              display: true,
              text: "Nageurs actifs",
              color: c.text,
            },
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
