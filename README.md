# 🍕 Pizzeria Backend

Ein leistungsstarkes Spring Boot Backend für ein Pizzeria-Bestellsystem.
Dieses Projekt dient als zentrale API für die Verwaltung von Produkten, Bestellungen und Benutzern.

---

## 🛠 Voraussetzungen & Tools

Bevor du startest, stelle sicher, dass du **Docker** installiert hast. Das Projekt wurde mit folgenden Tools entwickelt und getestet:

| Kategorie | Tool | Verwendung |
| :--- | :--- | :--- |
| **Laufzeit** | ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white) | Containerisierung von App & DB |
| **Backend** | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=flat&logo=openjdk&logoColor=white) | Spring Boot 3 (Java 21) |
| **IDE** | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=flat&logo=intellij-idea&logoColor=white) | Haupt-Entwicklungsumgebung |
| **IDE** | ![VS Code](https://img.shields.io/badge/VS%20Code-007ACC?style=flat&logo=visual-studio-code&logoColor=white) | Alternative für schnelle Fixes / WebStorm |
| **API Test** | ![Bruno](https://img.shields.io/badge/Bruno-ff4500?style=flat) | API Testing & Dokumentation |
| **Datenbank** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white) | Persistente Speicherung |

---

## 🚀 Setup & Start

Wir nutzen verschiedene Docker-Konfigurationen, je nachdem, wie du entwickeln möchtest.

### 1. Hybrid-Modus (Entwicklung in der IDE)
*Nutzt Docker nur für die Datenbank und Adminer. Das Backend wird direkt in der IDE (IntelliJ/Rider) gestartet.*

* **Start:** `docker compose up -d`
* **Backend-URL:** `http://localhost:8081`
* **Adminer:** `http://localhost:8085` (Postgres läuft auf Port `5433`)

### 2. Full Docker Modus (Standalone)
*Startet das komplette System inkl. Backend in Docker. Ideal für Tests ohne lokale Java-Installation.*

* **Start:** `docker compose -f docker-compose.full.yml up -d --build`
* **Backend-URL:** `http://localhost:8082`
* **Adminer:** `http://localhost:8086`
* **Logs verfolgen:** `docker logs -f pizzaria-backend-full`

### 3. Codespace (Cloud)
* **Start:** `docker compose -f docker-compose.codespace.yml up -d`

---

## 📖 API-Dokumentation (Swagger)

Das Projekt nutzt **SpringDoc OpenAPI**. Die Dokumentation wird automatisch generiert und erlaubt das direkte Testen der Endpunkte.



* **Swagger UI:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
* **OpenAPI JSON:** [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)

> **Hinweis:** Im IDE-Modus (Hybrid) musst du Port **8081** verwenden.

---

## 🧪 API-Tests mit Bruno

Die Test-Requests sind direkt im Repository eingecheckt (`/api-requests`), damit du sofort loslegen kannst.

1.  Installiere den [Bruno API Client](https://usebruno.com/).
2.  Klicke auf **"Open Collection"** und wähle den Ordner `api-requests/` in diesem Projekt.
3.  Wähle oben rechts das Environment **"Docker-Local"** (für Port 8082).
4.  **JWT-Auth:** Die Collection ist so vorkonfiguriert, dass nach einem erfolgreichen Login (`/auth/login`) der Token automatisch für alle geschützten Endpunkte genutzt wird.

---

## ⚙️ Wichtige Befehle

| Ziel | Befehl |
| :--- | :--- |
| **Alles neu bauen & starten** | `docker compose -f docker-compose.full.yml up -d --build` |
| **Logs live verfolgen** | `docker logs -f pizzaria-backend-full` |
| **Stoppen (Daten behalten)** | `docker compose -f docker-compose.full.yml stop` |
| **Beenden (Container löschen)** | `docker compose -f docker-compose.full.yml down` |
| **Hard Reset (Inkl. DB-Löschung)** | `docker compose -f docker-compose.full.yml down -v` |

---

## 🛡 Security & Architektur

* **Authentifizierung:** JWT (JSON Web Token) via `Authorization: Bearer <token>` Header.
* **Öffentliche Pfade:** `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, sowie Produkt-Listen (GET).
* **Datenbank:** PostgreSQL mit Hibernate/JPA.
* **Migrations:** Automatisches Schema-Setup via Hibernate `ddl-auto: create`.

---

> **Fehlerbehebung:** Falls die Swagger-UI nicht lädt, stelle sicher, dass du das Backend nach Code-Änderungen mit `--build` neu gestartet hast, damit die neuesten Security-Einstellungen im Container aktiv sind.
