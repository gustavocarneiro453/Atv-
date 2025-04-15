# Sistema de Integração AdSet com Kafka

## 📋 Visão Geral
Este sistema realiza a integração entre a API AdSet e o Kafka, sincronizando dados de veículos e disponibilizando-os para consumo por outros sistemas. O sistema é desenvolvido em Java 17 utilizando Spring Boot e Spring Kafka.

## 🚀 Funcionalidades Principais

- **Sincronização Automática**: Agendamento de sincronização com a API AdSet em horários específicos (22h, 00h, 02h, 04h e 06h)
- **Sincronização Manual**: Endpoint REST para sincronização sob demanda
- **Processamento Individual**: Capacidade de processar veículos individualmente
- **Integração com Kafka**: Transmissão de dados para tópicos Kafka para consumo por outros sistemas
- **Tratamento de Erros**: Sistema robusto de tratamento de erros e logging

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Kafka
- Docker e Docker Compose
- Maven
- Lombok
- Jackson

## 📦 Pré-requisitos

- Java 17 instalado
- Maven instalado
- Docker e Docker Compose instalados
- Acesso à API AdSet (credenciais necessárias)

## 🔧 Configuração

### Propriedades da Aplicação
As configurações principais estão em `src/main/resources/application.properties`:

```properties
# Servidor
server.port=8099

# API AdSet
app.adset.api-url=https://www.adset.com.br/integrador/api/estoqueveiculos
app.adset.email=anuncios@segsat.com
app.adset.senha=SeSaT%9
app.adset.cnpj=35715234001414

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
app.kafka.topic-name=veiculos-adset
```

## 🚀 Início Rápido

1. Clone o repositório
2. Inicie o ambiente Kafka:
   ```bash
   docker-compose up -d
   ```
3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acesse a aplicação em: [http://localhost:8099](http://localhost:8099)

## 📡 Endpoints da API

### Sincronização com AdSet
```http
POST /api/adset/sincronizar
```
Resposta: Lista de veículos obtidos da API.

### Processamento de Veículo Individual
```http
POST /api/adset/veiculo
Content-Type: application/json

{
  "Id": 123,
  "Marca": "Toyota",
  "Modelo": "Corolla",
  "AnoFabricacao": 2023,
  "Preco": 120000.0
}
```

## 🔄 Fluxo de Dados

1. **Conexão com API**: A aplicação se conecta à API AdSet utilizando o formato JSON específico
2. **Processamento**: Os dados recebidos são convertidos para objetos Veiculo
3. **Publicação**: Cada veículo é publicado no tópico Kafka
4. **Disponibilização**: Os dados ficam disponíveis para consumo por outros sistemas

## 📊 Estrutura do Projeto

```
src/main/java/teste/Project_segsat_sistema1/
├── config/              # Configurações (Kafka, Rest, etc.)
├── controller/          # APIs REST
├── dto/                 # Objetos de transferência de dados
├── model/               # Modelos de domínio
├── schedule/            # Agendadores de tarefas
├── service/             # Lógica de negócio
└── ProjectSegsatSistema1Application.java
```

## 🔒 Segurança

- As credenciais da API AdSet são configuradas no arquivo `application.properties`
- Recomenda-se utilizar variáveis de ambiente ou um sistema de gerenciamento de segredos em produção

## 📝 Logs

O sistema utiliza SLF4J para logging, com configurações em:
```properties
logging.level.teste.Project_segsat_sistema1=INFO
logging.level.org.apache.kafka=WARN
```

## 🐛 Testes

O projeto inclui testes básicos para:
- Produção de mensagens Kafka
- Carga do contexto da aplicação

Para executar os testes:
```bash
./mvnw test
```

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

Para suporte, entre em contato com:
- Email: suporte@segsat.com
- Telefone: (XX) XXXX-XXXX 