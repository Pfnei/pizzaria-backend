# Pizzeria Backend 

## 🛠 Setup & Start

Wir nutzen verschiedene Docker-Konfigurationen, je nachdem, wie du entwickeln möchtest.

### 1. Für Kollegen (Entwicklung in der IDE)
*Nutzt Docker nur für die Datenbank und Adminer. Das Backend wird in IntelliJ/Rider gestartet.*

* **Start:** `docker compose up -d`
* **Backend-URL:** `http://localhost:8081` (via IDE)
* **Adminer:** `http://localhost:8085`

---

### 2. Für dich (Full Docker - Ohne IDE)
*Startet alles (DB, Adminer & Backend) komplett in Docker. Das Backend nutzt Port **8082**, um Konflikte mit der IDE zu vermeiden.*

* **Start:** `docker compose -f docker-compose.full.yml up -d --build`
* **Logs schauen:** `docker logs -f pizzaria-backend-full`
* **Backend-URL:** `http://localhost:8082`
* **Adminer:** `http://localhost:8086`

---

### 3. Für Codespace
*Spezielle Konfiguration für die Cloud-Umgebung.*

* **Start:** `docker compose -f docker-compose.codespace.yml up -d`
* **Logs:** `docker logs -f pizzaria-backend-codespace`

---

## Befehls-Übersicht

| Situation | Befehl |
| :--- | :--- |
| **Alles neu bauen & starten** | `docker compose -f docker-compose.full.yml up -d --build` |
| **Nur stoppen (Pause)** | `docker compose -f docker-compose.full.yml stop` |
| **Beenden & Aufräumen** | `docker compose -f docker-compose.full.yml down` |
| **Alles löschen (inkl. DB-Daten)** | `docker compose -f docker-compose.full.yml down -v` |
| **Logs live verfolgen** | `docker logs -f pizzaria-backend-full` |

> **Hinweis:** Wenn du zwischen IDE-Modus und Full-Docker-Modus wechselst, empfiehlt es sich, vorher einmal `docker system prune` auszuführen, um Netzwerk-Konflikte zu vermeiden.
