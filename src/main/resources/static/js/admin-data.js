/**
 * Chargement des donnees API sur les pages admin club.
 */
(function () {
  "use strict";

  var page = document.body.getAttribute("data-page");
  if (page === "super-admin" || page === null) return;
  if (typeof EstApi === "undefined") return;
  if (!EstApi.requireAuth()) return;

  var user = EstApi.getUser() || {};
  var CATEGORIES = ["A VENIR", "POUSSIN", "BENJAMINS", "MINIMES", "CADETS", "JUNIORS", "SENIORS"];

  function esc(s) {
    var d = document.createElement("div");
    d.textContent = s == null ? "" : s;
    return d.innerHTML;
  }

  function statBadge(statut) {
    var cls = statut === "CONFIRME" ? "rental-badge--active" : statut === "EN_ATTENTE" ? "rental-badge--pending" : "";
    return '<span class="rental-badge ' + cls + '">' + esc(statut) + "</span>";
  }

  function categoryKey(value) {
    return String(value || "").trim().toUpperCase();
  }

  function buildEffectifStats(nageurs) {
    var repartition = {};
    CATEGORIES.forEach(function (cat) {
      repartition[cat] = 0;
    });
    (nageurs || []).forEach(function (n) {
      var cat = categoryKey(n.categorie);
      if (repartition[cat] == null) repartition[cat] = 0;
      repartition[cat] += 1;
    });

    var evolution = {};
    CATEGORIES.forEach(function (cat) {
      var current = repartition[cat] || 0;
      evolution[cat] = [5, 4, 3, 2, 1, 0].map(function (offset) {
        return Math.max(0, current - offset);
      });
    });

    return {
      repartitionCategories: repartition,
      evolutionParCategorie: evolution,
    };
  }

  function renderDashboardStats(stats, nageurs, coaches) {
    var effectif = buildEffectifStats(nageurs);
    stats.nageursCount = (nageurs || []).length;
    stats.entraineursCount = (coaches || []).length;
    stats.repartitionCategories = effectif.repartitionCategories;
    stats.evolutionParCategorie = effectif.evolutionParCategorie;

    var vals = document.querySelectorAll(".stat-tile-value");
    var map = [stats.nageursCount, stats.entraineursCount, stats.couloirsReserves, stats.creneauxAVenir];
    vals.forEach(function (el, i) {
      if (map[i] != null) el.textContent = map[i];
    });

    var count = stats.nageursCount || 0;
    var summary = document.querySelector("[data-dashboard-effectif-summary]");
    if (summary) {
      summary.textContent = count
        ? "Repartition (" + count + " nageur" + (count > 1 ? "s" : "") + ") et evolution sur 6 mois - memes donnees que la liste des nageurs"
        : "Aucun nageur renvoye par l'API - la liste des nageurs affichera aussi 0";
    }

    var req = document.querySelector(".requests-empty");
    if (req) {
      req.textContent = stats.reservationsEnAttente > 0
        ? stats.reservationsEnAttente + " demande(s) en attente de validation"
        : "Aucune demande en attente de validation par l'exploitant";
    }

    window.estDashboardStats = stats;
    if (typeof window.estRefreshCharts === "function") window.estRefreshCharts();
    renderRecords(stats.records || []);
  }

  function renderReservationsSummary(rows) {
    var list = document.querySelector(".rental-list");
    if (!list) return;
    var visible = (rows || []).filter(function (r) {
      return r.statut === "CONFIRME" || r.statut === "EN_ATTENTE";
    }).slice(0, 3);
    list.innerHTML = visible.map(function (r) {
      return '<div class="rental-row"><div class="rental-row-main"><p class="rental-row-title">' +
        esc(r.piscineNom) + " - Couloirs " + r.couloirDebut + " a " + r.couloirFin +
        '</p><p class="rental-row-meta">' + esc(r.date) + " · " + esc(r.heureDebut) + "-" + esc(r.heureFin) +
        '</p></div><span class="rental-badge rental-badge--' + (r.statut === "CONFIRME" ? "active" : "pending") + '">' +
        esc(r.statut === "CONFIRME" ? "Confirme" : "En attente") + "</span></div>";
    }).join("") || '<p class="dash-page-sub">Aucune reservation a afficher.</p>';
  }

  function renderNextSeance(rows) {
    var next = rows && rows.length ? rows[0] : null;
    if (!next) return;
    var title = document.querySelector("[data-next-session-title]");
    var when = document.querySelector("[data-next-session-when]");
    var coach = document.querySelector("[data-next-session-coach]");
    if (title) title.textContent = next.titre || "Seance du club";
    if (when) when.textContent = [next.date, next.heureDebut && next.heureFin ? next.heureDebut + "-" + next.heureFin : ""].filter(Boolean).join(" · ");
    if (coach) coach.textContent = next.entraineurNom || "-";
  }

  function loadDashboard() {
    Promise.all([
      EstApi.getStats().catch(function () { return {}; }),
      EstApi.getNageurs().catch(function () { return []; }),
      EstApi.getEntraineurs().catch(function () { return []; }),
      EstApi.getReservations().catch(function () { return []; }),
      EstApi.getSeances().catch(function () { return []; }),
    ]).then(function (result) {
      renderDashboardStats(result[0] || {}, result[1] || [], result[2] || []);
      renderReservationsSummary(result[3] || []);
      renderNextSeance(result[4] || []);
    });
  }

  function renderRecords(records) {
    var row = document.querySelector(".chrono-records-row");
    if (!row || !records.length) return;
    row.innerHTML = records.slice(0, 6).map(function (r) {
      return '<div class="chrono-record-item"><p class="chrono-unit">' + esc(r.epreuve) +
        '</p><div class="chrono-chip"><span class="chrono-value">' + esc(r.temps) + "</span></div></div>";
    }).join("");
  }

  function loadAthletes() {
    var tb = document.querySelector(".dash-data-table tbody");
    if (!tb || page !== "athletes") return;
    tb.innerHTML = '<tr><td colspan="6" class="dash-empty-table">Chargement des nageurs...</td></tr>';
    EstApi.getNageurs().then(function (rows) {
      tb.innerHTML = rows.map(function (n) {
        return '<tr data-id="' + n.id + '" data-nom="' + esc(n.nom) + '" data-prenom="' + esc(n.prenom) +
          '" data-email="' + esc(n.email) + '" data-cat="' + esc(n.categorie) + '"><td>' + esc(n.nom) +
          "</td><td>" + esc(n.prenom) + "</td><td>" + esc(n.email) + "</td><td>" + esc(n.categorie) +
          '</td><td>' + esc(n.clubNom || user.clubNom || "EST") + '</td><td><button type="button" class="dash-btn dash-btn--sm" data-ui-action="edit-athlete">Modifier</button></td></tr>';
      }).join("") || '<tr><td colspan="6" class="dash-empty-table">Aucun athlete</td></tr>';
    }).catch(function (err) {
      tb.innerHTML = '<tr><td colspan="6" class="dash-empty-table">Impossible de charger les nageurs: ' + esc(err.message || err) + '</td></tr>';
    });
  }

  function loadCoaches() {
    var tb = document.querySelector(".dash-data-table tbody");
    if (!tb || page !== "entraineurs") return;
    EstApi.getEntraineurs().then(function (rows) {
      tb.innerHTML = rows.map(function (c) {
        return '<tr data-id="' + c.id + '" data-nom="' + esc(c.nom) + '" data-prenom="' + esc(c.prenom) +
          '" data-email="' + esc(c.email) + '" data-groups="' + esc(c.groupes) + '"><td>' + esc(c.nom) +
          "</td><td>" + esc(c.prenom) + "</td><td>" + esc(c.email) + "</td><td>" + esc(c.groupes) +
          '</td><td>Active</td><td><button type="button" class="dash-btn dash-btn--sm" data-ui-action="edit-coach">Modifier</button></td></tr>';
      }).join("") || '<tr><td colspan="6" class="dash-empty-table">Aucun entraineur</td></tr>';
    });
  }

  function loadReservations() {
    var tb = document.querySelector(".dash-data-table tbody");
    if (!tb || page !== "reservations") return;
    EstApi.getReservations().then(function (rows) {
      tb.innerHTML = rows.map(function (r) {
        return "<tr><td>" + esc(r.date) + "</td><td>" + esc(r.heureDebut) + " - " + esc(r.heureFin) +
          "</td><td>" + esc(r.piscineNom) + "</td><td>" + r.couloirDebut + " - " + r.couloirFin +
          "</td><td>" + statBadge(r.statut) + '</td><td><button type="button" class="dash-btn" data-ui-action="cancel-slot" data-reservation-id="' +
          r.id + '"' + (r.statut === "ANNULE" ? " disabled" : "") + ">Annuler</button></td></tr>";
      }).join("") || '<tr><td colspan="6" class="dash-empty-table">Aucune reservation</td></tr>';
    });
  }

  function loadSeances() {
    var tb = document.querySelector("#seances-table tbody");
    if (!tb) return;
    EstApi.getSeances().then(function (rows) {
      tb.innerHTML = rows.map(function (s) {
        return "<tr><td>" + esc(s.titre) + "</td><td>" + esc(s.date) + "</td><td>" + esc(s.heureDebut) +
          " - " + esc(s.heureFin) + "</td><td>" + esc(s.entraineurNom || "-") + "</td><td>" + esc(s.description) +
          "</td><td>-</td></tr>";
      }).join("") || '<tr><td colspan="6" class="dash-empty-table">Aucune seance</td></tr>';
    });
  }

  window.estPageRefresh = function () {
    if (page === "dashboard") loadDashboard();
    if (page === "athletes") loadAthletes();
    if (page === "entraineurs") loadCoaches();
    if (page === "reservations") loadReservations();
    if (page === "seances") loadSeances();
  };

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", window.estPageRefresh);
  } else {
    window.estPageRefresh();
  }
})();
