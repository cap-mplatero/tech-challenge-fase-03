# Documentation Standards

## 1. Code Documentation

### Javadoc (Mandatory for public APIs)
```java
/**
 * Creates a new user in the system.
 *
 * @param request the user creation request containing name, email, and password
 * @return the created user response with generated ID
 * @throws EmailAlreadyExistsException if the email is already registered
 */
public UserResponse execute(CreateUserRequest request) { ... }
```

### When to Document
- All public classes and methods in `domain` and `application` layers
- All REST endpoints (via SpringDoc `@Operation` annotations)
- Complex business rules and domain logic
- Non-obvious design decisions (inline comments)

### When NOT to Document
- Getters/setters, constructors (self-explanatory)
- Private methods with clear names
- Test methods (use `@DisplayName` instead)

## 2. API Documentation (SpringDoc OpenAPI)

```java
@Operation(summary = "Create a new user", description = "Registers a new user in the system")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "User created"),
    @ApiResponse(responseCode = "400", description = "Invalid input"),
    @ApiResponse(responseCode = "409", description = "Email already exists")
})
@PostMapping("/api/v1/users")
public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) { ... }
```

## 3. README per Service

Each microservice must have a `README.md` with:
- Service description and responsibilities
- How to run locally (`mvn spring-boot:run`)
- Environment variables required
- API endpoints summary
- Database migrations info

## 4. Architecture Decision Records (ADRs)

Store in `docs/architecture/` using format:
- **Title:** Short descriptive name
- **Status:** Proposed / Accepted / Deprecated
- **Context:** Why this decision was needed
- **Decision:** What was decided
- **Consequences:** Trade-offs and impacts

## 5. Conventional Commits

Format: `type(scope): description`

Types: `feat`, `fix`, `refactor`, `docs`, `test`, `chore`, `ci`

Examples:
- `feat(user-service): add user creation endpoint`
- `fix(order-service): handle null payment status`
- `docs(payment-service): add API documentation`
