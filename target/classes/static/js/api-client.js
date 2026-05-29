/**
 * Client API EST Natation — auth JWT + appels REST.
 */
(function (global) {
  "use strict";

  var TOKEN_KEY = "est_auth_token";
  var USER_KEY = "est_auth_user";

  function getToken() {
    return localStorage.getItem(TOKEN_KEY);
  }

  function getUser() {
    try {
      return JSON.parse(localStorage.getItem(USER_KEY) || "null");
    } catch (e) {
      return null;
    }
  }

  function saveSession(token, user) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  function isSuperAdmin() {
    var u = getUser();
    return u && (u.role === "SUPER_ADMIN" || u.superAdmin === true);
  }

  function isClubAdmin() {
    var u = getUser();
    return u && u.role === "ADMIN";
  }

  function requireAuth(loginPage) {
    if (!getToken()) {
      window.location.href = loginPage || "login.html";
      return false;
    }
    return true;
  }

  function apiFetch(path, options) {
    options = options || {};
    var headers = options.headers || {};
    headers["Content-Type"] = headers["Content-Type"] || "application/json";
    var token = getToken();
    if (token) headers["Authorization"] = "Bearer " + token;
    return fetch(path, {
      method: options.method || "GET",
      headers: headers,
      body: options.body ? JSON.stringify(options.body) : undefined,
    }).then(function (res) {
      if (res.status === 401) {
        clearSession();
        window.location.href = "login.html";
        throw new Error("Session expirée");
      }
      return res.text().then(function (text) {
        var data = text;
        try {
          data = text ? JSON.parse(text) : null;
        } catch (e) { /* plain text */ }
        if (!res.ok) {
          var msg = typeof data === "string" ? data : (data && data.message) || "Erreur " + res.status;
          throw new Error(msg);
        }
        return data;
      });
    });
  }

  function login(email, password) {
    return apiFetch("/api/auth/login", {
      method: "POST",
      body: { email: email, motDePasse: password },
    }).then(function (data) {
      saveSession(data.token, data.utilisateur);
      return data;
    });
  }

  function logout() {
    clearSession();
    window.location.href = "login.html";
  }

  function getStats() {
    return apiFetch("/api/dashboard/stats");
  }

  function getNageurs() {
    return apiFetch("/api/nageurs");
  }

  function createNageur(dto) {
    return apiFetch("/api/nageurs", { method: "POST", body: dto });
  }

  function updateNageur(id, dto) {
    return apiFetch("/api/nageurs/" + id, { method: "PUT", body: dto });
  }

  function getEntraineurs() {
    return apiFetch("/api/entraineurs");
  }

  function createEntraineur(dto) {
    return apiFetch("/api/entraineurs", { method: "POST", body: dto });
  }

  function updateEntraineur(id, dto) {
    return apiFetch("/api/entraineurs/" + id, { method: "PUT", body: dto });
  }

  function getReservations() {
    return apiFetch("/api/reservations");
  }

  function createReservation(dto) {
    return apiFetch("/api/reservations", { method: "POST", body: dto });
  }

  function updateReservationStatut(id, statut) {
    return apiFetch("/api/reservations/" + id + "/statut", {
      method: "PATCH",
      body: { statut: statut },
    });
  }

  function getSeances() {
    return apiFetch("/api/seances");
  }

  function createSeance(dto) {
    return apiFetch("/api/seances", { method: "POST", body: dto });
  }

  function updateSeance(id, dto) {
    return apiFetch("/api/seances/" + id, { method: "PUT", body: dto });
  }

  function getPiscines() {
    return apiFetch("/api/piscines");
  }

  function createPiscine(dto) {
    return apiFetch("/api/piscines", { method: "POST", body: dto });
  }

  function updatePiscine(id, dto) {
    return apiFetch("/api/piscines/" + id, { method: "PUT", body: dto });
  }

  function deletePiscine(id) {
    return apiFetch("/api/piscines/" + id, { method: "DELETE" });
  }

  function getClubs() {
    return apiFetch("/api/clubs");
  }

  function createClub(dto) {
    return apiFetch("/api/clubs", { method: "POST", body: dto });
  }

  function updateClub(id, dto) {
    return apiFetch("/api/clubs/" + id, { method: "PUT", body: dto });
  }

  function getUtilisateurs() {
    return apiFetch("/api/utilisateurs");
  }

  function createUtilisateur(dto) {
    return apiFetch("/api/utilisateurs", { method: "POST", body: dto });
  }

  function updateUtilisateur(id, dto) {
    return apiFetch("/api/utilisateurs/" + id, { method: "PUT", body: dto });
  }

  function deleteUtilisateur(id) {
    return apiFetch("/api/utilisateurs/" + id, { method: "DELETE" });
  }

  function getResultats() {
    return apiFetch("/api/resultats");
  }

  global.EstApi = {
    getToken: getToken,
    getUser: getUser,
    saveSession: saveSession,
    clearSession: clearSession,
    isSuperAdmin: isSuperAdmin,
    isClubAdmin: isClubAdmin,
    requireAuth: requireAuth,
    login: login,
    logout: logout,
    apiFetch: apiFetch,
    getStats: getStats,
    getNageurs: getNageurs,
    createNageur: createNageur,
    updateNageur: updateNageur,
    getEntraineurs: getEntraineurs,
    createEntraineur: createEntraineur,
    updateEntraineur: updateEntraineur,
    getReservations: getReservations,
    createReservation: createReservation,
    updateReservationStatut: updateReservationStatut,
    getSeances: getSeances,
    createSeance: createSeance,
    updateSeance: updateSeance,
    getPiscines: getPiscines,
    createPiscine: createPiscine,
    updatePiscine: updatePiscine,
    deletePiscine: deletePiscine,
    getClubs: getClubs,
    createClub: createClub,
    updateClub: updateClub,
    getUtilisateurs: getUtilisateurs,
    createUtilisateur: createUtilisateur,
    updateUtilisateur: updateUtilisateur,
    deleteUtilisateur: deleteUtilisateur,
    getResultats: getResultats,
  };
})(window);
