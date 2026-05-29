# 🏊 Project Mission

A full-stack web application for managing swimming club operations — athletes, coaches, training sessions, and results.

---

## 📁 Project Structure

```
mission/
├── frontend/          # Angular 21 (TypeScript)
├── mission/           # Spring Boot 3.2 backend (Java 17)
├── database/          # SQL scripts for database setup
├── Project Report.pdf
└── README.md
```

---

## ⚙️ Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Frontend   | Angular 21, TypeScript, Chart.js  |
| Backend    | Spring Boot 3.2, Spring Security  |
| Database   | MySQL 8                           |
| Auth       | JWT (jjwt 0.11.5) + BCrypt        |
| Email      | Brevo SMTP (ex-Sendinblue)        |
| API Docs   | Swagger / SpringDoc OpenAPI       |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** (JDK)
- **Maven 3.8+**
- **Node.js 18+** & **npm**
- **MySQL 8**
- **Angular CLI** (`npm install -g @angular/cli`)

### 1. Database Setup

```bash
# Connect to MySQL and run the seed script
mysql -u root -p < database/init-data.sql
```

This creates the `projectmission` database with all tables and **demo accounts** (see below).

### 2. Backend

```bash
cd mission

# Set environment variables for email (optional for local dev)
export MAIL_USERNAME=your-brevo-username
export MAIL_PASSWORD=your-brevo-smtp-key

# Run
mvn spring-boot:run
```

The API starts at **http://localhost:8080**

> **API Docs:** http://localhost:8080/swagger-ui.html

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

The app opens at **http://localhost:4200**

---

## 🔑 Demo Accounts

All demo accounts are **pre-verified** — you can login immediately without email confirmation.

| Role           | Email                  | Password      |
|----------------|------------------------|---------------|
| 🔴 Admin      | `admin@mission.tn`     | `Admin@123`   |
| 🟡 Coach      | `coach@mission.tn`     | `Coach@123`   |
| 🟢 Swimmer    | `nageur@mission.tn`    | `Nageur@123`  |

> To create these accounts, run: `mysql -u root -p < database/init-data.sql`

### Login API Example

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@mission.tn","motDePasse":"Admin@123"}'
```

---

## 🔗 API Endpoints

### Authentication (`/api/auth`)

| Method | Endpoint                   | Description              | Auth    |
|--------|----------------------------|--------------------------|---------|
| POST   | `/api/auth/login`          | Login & get JWT token    | No      |
| POST   | `/api/auth/register`       | Create a new account     | No      |
| GET    | `/api/auth/verify-email`   | Verify email with token  | No      |
| POST   | `/api/auth/resend-verification` | Resend verification email | No |

### Users (`/api/utilisateurs`)

| Method | Endpoint                     | Description              | Auth    |
|--------|------------------------------|--------------------------|---------|
| GET    | `/api/utilisateurs`          | List all users           | JWT     |
| GET    | `/api/utilisateurs/{id}`     | Get user by ID           | JWT     |
| PUT    | `/api/utilisateurs/{id}`     | Update a user            | JWT     |
| DELETE | `/api/utilisateurs/{id}`     | Delete a user            | JWT     |

> **Note:** Include the JWT token in the `Authorization: Bearer <token>` header for protected endpoints.

---

## 🔒 Environment Variables

| Variable         | Default                   | Description                    |
|------------------|---------------------------|--------------------------------|
| `MAIL_HOST`      | `smtp-relay.brevo.com`    | SMTP server host               |
| `MAIL_PORT`      | `587`                     | SMTP server port               |
| `MAIL_USERNAME`  | *(required)*              | Brevo SMTP username            |
| `MAIL_PASSWORD`  | *(required)*              | Brevo SMTP API key             |
| `MAIL_FROM`      | `thamer.kthiri@esprit.tn` | Sender email address           |
| `FRONTEND_URL`   | `http://localhost:4200`   | Frontend URL (for email links) |

---

## 👥 User Roles

| Role           | Code          | Permissions                           |
|----------------|---------------|---------------------------------------|
| Admin          | `ADMIN`       | Full access — manage all users        |
| Coach          | `ENTRAINEUR`  | Manage training sessions & athletes   |
| Swimmer        | `NAGEUR`      | View own schedule & results           |

---

## 📝 License

This project is developed as part of a university project at [ESPRIT](https://esprit.tn).
