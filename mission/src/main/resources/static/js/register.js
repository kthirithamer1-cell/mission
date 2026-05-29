document.addEventListener("DOMContentLoaded", () => {
  if (getToken()) {
    window.location.href = "/dashboard.html";
    return;
  }

  const form = document.getElementById("register-form");
  const submitBtn = document.getElementById("register-submit");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    hideAlert("register-alert");

    const nom = document.getElementById("nom").value.trim();
    const prenom = document.getElementById("prenom").value.trim();
    const email = document.getElementById("email").value.trim();
    const motDePasse = document.getElementById("password").value;
    const confirm = document.getElementById("confirm-password").value;
    const userType = document.getElementById("user-type").value;

    if (!nom || !prenom || !email || !motDePasse) {
      showAlert("register-alert", "Please fill in all required fields.");
      return;
    }

    if (motDePasse.length < 6) {
      showAlert("register-alert", "Password must be at least 6 characters.");
      return;
    }

    if (motDePasse !== confirm) {
      showAlert("register-alert", "Passwords do not match.");
      return;
    }

    submitBtn.disabled = true;
    submitBtn.innerHTML = '<span class="spinner"></span>Creating account…';

    const payload = {
      utilisateurDTO: { nom, prenom, email, motDePasse },
      userType,
    };

    try {
      await apiRequest("/api/auth/register", {
        method: "POST",
        body: JSON.stringify(payload),
      });

      showAlert(
        "register-alert",
        "Account created. You can sign in now.",
        "success"
      );
      setTimeout(() => {
        window.location.href = "/login.html";
      }, 1200);
    } catch (err) {
      showAlert("register-alert", err.message || "Registration failed.");
      submitBtn.disabled = false;
      submitBtn.textContent = "Create account";
    }
  });
});
