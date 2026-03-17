# Documentação de APIs

## Autenticação

Todos os endpoints (exceto `/auth/**` e Swagger) requerem header:
```
Authorization: Bearer <token>
```

Obtenha o token via `POST /auth/login` no user-service.

---

## user-service (porta 8081)

### Auth
```
POST /auth/register
Body: { "name": "string", "email": "string", "password": "string (min 8)" }
Response 201: { "token": "jwt", "email": "string", "roles": ["ROLE_USER"] }

POST /auth/login
Body: { "email": "string", "password": "string" }
Response 200: { "token": "jwt", "email": "string", "roles": [...] }
```

### Users (requer JWT)
```
GET    /users          → Lista todos os usuários
GET    /users/{id}     → Busca por UUID
PUT    /users/{id}     → Atualiza (name, email, password - todos opcionais)
DELETE /users/{id}     → Remove usuário
```

---

## order-service (porta 8080)

Todos os endpoints requerem JWT.

### Orders
```
POST   /api/orders                      → Cria pedido (publica evento Kafka)
GET    /api/orders                      → Lista todos
GET    /api/orders/{id}                 → Busca por ID
GET    /api/orders/customer/{id}        → Pedidos por cliente
GET    /api/orders/status/{status}      → Pedidos por status (PENDING, CONFIRMED, PAYMENT_FAILED)
GET    /api/orders/restaurant/{id}      → Pedidos por restaurante
PUT    /api/orders/{id}                 → Atualiza pedido
PATCH  /api/orders/{id}/status          → Atualiza status { "status": "string" }
DELETE /api/orders/{id}                 → Remove pedido
```

### Customers
```
POST   /api/customers       → Cria cliente
GET    /api/customers        → Lista todos
GET    /api/customers/{id}   → Busca por ID
PUT    /api/customers/{id}   → Atualiza
DELETE /api/customers/{id}   → Remove
```

### Restaurants
```
POST   /api/restaurants       → Cria restaurante { "name", "address", "cuisineType" }
GET    /api/restaurants        → Lista todos
GET    /api/restaurants/{id}   → Busca por ID
PUT    /api/restaurants/{id}   → Atualiza
DELETE /api/restaurants/{id}   → Remove
```

### Menu Items
```
POST   /api/menu-items                       → Cria item { "restaurantId", "name", "quantity", "price" }
GET    /api/menu-items                        → Lista todos
GET    /api/menu-items/{id}                   → Busca por ID
GET    /api/menu-items/restaurant/{id}        → Itens por restaurante
PUT    /api/menu-items/{id}                   → Atualiza
DELETE /api/menu-items/{id}                   → Remove
```

---

## payment-service (porta 8082)

Requer JWT.

### Payments
```
POST /api/payments
Body: { "paymentId": "string", "orderId": "string", "amount": 1000 }
Response 200: { "status": "APPROVED", "message": "...", "paymentId": "...", "orderId": "..." }
Response 422: { "status": "REJECTED", "message": "...", ... }
```

### Processamento automático via Kafka
- Consome `order-created` → processa pagamento → publica `payment-result`
- Pagamentos com falha são salvos como `PENDING`
- Scheduler reprocessa pagamentos `PENDING` a cada 60 segundos

---

## Swagger UI

Acesse a documentação interativa:
- http://localhost:8081/swagger-ui.html (user-service)
- http://localhost:8080/swagger-ui.html (order-service)
- http://localhost:8082/swagger-ui.html (payment-service)
