/**
 * Super admin plateforme — piscines, clubs, réservations, utilisateurs.
 */
(function () {
  "use strict";

  if (!EstApi.requireAuth()) return;
  if (!EstApi.isSuperAdmin()) {
    window.location.href = "admin-dashboard.html";
    return;
  }

  function esc(s) {
    var d = document.createElement("div");
    d.textContent = s || "";
    return d.innerHTML;
  }

  function badge(statut) {
    var cls = statut === "CONFIRME" ? "rental-badge--active" : statut === "EN_ATTENTE" ? "rental-badge--pending" : "";
    return '<span class="rental-badge ' + cls + '">' + esc(statut) + "</span>";
  }

  function loadStats() {
    EstApi.getStats().then(function (s) {
      var el = document.getElementById("platform-stats");
      if (!el) return;
      el.innerHTML =
        '<article class="stat-tile"><p class="stat-tile-label">Piscines</p><p class="stat-tile-value">' + s.piscinesCount + "</p></article>" +
        '<article class="stat-tile"><p class="stat-tile-label">Clubs</p><p class="stat-tile-value">' + s.clubsCount + "</p></article>" +
        '<article class="stat-tile"><p class="stat-tile-label">Nageurs</p><p class="stat-tile-value">' + s.nageursCount + "</p></article>" +
        '<article class="stat-tile"><p class="stat-tile-label">En attente</p><p class="stat-tile-value">' + s.reservationsEnAttente + "</p></article>";
    });
  }

  function loadPiscines() {
    EstApi.getPiscines().then(function (rows) {
      var tb = document.querySelector("#piscines-table tbody");
      if (!tb) return;
      tb.innerHTML = rows.map(function (p) {
        return "<tr data-id=\"" + p.id + "\"><td>" + esc(p.nom) + "</td><td>" + esc(p.ville) + "</td><td>" + p.nombreCouloirs +
          "</td><td>" + p.longueurMetres + " m</td><td>" + (p.active ? "Active" : "Inactive") +
          '</td><td><button type="button" class="dash-btn dash-btn--sm" data-ui-action="edit-piscine" data-id="' + p.id + '">Modifier</button></td></tr>";
      }).join("") || '<tr><td colspan="6" class="dash-empty-table">Aucune piscine</td></tr>';
    });
  }

  function loadClubs() {
    EstApi.getClubs().then(function (rows) {
      var tb = document.querySelector("#clubs-table tbody");
      if (!tb) return;
      tb.innerHTML = rows.map(function (c) {
        return "<tr><td>" + esc(c.nom) + "</td><td>" + esc(c.adresse) + "</td><td>" + esc(c.dateAffiliation || "—") +
          '</td><td><button type="button" class="dash-btn dash-btn--sm" data-ui-action="edit-club" data-id="' + c.id + '">Modifier</button></td></tr>';
      }).join("") || '<tr><td colspan="4" class="dash-empty-table">Aucun club</td></tr>';
        // If clubs exist, point the header "Vue club" link to the first club for quick view
        try {
          if (rows && rows.length) {
            var viewLink = document.querySelector('.dash-nav-links a[href="admin-dashboard.html"]');
            if (viewLink) viewLink.href = 'admin-dashboard.html?clubId=' + rows[0].id;
          }
        } catch (e) { /* ignore */ }
    });
  }

  function loadReservations() {
    EstApi.getReservations().then(function (rows) {
      var tb = document.querySelector("#platform-reservations-table tbody");
      if (!tb) return;
      tb.innerHTML = rows.map(function (r) {
        var actions = "";
        if (r.statut === "EN_ATTENTE") {
          actions = '<button type="button" class="dash-btn dash-btn--sm dash-btn-primary" data-ui-action="confirm-reservation" data-id="' + r.id + '">Confirmer</button> ';
        }
        if (r.statut !== "ANNULE") {
          actions += '<button type="button" class="dash-btn dash-btn--sm" data-ui-action="cancel-reservation" data-id="' + r.id + '">Annuler</button>';
        }
        return "<tr><td>" + esc(r.clubNom) + "</td><td>" + esc(r.piscineNom) + "</td><td>" + esc(r.date) +
          "</td><td>" + esc(r.heureDebut) + " – " + esc(r.heureFin) + "</td><td>" + r.couloirDebut + "–" + r.couloirFin +
          "</td><td>" + badge(r.statut) + "</td><td class=\"table-actions\">" + actions + "</td></tr>";
      }).join("") || '<tr><td colspan="7" class="dash-empty-table">Aucune réservation</td></tr>';
    });
  }

  function loadUsers() {
    EstApi.getUtilisateurs().then(function (rows) {
      var tb = document.querySelector("#users-table tbody");
      if (!tb) return;
      tb.innerHTML = rows.map(function (u) {
        return "<tr data-id=\"" + u.id + "\" data-nom=\"" + esc(u.nom) + "\" data-prenom=\"" + esc(u.prenom) +
          "\" data-email=\"" + esc(u.email) + "\" data-role=\"" + esc(u.role) + "\" data-club-id=\"" + (u.clubId || "") +
          "\"><td>" + esc(u.prenom + " " + u.nom) + "</td><td>" + esc(u.email) + "</td><td>" + esc(u.role) +
          "</td><td>" + esc(u.clubNom || "—") + '</td><td><button type="button" class="dash-btn dash-btn--sm" data-ui-action="edit-user" data-id="' + u.id + '">Modifier</button></td></tr>';
      }).join("") || '<tr><td colspan="5" class="dash-empty-table">Aucun utilisateur</td></tr>';
    });
  }

  function refreshAll() {
    loadStats();
    loadPiscines();
    loadClubs();
    loadReservations();
    loadUsers();
  }

  function openModal(title, body, onSave) {
    var backdrop = document.createElement("div");
    backdrop.className = "ui-modal-backdrop is-open";
    backdrop.innerHTML = '<div class="ui-modal"><h2 class="dash-card-title">' + esc(title) +
      '</h2><div class="ui-modal-body">' + body + '</div><div class="ui-modal-actions">' +
      '<button type="button" class="dash-btn" id="pf-cancel">Annuler</button>' +
      '<button type="button" class="dash-btn dash-btn-primary" id="pf-save">Enregistrer</button></div></div>';
    document.body.appendChild(backdrop);
    backdrop.querySelector("#pf-cancel").onclick = function () { backdrop.remove(); };
    backdrop.querySelector("#pf-save").onclick = function () {
      onSave(function () { backdrop.remove(); refreshAll(); });
    };
    backdrop.addEventListener("click", function (ev) {
      if (ev.target === backdrop) backdrop.remove();
    });
  }

  function openPlatformPiscineForm(p) {
    p = p || {};
    openModal(p.id ? "Modifier piscine" : "Ajouter piscine",
      '<label class="ui-field"><span>Nom</span><input id="pf-nom" value="' + esc(p.nom || "") + '"></label>' +
      '<label class="ui-field"><span>Ville</span><input id="pf-ville" value="' + esc(p.ville || "") + '"></label>' +
      '<label class="ui-field"><span>Adresse</span><input id="pf-adresse" value="' + esc(p.adresse || "") + '"></label>' +
      '<label class="ui-field"><span>Couloirs</span><input id="pf-couloirs" type="number" value="' + (p.nombreCouloirs || 8) + '"></label>' +
      '<label class="ui-field"><span>Longueur (m)</span><input id="pf-long" type="number" value="' + (p.longueurMetres || 50) + '"></label>',
      function (done) {
        var dto = {
          nom: document.getElementById("pf-nom").value,
          ville: document.getElementById("pf-ville").value,
          adresse: document.getElementById("pf-adresse").value,
          nombreCouloirs: parseInt(document.getElementById("pf-couloirs").value, 10),
          longueurMetres: parseInt(document.getElementById("pf-long").value, 10),
          active: true,
        };
        var req = p.id ? EstApi.updatePiscine(p.id, dto) : EstApi.createPiscine(dto);
        req.then(done);
      });
  }

  function openPlatformClubForm(c) {
    c = c || {};
    openModal(c.id ? "Modifier club" : "Ajouter club",
      '<label class="ui-field"><span>Nom</span><input id="pf-nom" value="' + esc(c.nom || "") + '"></label>' +
      '<label class="ui-field"><span>Adresse</span><input id="pf-adresse" value="' + esc(c.adresse || "") + '"></label>' +
      '<label class="ui-field"><span>Date affiliation</span><input id="pf-date" type="date" value="' + esc(c.dateAffiliation || "") + '"></label>',
      function (done) {
        var dto = {
          nom: document.getElementById("pf-nom").value,
          adresse: document.getElementById("pf-adresse").value,
          dateAffiliation: document.getElementById("pf-date").value || null,
        };
        var req = c.id ? EstApi.updateClub(c.id, dto) : EstApi.createClub(dto);
        req.then(done);
      });
  }

  function openPlatformUserForm(u) {
    u = u || {};
    EstApi.getClubs().then(function (clubs) {
      var opts = clubs.map(function (c) {
        return '<option value="' + c.id + '"' + (u.clubId === c.id ? " selected" : "") + ">" + esc(c.nom) + "</option>";
      }).join("");
      openModal(u.id ? "Modifier utilisateur" : "Créer utilisateur",
        '<label class="ui-field"><span>Prénom</span><input id="pf-prenom" value="' + esc(u.prenom || "") + '"></label>' +
        '<label class="ui-field"><span>Nom</span><input id="pf-nom" value="' + esc(u.nom || "") + '"></label>' +
        '<label class="ui-field"><span>Email</span><input id="pf-email" type="email" value="' + esc(u.email || "") + '"></label>' +
        (u.id ? "" : '<label class="ui-field"><span>Mot de passe</span><input id="pf-pwd" type="password"></label>') +
        '<label class="ui-field"><span>Rôle</span><select id="pf-role" class="ui-select-native">' +
        '<option value="ADMIN"' + (u.role === "ADMIN" ? " selected" : "") + ">Admin club</option>" +
        '<option value="ENTRAINEUR"' + (u.role === "ENTRAINEUR" ? " selected" : "") + ">Entraîneur</option>" +
        '<option value="SUPER_ADMIN"' + (u.role === "SUPER_ADMIN" ? " selected" : "") + ">Super admin</option>" +
        "</select></label>" +
        '<label class="ui-field"><span>Club</span><select id="pf-club" class="ui-select-native"><option value="">—</option>' + opts + "</select></label>",
        function (done) {
          var dto = {
            prenom: document.getElementById("pf-prenom").value,
            nom: document.getElementById("pf-nom").value,
            email: document.getElementById("pf-email").value,
            role: document.getElementById("pf-role").value,
          };
          var clubVal = document.getElementById("pf-club").value;
          if (clubVal) dto.clubId = parseInt(clubVal, 10);
          if (dto.role === "SUPER_ADMIN") dto.superAdmin = true;
          var pwd = document.getElementById("pf-pwd");
          if (pwd && pwd.value) dto.motDePasse = pwd.value;
          if (dto.role === "ENTRAINEUR" && !u.id) {
            EstApi.createEntraineur(Object.assign({ groupes: "—" }, dto)).then(done);
          } else {
            var req = u.id ? EstApi.updateUtilisateur(u.id, dto) : EstApi.createUtilisateur(dto);
            req.then(done);
          }
        });
    });
  }

  document.body.addEventListener("click", function (e) {
    var btn = e.target.closest("[data-ui-action]");
    if (!btn) return;
    var action = btn.getAttribute("data-ui-action");
    var id = btn.getAttribute("data-id");

    if (action === "add-piscine") {
      openPlatformPiscineForm(null);
      return;
    }
    if (action === "edit-piscine" && id) {
      EstApi.getPiscines().then(function (list) {
        var p = list.find(function (x) { return String(x.id) === String(id); });
        if (p) openPlatformPiscineForm(p);
      });
      return;
    }
    if (action === "add-club") {
      openPlatformClubForm(null);
      return;
    }
    if (action === "edit-club" && id) {
      EstApi.getClubs().then(function (list) {
        var c = list.find(function (x) { return String(x.id) === String(id); });
        if (c) openPlatformClubForm(c);
      });
      return;
    }
    if (action === "add-user") {
      openPlatformUserForm(null);
      return;
    }
    if (action === "edit-user" && id) {
      var row = btn.closest("tr");
      if (row) openPlatformUserForm({
        id: row.dataset.id,
        nom: row.dataset.nom,
        prenom: row.dataset.prenom,
        email: row.dataset.email,
        role: row.dataset.role,
        clubId: row.dataset.clubId ? parseInt(row.dataset.clubId, 10) : null,
      });
      return;
    }
    if (action === "refresh-platform") {
      refreshAll();
      return;
    }
    if (action === "confirm-reservation" && id) {
      EstApi.updateReservationStatut(id, "CONFIRME").then(refreshAll);
      return;
    }
    if (action === "cancel-reservation" && id) {
      EstApi.updateReservationStatut(id, "ANNULE").then(refreshAll);
      return;
    }
    if (action === "logout") {
      EstApi.logout();
    }
  });

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", refreshAll);
  } else {
    refreshAll();
  }

  window.estPlatformRefresh = refreshAll;
})();
