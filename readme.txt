# Für Codespace
docker-compose -f docker-compose.codespace.yml --env-file .env.codespace up -d
docker logs -f pizzaria-backend-codespace

# Für lokal
docker-compose up -d
docker logs -f pizzaria-backend