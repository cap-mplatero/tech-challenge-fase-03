# Decisões Arquiteturais

## Arquitetura Hexagonal (Ports and Adapters)

Cada microserviço segue a Arquitetura Hexagonal com 3 camadas:

```
infrastructure → application → domain
```

### Domain
- Entidades de negócio puras (sem Spring)
- Exceptions de domínio
- Sem dependência de frameworks

### Application
- Use cases que orquestram lógica de domínio
- DTOs (request/response)
- Ports: interfaces de entrada (input) e saída (output)
- Sem dependência de infraestrutura

### Infrastructure
- Adapters: controllers, repositories JPA, Kafka producers/consumers, clientes externos
- Configurações Spring (Security, Beans)
- Implementa os ports definidos em application/domain

## Comunicação entre Serviços

### Síncrona
- REST API com Spring Web (comunicação cliente → serviço)
- JWT compartilhado entre os 3 serviços para autenticação

### Assíncrona
- Apache Kafka para comunicação entre order-service e payment-service
- Tópicos: `order-created`, `payment-result`

```
[order-service] --order-created--> [payment-service]
[payment-service] --payment-result--> [order-service]
```

## Banco de Dados

- PostgreSQL único com 3 databases isolados (`user_db`, `order_db`, `payment_db`)
- Cada serviço acessa apenas seu database (database-per-service pattern)
- Hibernate `ddl-auto: update` para criação automática de tabelas

## Segurança

- user-service gera tokens JWT (registro/login)
- order-service e payment-service apenas validam tokens
- Mesma chave secreta compartilhada via variável de ambiente `JWT_SECRET`
- Endpoints Swagger são públicos, demais requerem autenticação

## Resiliência

- Resilience4j no payment-service (adapter layer)
- Circuit Breaker: abre após 50% de falhas em janela de 10 chamadas
- Retry: até 3 tentativas com 1s de intervalo
- Fallback: lança `FallbackException` quando circuit breaker abre
- Scheduler: reprocessa pagamentos PENDING a cada 60s

## Decisões de Design

| Decisão | Justificativa |
|---------|---------------|
| Monorepo | Facilita desenvolvimento e CI/CD para equipe pequena |
| Kafka ao invés de REST síncrono | Desacoplamento entre order e payment, tolerância a falhas |
| JWT compartilhado | Simplicidade; em produção usaria um auth server centralizado |
| PostgreSQL único | Custo; em produção cada serviço teria sua instância |
| Hexagonal Architecture | Testabilidade, separação de concerns, facilidade de trocar adapters |
