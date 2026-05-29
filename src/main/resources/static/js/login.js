document.addEventListener("DOMContentLoaded", () => {
  if (getToken()) {
    window.location.href = "/dashboard.html";
    return;
  }

  const form = document.getElementById("login-form");
  const submitBtn = document.getElementById("login-submit");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    hideAlert("login-alert");

    const email = document.getElementById("email").value.trim();
    const motDePasse = document.getElementById("password").value;

    if (!email || !motDePasse) {
      showAlert("login-alert", "Please enter email and password.");
      return;
    }

    submitBtn.disabled = true;
    submitBtn.innerHTML = '<span class="spinner"></span>Signing in…';

    try {
      const data = await apiRequest("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, motDePasse }),
      });

      setSession(data.token, data.utilisateur);
      showAlert("login-alert", "Login successful. Redirecting…", "success");
      setTimeout(() => {
        window.location.href = "/dashboard.html";
      }, 600);
    } catch (err) {
      showAlert("login-alert", err.message || "Invalid credentials.");
      submitBtn.disabled = false;
      submitBtn.textContent = "Sign in";
    }
  });
});
