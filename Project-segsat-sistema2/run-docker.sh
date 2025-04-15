#!/bin/bash

echo "Iniciando o ambiente containerizado para o Sistema 2..."
echo "Construindo e iniciando os containers..."

docker-compose up -d

echo "Aguardando inicialização dos serviços..."
sleep 10

echo "Verificando status dos containers..."
docker-compose ps

echo "Logs da aplicação (pressione Ctrl+C para sair):"
docker-compose logs -f app 