# Pizzeria Backend

## Setup

### Für Codespace

docker-compose -f docker-compose.codespace.yml --env-file .env.codespace up -d
docker logs -f pizzaria-backend-codespace


### Für lokale Entwicklung

docker compose up -d
docker logs -f pizzaria-backend

