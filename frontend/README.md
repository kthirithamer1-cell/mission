# Project Mission — Angular Frontend

Angular app for login, registration, and dashboard connected to the Spring Boot API.

The original static pages (`login.html`, `register.html`, `dashboard.html`, `css/auth.css`, `js/`) are kept in `mission/src/main/resources/static/` and served by Spring Boot at http://localhost:8080.

## Prerequisites

- Node.js 20+
- Spring Boot backend running on `http://localhost:8080`
- MySQL database configured in `mission/src/main/resources/application.properties`

## Development

```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200

The dev server proxies `/api` to the backend (`proxy.conf.json`).

## Routes

| Path | Description |
|------|-------------|
| `/login` | Sign in (auth.css — simple form) |
| `/register` | Create account |
| `/dashboard` | EST admin dashboard (design from `miss/`) |
| `/athletes` | Athlètes du club |
| `/entraineurs` | Entraîneurs |
| `/reservations` | Réservations couloirs |
| `/resultats` | Compétitions & résultats |

Design files copied from `miss/mission/src/main/resources/static/css/admin-dashboard.css`.

## Production build

```bash
npm run build
```

Output is in `dist/frontend/browser/`. Serve with any static host and set `apiUrl` in `src/environments/environment.prod.ts`.

## Tests

```bash
npm test
```
