# Sistema de Gestão de Veículos

## Descrição

Este sistema é uma API para gestão de veículos com funcionalidades para consumir dados de um tópico Kafka, armazenar no PostgreSQL e disponibilizar endpoints REST para consulta e atualização.

## Estrutura do Sistema

O sistema segue a arquitetura em camadas MVC (Model-View-Controller) com as seguintes partes:

### 1. Controladores (Controllers)

#### VeiculoController
O `VeiculoController` é responsável por receber as requisições HTTP básicas:

- **Endpoint de Listagem Geral**: `/api/veiculos` - Retorna todos os veículos cadastrados
- **Endpoint de Listagem por Loja**: `/api/veiculos/loja/{loja}` - Filtra veículos de uma loja específica
- **Endpoint de Listagem por Anos**: `/api/veiculos/anos?anoInicio=X&anoFim=Y` - Filtra veículos por intervalo de anos
- **Endpoint de Listagem por Marca**: `/api/veiculos/marca/{marca}` - Filtra veículos por marca
- **Endpoint de Atualização**: `/api/veiculos/{id}` (PATCH) - Atualiza dados de um veículo específico

#### ConsultasController
O `ConsultasController` é especializado em consultas com queries SQL personalizadas:

- **Endpoint de Consulta por Loja**: `/api/consultas/loja/{loja}` - Filtra veículos de uma loja específica usando queries SQL
- **Endpoint de Consulta por Anos**: `/api/consultas/anos?anoInicio=X&anoFim=Y` - Filtra veículos por intervalo de anos usando queries SQL
- **Endpoint de Alteração de Dados**: `/api/consultas/alterar/{id}` (PATCH) - Atualiza dados de um veículo específico

### 2. Serviço (Service)

O `VeiculoService` contém a lógica de negócio e faz a ponte entre o controlador e o repositório:

- **listarTodos()**: Retorna todos os veículos do banco
- **listarPorLoja()**: Filtra veículos por loja usando query personalizada
- **listarPorIntervaloAnoModelo()**: Filtra veículos por intervalo de anos do modelo
- **listarPorMarca()**: Filtra veículos por marca
- **buscarPorId()**: Busca um veículo específico pelo ID
- **atualizar()**: Atualiza os dados de um veículo existente

### 3. Repositório (Repository)

O `VeiculoRepository` é responsável pela comunicação com o banco de dados:

- Estende `JpaRepository` para operações básicas de CRUD
- Contém métodos com `@Query` para consultas personalizadas SQL
- Implementa consultas específicas como busca por loja, marca e intervalo de anos

### 4. Modelo (Model)

A classe `Veiculo` representa a entidade principal do sistema com vários atributos:
- ID, marca, modelo, ano de fabricação, ano do modelo
- Placa, cor, loja, chassi, status
- Quilometragem, preço, combustível, transmissão, portas, condição

### 5. DTO (Data Transfer Object)

O `VeiculoUpdateDTO` é usado para transferir apenas os dados necessários para atualização, sem expor toda a entidade:
- Contém campos como marca, modelo e ano de fabricação

### 6. Integração com Kafka

Um componente `VeiculoConsumer` consome dados de um tópico Kafka e salva no banco de dados:
- Escuta o tópico "veiculos-adset"
- Converte as mensagens JSON em objetos `Veiculo`
- Persiste os dados no PostgreSQL

## Fluxo de Dados

1. **Recebimento de Dados**: O sistema recebe dados de veículos via Kafka
2. **Persistência**: Os dados são convertidos e salvos no PostgreSQL
3. **Consulta**: Clientes podem consultar os dados através dos endpoints REST
4. **Atualização**: Dados podem ser atualizados via endpoint PATCH

## Pré-requisitos

- Java 17+
- Maven
- PostgreSQL
- Docker e Docker Compose

## Executando o Sistema

### Usando Docker (Recomendado)

```bash
docker-compose up -d
```

Este comando inicia:
- PostgreSQL
- Zookeeper
- Kafka
- Aplicação Spring Boot

### Manualmente

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: http://localhost:8097

## Como Testar

Para testar os endpoints via terminal:

### Endpoints do VeiculoController

```bash
# Listar todos os veículos
curl -X GET "http://localhost:8097/api/veiculos"

# Listar por loja
curl -X GET "http://localhost:8097/api/veiculos/loja/PARVI%20FIORI%20FIAT%20-%20PARALELA%20%28BA%29"

# Listar por intervalo de anos
curl -X GET "http://localhost:8097/api/veiculos/anos?anoInicio=2020&anoFim=2023"

# Listar por marca
curl -X GET "http://localhost:8097/api/veiculos/marca/FIAT"

# Atualizar veículo
curl -X PATCH "http://localhost:8097/api/veiculos/1001" \
-H "Content-Type: application/json" \
-d "{\"marca\":\"FIAT\",\"modelo\":\"Uno Way\",\"anoFabricacao\":2022}"
```

### Endpoints do ConsultasController (com queries SQL personalizadas)

```bash
# Consultar por loja usando query SQL
curl -X GET "http://localhost:8097/api/consultas/loja/PARVI%20FIORI%20FIAT%20-%20PARALELA%20%28BA%29"

# Consultar por intervalo de anos usando query SQL
curl -X GET "http://localhost:8097/api/consultas/anos?anoInicio=2020&anoFim=2023"

# Alterar dados do veículo
curl -X PATCH "http://localhost:8097/api/consultas/alterar/1001" \
-H "Content-Type: application/json" \
-d "{\"marca\":\"FIAT\",\"modelo\":\"Uno Way\",\"anoFabricacao\":2022}"
```

## Ambiente Kafka

O sistema utiliza um ambiente Kafka configurado via docker-compose, que inclui:
- Zookeeper para gerenciar o cluster Kafka
- Broker Kafka para processamento de mensagens
- Interface Kafka UI para monitoramento

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.3.2
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"
    networks:
      - kafka-network

  kafka:
    image: confluentinc/cp-kafka:7.3.0
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:29092,PLAINTEXT_HOST://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    networks:
      - kafka-network

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-ui
    depends_on:
      - kafka
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:29092
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
    networks:
      - kafka-network

networks:
  kafka-network:
    driver: bridge
```

## Solução de Problemas

- **Porta em uso**: Se a porta 8097 estiver em uso, altere no arquivo `application.properties`
- **Erro de conexão com Kafka**: Verifique se os serviços Kafka estão rodando corretamente
- **Erro de banco de dados**: Verifique as credenciais do PostgreSQL 