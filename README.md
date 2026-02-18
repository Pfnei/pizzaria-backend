# Pizzeria Backend

## Setup

### Für Codespace

docker-compose -f docker-compose.codespace.yml --env-file .env.codespace up -d
docker logs -f pizzaria-backend-codespace


### Für lokale Entwicklung

docker compose up -d
docker logs -f pizzaria-backend


env dateien mal entfernt, jetzt ist das docker setup final für uns..^^

Situation,Befehl
Erster Start / Alles neu,docker compose up -d --build
Nur stoppen,docker compose stop
Alles löschen (auch DB-Daten),docker compose down -v
Logs anschauen (Fehlersuche),docker logs -f pizzaria-backend