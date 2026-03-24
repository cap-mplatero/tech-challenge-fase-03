# Tech Challenge Fase 03

**Repositório:** https://github.com/cap-mplatero/tech-challenge-fase-03.git

Monorepo com 3 microserviços seguindo **Arquitetura Hexagonal (Ports and Adapters)**.

## Estrutura do Projeto

```
tech-challenge-fase-03/
├── services/
│   ├── user-service/            # Autenticação e gerenciamento de usuários
│   ├── order-service/           # Pedidos, clientes, restaurantes, cardápio
│   └── payment-service/         # Processamento e reprocessamento de pagamentos
├── infrastructure/
│   └── scripts/
│       └── init-databases.sql   # Cria os 3 databases no PostgreSQL
├── docs/
│   ├── architecture/            # Decisões arquiteturais
│   └── api/                     # Documentação de endpoints
├── tech-challenge-fase-03-collection.json  # Collection Postman
├── docker-compose.yml
└── .gitignore
```

## Arquitetura Hexagonal

Cada serviço segue:

```
service/src/main/java/.../
├── domain/                      # Entidades, exceptions (puro, sem Spring)
├── application/                 # Use cases, DTOs, ports (input/output)
└── infrastructure/              # Adapters (controllers, repositories, messaging, config)
```

**Regra de dependência:** `infrastructure → application → domain`

## Tecnologias

- Java 21 + Spring Boot 3.3.6
- PostgreSQL 15 (3 databases)
- Apache Kafka (comunicação assíncrona)
- Spring Security + JWT (autenticação e autorização por roles)
- Resilience4j (circuit breaker + retry)
- SpringDoc OpenAPI (Swagger)
- Docker + Docker Compose
- Maven

## Como Rodar

```bash
# Subir tudo (PostgreSQL, Kafka, Zookeeper + 3 serviços)
docker-compose up --build

# Ou subir só a infra e rodar os serviços localmente
docker-compose up postgres zookeeper kafka
cd services/user-service && mvn spring-boot:run
cd services/order-service && mvn spring-boot:run
cd services/payment-service && mvn spring-boot:run
```

## Serviços e Portas

| Serviço | Porta | Swagger |
|---------|-------|---------|
| user-service | 8081 | http://localhost:8081/swagger-ui.html |
| order-service | 8080 | http://localhost:8080/swagger-ui.html |
| payment-service | 8082 | http://localhost:8082/swagger-ui.html |

## Segurança e Roles

| Role | Permissões |
|------|-----------|
| `ROLE_CUSTOMER` | Criar pedidos, ver seus próprios pedidos, visualizar restaurantes e cardápios |
| `ROLE_RESTAURANT_OWNER` | CRUD de restaurantes e cardápios (apenas os seus), ver pedidos do seu restaurante, atualizar status de pedidos |
| `ROLE_ADMIN` | Acesso total, gerenciamento de clientes |

Cada restaurante é vinculado ao seu dono via `ownerId` (extraído do JWT). Donos só acessam seus próprios recursos.

## Endpoints

### user-service (`:8081`)

| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/auth/register` | Registrar usuário | Não |
| POST | `/auth/login` | Login (retorna JWT) | Não |
| GET | `/users` | Listar usuários | Sim |
| GET | `/users/{id}` | Buscar por ID | Sim |
| PUT | `/users/{id}` | Atualizar usuário | Sim |
| DELETE | `/users/{id}` | Deletar usuário | Sim |

### order-service (`:8080`)

| Método | Rota | Descrição | Role |
|--------|------|-----------|------|
| POST | `/api/restaurants` | Criar restaurante | RESTAURANT_OWNER |
| GET | `/api/restaurants` | Listar (owner vê só os seus) | Autenticado |
| PUT/DELETE | `/api/restaurants/{id}` | Atualizar/Deletar (valida ownership) | RESTAURANT_OWNER |
| POST | `/api/menu-items` | Criar item (valida ownership) | RESTAURANT_OWNER |
| GET | `/api/menu-items/**` | Listar itens | Autenticado |
| PUT/DELETE | `/api/menu-items/{id}` | Atualizar/Deletar (valida ownership) | RESTAURANT_OWNER |
| POST | `/api/orders` | Criar pedido (publica evento Kafka) | CUSTOMER |
| GET | `/api/orders` | Meus pedidos | Autenticado |
| GET | `/api/orders/restaurant/{id}` | Pedidos por restaurante (valida ownership) | RESTAURANT_OWNER |
| PATCH | `/api/orders/{id}/status` | Atualizar status | RESTAURANT_OWNER |
| CRUD | `/api/customers` | Gerenciar clientes | ADMIN |

### payment-service (`:8082`)

| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/api/payments` | Processar pagamento | Sim |

Pagamentos também são processados automaticamente via Kafka.

## Fluxo Kafka

```
order-service cria pedido → publica "order-created"
  → payment-service consome → processa pagamento (Resilience4j)
    → Sucesso: salva APPROVED → publica "payment-result"
    → Falha: salva PENDING → publica "payment-result"
      → Scheduler reprocessa PENDING a cada 60s
        → order-service consome "payment-result" → atualiza status do pedido
```

## Collection Postman

O arquivo `tech-challenge-fase-03-collection.json` contém todas as rotas com cenários de sucesso e erro. Tokens e IDs são salvos automaticamente via test scripts.

## Variáveis de Ambiente

| Variável | Default | Descrição |
|----------|---------|-----------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/{service}_db` | URL do banco |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Senha do banco |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:29092` | Endereço do Kafka |
| `JWT_SECRET` | (base64 default) | Chave secreta JWT (compartilhada entre serviços) |
| `JWT_EXPIRATION` | `86400000` | Expiração do token (ms) |
| `PAYMENT_EXTERNAL_URL` | `http://localhost:8089/requisicao` | URL do processador externo |
| `PAYMENT_RETRY_INTERVAL_MS` | `60000` | Intervalo de reprocessamento (ms) |
