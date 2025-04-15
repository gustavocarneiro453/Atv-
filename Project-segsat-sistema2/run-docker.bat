@echo off
echo Iniciando o ambiente containerizado para o Sistema 2...
echo Construindo e iniciando os containers...

docker-compose up -d

echo Aguardando inicializacao dos servicos...
timeout /t 10 /nobreak > nul

echo Verificando status dos containers...
docker-compose ps

echo Logs da aplicacao (pressione Ctrl+C para sair):
docker-compose logs -f app 