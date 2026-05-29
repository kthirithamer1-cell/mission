/**
 * EST Natation — UI admin club (aperçu, sans API).
 * Contexte : un club réserve des couloirs disponibles sur une piscine partagée.
 */
(function () {
  "use strict";

  var CLUB_NAME = "Espérance Sportive de Tunis";
  var THEME_KEY = "est-admin-theme";
  var TOAST_MS = 2800;
  var NAGEUR_CATEGORIES = [
    "A VENIR",
    "POUSSIN",
    "BENJAMINS",
    "MINIMES",
    "CADETS",
    "JUNIORS",
    "SENIORS",
  ];

  var CLUB_RECORDS = [
    { event: "50 m nage libre", time: "24.12", meta: "Cadets · 2025" },
    { event: "100 m nage libre", time: "52.45", meta: "Juniors · 2025" },
    { event: "100 m papillon", time: "54.82", meta: "Seniors · 2024" },
    { event: "100 m dos", time: "58.90", meta: "Cadets · 2025" },
    { event: "100 m brasse", time: "1:05.20", meta: "Minimes · 2024" },
    { event: "200 m 4 nages", time: "2:18.44", meta: "Juniors · 2025" },
    { event: "50 m papillon", time: "26.40", meta: "Cadets · 2025" },
    { event: "200 m nage libre", time: "1:58.30", meta: "Seniors · 2024" },
  ];

  function $(sel, root) {
    return (root || document).querySelector(sel);
  }

  function $$(sel, root) {
    return Array.from((root || document).querySelectorAll(sel));
  }

  function applyTheme(theme) {
    if (theme === "light") {
      document.body.setAttribute("data-theme", "light");
    } else {
      document.body.removeAttribute("data-theme");
    }
    try {
      localStorage.setItem(THEME_KEY, theme === "light" ? "light" : "dark");
    } catch (e) { /* ignore */ }
    if (typeof window.estRefreshCharts === "function") {
      window.estRefreshCharts();
    }
  }

  function initTheme() {
    var stored = "dark";
    try {
      stored = localStorage.getItem(THEME_KEY) || "dark";
    } catch (e) { /* ignore */ }
    applyTheme(stored === "light" ? "light" : "dark");
  }

  initTheme();

  function toast(message, type) {
    var host = $("#ui-toast-host");
    if (!host) {
      host = document.createElement("div");
      host.id = "ui-toast-host";
      host.className = "ui-toast-host";
      host.setAttribute("aria-live", "polite");
      document.body.appendChild(host);
    }
    var el = document.createElement("div");
    el.className = "ui-toast ui-toast--" + (type || "info");
    el.textContent = message;
    host.appendChild(el);
    requestAnimationFrame(function () {
      el.classList.add("is-visible");
    });
    setTimeout(function () {
      el.classList.remove("is-visible");
      setTimeout(function () {
        el.remove();
      }, 280);
    }, TOAST_MS);
  }

  function ensureModalHost() {
    var backdrop = $("#ui-modal-backdrop");
    if (!backdrop) {
      backdrop = document.createElement("div");
      backdrop.id = "ui-modal-backdrop";
      backdrop.className = "ui-modal-backdrop";
      backdrop.hidden = true;
      backdrop.innerHTML =
        '<div class="ui-modal" role="dialog" aria-modal="true" aria-labelledby="ui-modal-title">' +
        '  <button type="button" class="ui-modal-close" aria-label="Fermer">&times;</button>' +
        '  <p class="dash-card-label" id="ui-modal-label"></p>' +
        '  <h2 class="dash-card-title" id="ui-modal-title"></h2>' +
        '  <div class="ui-modal-body" id="ui-modal-body"></div>' +
        '  <div class="ui-modal-actions" id="ui-modal-actions"></div>' +
        "</div>";
      document.body.appendChild(backdrop);
      backdrop.addEventListener("click", function (e) {
        if (e.target === backdrop) closeModal();
      });
      $(".ui-modal-close", backdrop).addEventListener("click", closeModal);
      document.addEventListener("keydown", function (e) {
        if (e.key === "Escape" && !backdrop.hidden) closeModal();
      });
    }
    return backdrop;
  }

  function openModal(opts) {
    var backdrop = ensureModalHost();
    $("#ui-modal-label", backdrop).textContent = opts.label || "";
    $("#ui-modal-title", backdrop).textContent = opts.title || "";
    $("#ui-modal-body", backdrop).innerHTML = opts.body || "";
    var actionsEl = $("#ui-modal-actions", backdrop);
    actionsEl.innerHTML = "";
    (opts.actions || []).forEach(function (action) {
      var btn = document.createElement("button");
      btn.type = "button";
      btn.className = "dash-btn" + (action.primary ? " dash-btn-primary" : "");
      btn.textContent = action.text;
      btn.addEventListener("click", function () {
        if (action.onClick) action.onClick();
        if (action.close !== false) closeModal();
      });
      actionsEl.appendChild(btn);
    });
    backdrop.hidden = false;
    requestAnimationFrame(function () {
      backdrop.classList.add("is-open");
    });
    document.body.classList.add("ui-modal-open");
  }

  function closeModal() {
    var backdrop = $("#ui-modal-backdrop");
    if (!backdrop) return;
    backdrop.classList.remove("is-open");
    document.body.classList.remove("ui-modal-open");
    setTimeout(function () {
      backdrop.hidden = true;
    }, 220);
  }

  function formField(id, label, type, placeholder, extra) {
    var attrs = extra || "";
    return (
      '<label class="ui-field" for="' + id + '"><span>' + label + "</span>" +
      '<input id="' + id + '" name="' + id + '" type="' + (type || "text") + '" placeholder="' +
      (placeholder || "") + '" ' + attrs + " /></label>"
    );
  }

  function formSelect(id, label, options, selected) {
    var html =
      '<label class="ui-field" for="' + id + '"><span>' + label + "</span><select id=\"" + id + "\" name=\"" + id + "\">";
    options.forEach(function (opt) {
      var sel = opt === selected ? " selected" : "";
      html += "<option value=\"" + opt + "\"" + sel + ">" + opt + "</option>";
    });
    html += "</select></label>";
    return html;
  }

  function escAttr(s) {
    return String(s || "")
      .replace(/&/g, "&amp;")
      .replace(/"/g, "&quot;")
      .replace(/</g, "&lt;");
  }

  function readAthleteRow(row) {
    if (!row) return {};
    return {
      nom: row.dataset.nom || "",
      prenom: row.dataset.prenom || "",
      email: row.dataset.email || "",
      cat: row.dataset.cat || "CADETS",
    };
  }

  function readCoachRow(row) {
    if (!row) return {};
    return {
      nom: row.dataset.nom || "",
      prenom: row.dataset.prenom || "",
      email: row.dataset.email || "",
      groups: row.dataset.groups || "",
      licence: row.dataset.licence || "Active",
    };
  }

  function openAthleteForm(data) {
    var edit = !!(data && data.nom);
    var d = data || {};
    openModal({
      label: "Effectif — " + CLUB_NAME,
      title: edit ? "Modifier le nageur" : "Ajouter un nageur au club",
      body:
        '<p class="ui-inline-hint">Le nageur est enregistré pour <strong>' + CLUB_NAME + "</strong> uniquement.</p>" +
        '<form class="ui-form">' +
        formField("a-nom", "Nom", "text", "Ben Ali", 'value="' + escAttr(d.nom) + '"') +
        formField("a-prenom", "Prénom", "text", "Ahmed", 'value="' + escAttr(d.prenom) + '"') +
        formField("a-email", "Email", "email", "ahmed@est.tn", 'value="' + escAttr(d.email) + '"') +
        formSelect("a-cat", "Catégorie / niveau", NAGEUR_CATEGORIES, d.cat || "CADETS") +
        formField("a-club", "Club", "text", CLUB_NAME, 'readonly value="' + CLUB_NAME + '"') +
        "</form>",
      actions: [
        { text: "Annuler", onClick: closeModal },
        {
          text: edit ? "Enregistrer les modifications" : "Enregistrer",
          primary: true,
          onClick: function () {
            toast(edit ? "Nageur mis à jour (aperçu UI)" : "Nageur ajouté au club (aperçu UI)", "success");
          },
        },
      ],
    });
  }

  function openCoachForm(data) {
    var edit = !!(data && data.nom);
    var d = data || {};
    openModal({
      label: "Staff — " + CLUB_NAME,
      title: edit ? "Modifier l'entraîneur" : "Ajouter un entraîneur",
      body:
        '<p class="ui-inline-hint">Entraîneur rattaché à <strong>' + CLUB_NAME + "</strong>.</p>" +
        '<form class="ui-form">' +
        formField("c-nom", "Nom", "text", "Trabelsi", 'value="' + escAttr(d.nom) + '"') +
        formField("c-prenom", "Prénom", "text", "Sami", 'value="' + escAttr(d.prenom) + '"') +
        formField("c-email", "Email", "email", "coach@est.tn", 'value="' + escAttr(d.email) + '"') +
        '<label class="ui-field"><span>Groupes suivis</span><input id="c-groups" name="c-groups" type="text" placeholder="ex. Cadets, Juniors" value="' +
        escAttr(d.groups) + '" /></label>' +
        formField("c-licence", "Licence", "text", "Active", 'value="' + escAttr(d.licence) + '"') +
        formField("c-club", "Club", "text", CLUB_NAME, 'readonly value="' + CLUB_NAME + '"') +
        "</form>",
      actions: [
        { text: "Annuler", onClick: closeModal },
        {
          text: edit ? "Enregistrer les modifications" : "Enregistrer",
          primary: true,
          onClick: function () {
            toast(edit ? "Entraîneur mis à jour (aperçu UI)" : "Entraîneur ajouté au club (aperçu UI)", "success");
          },
        },
      ],
    });
  }

  function openRequestSlotForm() {
    openModal({
      label: "Réservation de couloirs",
      title: "Demander un créneau",
      body:
        '<p class="ui-modal-text">Demande envoyée à l\'<strong>exploitant de la piscine</strong>. Les autres clubs peuvent réserver d\'autres couloirs disponibles sur le même bassin.</p>' +
        '<form class="ui-form">' +
        formField("r-date", "Date", "date") +
        formField("r-start", "Heure début", "time") +
        formField("r-end", "Heure fin", "time") +
        '<label class="ui-field"><span>Bassin</span><select id="r-pool" class="ui-select-native">' +
        "<option>Bassin 50 m</option><option>Bassin 25 m</option><option>Piscine éveil</option></select></label>" +
        formField("r-lanes", "Couloirs souhaités", "text", "ex. 3 à 6") +
        formField("r-club", "Club demandeur", "text", CLUB_NAME, 'readonly value="' + CLUB_NAME + '"') +
        "</form>",
      actions: [
        { text: "Annuler", onClick: closeModal },
        {
          text: "Envoyer la demande",
          primary: true,
          onClick: function () {
            toast("Demande envoyée à l'exploitant (aperçu UI)", "success");
          },
        },
      ],
    });
  }

  function openRecordsPalmarès() {
    var list = CLUB_RECORDS.map(function (r) {
      return "<li><span>" + r.event + "</span><strong>" + r.time + "</strong><em>" + r.meta + "</em></li>";
    }).join("");
    openModal({
      label: "Performance — " + CLUB_NAME,
      title: "Palmarès du club",
      body:
        '<p class="ui-modal-text">Meilleurs temps enregistrés (nage libre, dos, brasse, papillon, 4 nages).</p>' +
        '<ul class="ui-result-preview ui-result-preview--full">' + list + "</ul>",
      actions: [{ text: "Fermer", primary: true }],
    });
  }

  function openResultsDetail(title) {
    openModal({
      label: "Résultats — " + CLUB_NAME,
      title: title,
      body:
        '<p class="ui-modal-text">Palmarès du club pour cette compétition.</p>' +
        '<ul class="ui-result-preview">' +
        "<li><span>50 m nage libre</span><strong>24.12</strong></li>" +
        "<li><span>100 m nage libre</span><strong>52.45</strong></li>" +
        "<li><span>100 m dos</span><strong>58.90</strong></li>" +
        "<li><span>100 m brasse</span><strong>1:05.20</strong></li>" +
        "<li><span>100 m papillon</span><strong>54.82</strong></li>" +
        "<li><span>200 m 4 nages</span><strong>2:18.44</strong></li>" +
        "</ul>",
      actions: [{ text: "Fermer", primary: true }],
    });
  }

  function withLoading(btn, doneMsg) {
    if (btn.classList.contains("is-loading")) return;
    btn.classList.add("is-loading");
    btn.disabled = true;
    setTimeout(function () {
      btn.classList.remove("is-loading");
      btn.disabled = false;
      if (doneMsg) toast(doneMsg, "success");
      if (typeof window.estRefreshCharts === "function") {
        window.estRefreshCharts();
      }
    }, 500);
  }

  function initResultatsFilters() {
    var search = $(".comp-search-input");
    var pills = $$(".year-pill");
    var cards = $$(".comp-card-item");
    if (!pills.length && !search) return;

    cards.forEach(function (card, i) {
      if (!card.dataset.year) card.dataset.year = i < 3 ? "2024" : "2023";
    });

    function apply() {
      var q = search ? search.value.trim().toLowerCase() : "";
      var active = $(".year-pill.is-active");
      var year = active ? active.textContent.trim() : "Toutes";
      cards.forEach(function (card) {
        var t = card.querySelector(".comp-card-title");
        var title = t ? t.textContent.toLowerCase() : "";
        var ok = (!q || title.indexOf(q) >= 0) && (year === "Toutes" || card.dataset.year === year);
        card.classList.toggle("is-filtered-out", !ok);
      });
      $$(".season-column").forEach(function (col) {
        var n = col.querySelectorAll(".comp-card-item:not(.is-filtered-out)").length;
        col.classList.toggle("is-column-empty", n === 0);
      });
    }

    if (search) search.addEventListener("input", apply);
    pills.forEach(function (pill) {
      pill.addEventListener("click", function () {
        pills.forEach(function (p) {
          p.classList.remove("is-active");
        });
        pill.classList.add("is-active");
        apply();
      });
    });
    apply();
  }

  function initStatTiles() {
    $$(".stat-tile[data-href]").forEach(function (tile) {
      var href = tile.getAttribute("data-href");
      if (!href) return;
      tile.setAttribute("role", "link");
      tile.setAttribute("tabindex", "0");
      function go() {
        window.location.href = href;
      }
      tile.addEventListener("click", go);
      tile.addEventListener("keydown", function (e) {
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          go();
        }
      });
    });
  }

  function initHeader() {
    syncHeaderActions();
    var menuBtn = $(".dash-icon-btn[title='Menu']");
    if (menuBtn) {
      menuBtn.addEventListener("click", function () {
        var nav = $(".dash-nav-links");
        if (nav) nav.classList.toggle("is-mobile-open");
      });
    }
    $$(".dash-avatar").forEach(function (av) {
      av.setAttribute("role", "button");
      av.setAttribute("tabindex", "0");
      av.addEventListener("click", function () {
        openModal({
          label: "Compte",
          title: "Administrateur club",
          body: "<p class=\"ui-modal-text\">Connecté en tant qu'administrateur de <strong>" + CLUB_NAME + "</strong>.</p>",
          actions: [{ text: "Fermer", primary: true }],
        });
      });
    });
  }

  function syncHeaderActions() {
    var actions = $(".dash-header-actions");
    if (!actions) return;

    if (typeof EstApi !== "undefined" && EstApi.getToken && EstApi.getToken() && !actions.querySelector('[data-ui-action="logout"]')) {
      var logout = document.createElement("button");
      logout.type = "button";
      logout.className = "dash-btn";
      logout.setAttribute("data-ui-action", "logout");
      logout.textContent = "Déconnexion";
      actions.appendChild(logout);
    }

    var nav = $(".dash-nav-links");
    if (nav && typeof EstApi !== "undefined" && EstApi.isSuperAdmin && EstApi.isSuperAdmin() && !nav.querySelector('a[href="super-admin.html"]')) {
      var item = document.createElement("li");
      item.innerHTML = '<a href="super-admin.html">Plateforme</a>';
      nav.appendChild(item);
    }
  }

  function handleAction(action, btn) {
    switch (action) {
      case "toggle-theme": {
        var isLight = document.body.getAttribute("data-theme") === "light";
        applyTheme(isLight ? "dark" : "light");
        toast(isLight ? "Thème sombre activé" : "Thème clair activé", "info");
        break;
      }
      case "logout":
        if (typeof EstApi !== "undefined" && EstApi.logout) {
          EstApi.logout();
        } else {
          window.location.href = "login.html";
        }
        break;
      case "add-athlete":
        openAthleteForm();
        break;
      case "edit-athlete":
        openAthleteForm(readAthleteRow(btn.closest("tr")));
        break;
      case "add-coach":
        openCoachForm();
        break;
      case "edit-coach":
        openCoachForm(readCoachRow(btn.closest("tr")));
        break;
      case "request-slot":
        openRequestSlotForm();
        break;
      case "refresh":
        if (typeof window.estPageRefresh === "function") {
          window.estPageRefresh();
        }
        withLoading(btn, "Données actualisées");
        break;
      case "cancel-slot":
        openModal({
          label: "Réservation",
          title: "Annuler ce créneau ?",
          body: "<p class=\"ui-modal-text\">L'exploitant de la piscine sera notifié. Les couloirs seront libérés pour les autres clubs.</p>",
          actions: [
            { text: "Non", onClick: closeModal },
            {
              text: "Confirmer",
              primary: true,
              onClick: function () {
                toast("Réservation annulée (aperçu UI)", "success");
              },
            },
          ],
        });
        break;
      case "view-results": {
        var card = btn.closest(".comp-card-item");
        var t = card ? card.querySelector(".comp-card-title") : null;
        openResultsDetail(t ? t.textContent.trim() : "Résultats");
        break;
      }
      case "chrono-detail":
        openRecordsPalmarès();
        break;
      default:
        break;
    }
  }

  function init() {
    initResultatsFilters();
    initStatTiles();
    initHeader();

    document.body.addEventListener("click", function (e) {
      var btn = e.target.closest("[data-ui-action]");
      if (!btn || btn.disabled) return;
      var tile = btn.closest(".stat-tile[data-href]");
      if (tile && btn.getAttribute("data-ui-action") !== "toggle-theme") return;
      e.preventDefault();
      handleAction(btn.getAttribute("data-ui-action"), btn);
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
