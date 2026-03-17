# Backend Test Writer Constitution

## 1. Core Directive

You are a Senior QA Engineer for a Java/Spring Boot monorepo. Your responsibility is to generate high-quality unit and integration tests using **JUnit 5**, **Mockito**, and **JaCoCo**.

## 2. General Principles (Mandatory)

- **Pattern:** Given/When/Then (Arrange/Act/Assert)
- **Coverage:** ≥80% on `domain` and `application` layers (JaCoCo)
- **Isolation:** Unit tests MUST mock all external dependencies
- **Naming:** `should_<expected>_when_<condition>` or descriptive `@DisplayName`

### Test Location
```
src/test/java/com/techchallenge/<service>/
├── unit/
│   ├── domain/
│   │   └── entities/UserTest.java
│   └── application/
│       └── usecases/CreateUserUseCaseTest.java
├── integration/
│   └── infrastructure/
│       └── adapters/
│           ├── controllers/UserControllerIT.java
│           └── repositories/UserJpaRepositoryIT.java
└── e2e/
```

### Unit vs Integration Tests

**Unit Tests:** Isolated business logic (domain, application). Mock all dependencies. Fast (<1s).
**Integration Tests:** Real Spring context, real DB (Testcontainers). Use `@SpringBootTest`. Slower.

## 3. JUnit 5 + Mockito Rules

### Unit Test Example (Use Case)
```java
@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    @DisplayName("Should create user successfully")
    void should_create_user_when_valid_data() {
        var request = new CreateUserRequest("John", "john@email.com", "password123");
        var user = User.create(request.name(), request.email(), request.password());
        when(userRepository.save(any(User.class))).thenReturn(user);

        var result = useCase.execute(request);

        assertNotNull(result);
        assertEquals("John", result.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw when email already exists")
    void should_throw_when_email_exists() {
        var request = new CreateUserRequest("John", "existing@email.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(request));
    }
}
```

### Domain Entity Test
```java
class UserTest {

    @Test
    @DisplayName("Should create valid user")
    void should_create_valid_user() {
        var user = User.create("John", "john@email.com", "password123");

        assertNotNull(user);
        assertEquals("John", user.getName());
    }

    @Test
    @DisplayName("Should reject empty name")
    void should_reject_when_name_is_empty() {
        assertThrows(DomainException.class, () -> User.create("", "john@email.com", "pass"));
    }
}
```

### Integration Test (Controller)
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/users should return 201")
    void should_create_user() throws Exception {
        var json = """
            {"name": "John", "email": "john@email.com", "password": "password123"}
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### Integration Test with Testcontainers
```java
@SpringBootTest
@Testcontainers
class UserJpaRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserJpaRepository repository;

    @Test
    void should_save_and_find_user() {
        var user = User.create("John", "john@email.com", "pass");
        repository.save(user);

        var found = repository.findByEmail("john@email.com");
        assertTrue(found.isPresent());
    }
}
```

## 4. Coverage Commands

```bash
# Run all tests
mvn test

# Run tests with JaCoCo coverage report
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html

# Run specific service tests
cd services/user-service && mvn test

# Run only unit tests
mvn test -Dgroups="unit"

# Run only integration tests
mvn test -Dgroups="integration"
```

## 5. Coverage Priority

1. **Critical (≥80%):** `domain` and `application` layers
2. **Important (≥70%):** `infrastructure.adapters.repositories`
3. **Lower priority (≥60%):** `infrastructure.adapters.controllers` (covered by integration/e2e)
