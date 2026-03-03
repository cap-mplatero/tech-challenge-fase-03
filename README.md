# Tech Challenge Fase 03 - Monorepo

Monorepo contendo os microserviços do sistema de pedidos seguindo **Arquitetura Hexagonal (Ports and Adapters)**.

## 📁 Estrutura do Projeto

```
tech-challenge-fase-03/
├── services/                    # Microserviços
│   ├── user-service/           # Gerenciamento de usuários
│   ├── order-service/          # Gerenciamento de pedidos
│   └── payment-service/        # Processamento de pagamentos
├── packages/                    # Pacotes compartilhados
│   ├── shared/                 # Código compartilhado entre serviços
│   └── types/                  # Tipos TypeScript compartilhados
├── infrastructure/             # Infraestrutura como código
│   ├── docker/                 # Dockerfiles e docker-compose
│   ├── kubernetes/             # Manifestos Kubernetes
│   ├── terraform/              # Provisionamento de infraestrutura
│   └── scripts/                # Scripts de automação
├── docs/                       # Documentação
│   ├── architecture/           # Diagramas e decisões arquiteturais
│   ├── api/                    # Documentação de APIs
│   └── diagrams/               # Diagramas do sistema
└── .github/                    # GitHub Actions workflows
    └── workflows/
```

## 🏗️ Arquitetura Hexagonal

Cada microserviço segue a estrutura da Arquitetura Hexagonal:

```
service/
├── src/
│   ├── domain/                 # Camada de Domínio (núcleo)
│   │   ├── entities/          # Entidades de negócio
│   │   ├── value-objects/     # Objetos de valor
│   │   ├── repositories/      # Interfaces de repositórios (ports)
│   │   └── services/          # Serviços de domínio
│   ├── application/            # Camada de Aplicação
│   │   ├── use-cases/         # Casos de uso
│   │   ├── dtos/              # Data Transfer Objects
│   │   └── ports/             # Portas de entrada/saída
│   ├── infrastructure/         # Camada de Infraestrutura (adapters)
│   │   ├── adapters/
│   │   │   ├── controllers/   # Controladores HTTP
│   │   │   ├── repositories/  # Implementação de repositórios
│   │   │   ├── messaging/     # Mensageria (Kafka, RabbitMQ)
│   │   │   └── external/      # Integrações externas
│   │   ├── config/            # Configurações
│   │   └── database/          # Migrations e seeds
│   └── presentation/           # Camada de Apresentação
│       ├── routes/            # Definição de rotas
│       ├── middlewares/       # Middlewares
│       └── validators/        # Validadores de entrada
└── tests/
    ├── unit/                  # Testes unitários
    ├── integration/           # Testes de integração
    └── e2e/                   # Testes end-to-end
```

## 🎯 Microserviços

### 1. User Service
Responsável pelo gerenciamento de usuários do sistema.

**Responsabilidades:**
- Cadastro e autenticação de usuários
- Gerenciamento de perfis
- Controle de permissões

### 2. Order Service
Responsável pelo gerenciamento de pedidos.

**Responsabilidades:**
- Criação de pedidos
- Consulta de pedidos
- Atualização de status de pedidos
- Gerenciamento do ciclo de vida do pedido

### 3. Payment Service
Responsável pelo processamento de pagamentos.

**Responsabilidades:**
- Processamento de pagamentos
- Integração com gateways de pagamento
- Gerenciamento de status de pagamento
- Reprocessamento automático de pagamentos pendentes

## 🚀 Tecnologias

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.3.6
- **Arquitetura:** Hexagonal (Ports and Adapters)
- **ORM:** Hibernate (Spring Data JPA)
- **Documentação API:** Swagger/OpenAPI (SpringDoc)
- **Comunicação:** REST API + Mensageria (Kafka)
- **Banco de Dados:** PostgreSQL
- **Build:** Maven
- **Containerização:** Docker
- **Orquestração:** Kubernetes
- **IaC:** Terraform
- **CI/CD:** GitHub Actions
- **Testes:** JUnit 5, Mockito, JaCoCo

## 📋 Requisitos

- Java 21
- Maven 3.8+
- Docker >= 20.x
- Kubernetes >= 1.25
- Terraform >= 1.5
- PostgreSQL 15+

## 🛠️ Como Começar

```bash
# Clonar o repositório
git clone <repository-url>

# Configurar variáveis de ambiente
cp .env.example .env

# Subir ambiente de desenvolvimento com Docker
docker-compose up -d

# Build de todos os serviços
mvn clean install

# Executar um serviço específico
cd services/user-service
mvn spring-boot:run

# Ou executar todos via Docker Compose
docker-compose up --build
```

## 📚 Documentação

- [Arquitetura do Sistema](./docs/architecture/README.md)
- [Documentação de APIs](./docs/api/README.md)
- [Diagramas](./docs/diagrams/README.md)

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Testes com coverage
mvn clean test jacoco:report

# Ver relatório de coverage
open target/site/jacoco/index.html

# Executar testes de um serviço específico
cd services/user-service
mvn test
```

## 📦 Build e Deploy

```bash
# Build de todos os serviços
mvn clean package

# Build de um serviço específico
cd services/user-service && mvn clean package

# Gerar imagem Docker
docker build -t user-service:latest services/user-service

# Deploy para ambiente de desenvolvimento
kubectl apply -f infrastructure/kubernetes/overlays/dev

# Deploy para produção
kubectl apply -f infrastructure/kubernetes/overlays/prod
```

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
2. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
3. Push para a branch (`git push origin feature/AmazingFeature`)
4. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.
