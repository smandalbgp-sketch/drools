# Drools + KIE + Spring Boot: Provident Fund demo

Spring Boot 2.7.18, Java 17+, Drools / KIE 7.72.0.Final.

## Run

```
mvn spring-boot:run
```

The app starts on port 8081. HTTP Basic users (in memory, see `SecurityConfig`):

| User  | Password  | Role  |
|-------|-----------|-------|
| user  | user123   | user  |
| admin | admin123  | admin |

## Endpoints (all POST)

| Path                    | Access                  |
|-------------------------|-------------------------|
| `/provident-cut-user`   | role `user`             |
| `/provident-cut-general`| any authenticated user  |
| `/provident-cut-admin`  | role `admin`            |

```
curl -u user:user123 -X POST http://localhost:8081/provident-cut-general \
  -H "Content-Type: application/json" \
  -d '{"department":"it","experience":5}'
```

Response:

```
{"responseCode":"200","responseMessage":"Success",
 "response":{"department":"IT","experience":5,"providentCut":"7% of gross salary"}}
```

## Rules (`src/main/resources/rules/provident-fund-category.drl`)

| Experience (years) | Provident cut          |
|--------------------|------------------------|
| 0 to 2             | 5% of basic salary     |
| 3 to 4             | 6% of basic salary     |
| 5 to 6             | 7% of gross salary     |
| more than 6        | 9% of gross salary     |

## How it fits together

- `DroolsConfig` builds a `KieContainer` bean from the `.drl` file.
- `UserServiceImpl` creates a `KieSession`, sets the `response` global, inserts the
  `ProvidentFundRequest` fact, fires the rules and disposes the session.
- `ProvidentFundRulesTest` covers every rule tier, validation and the role checks.

## Note on Keycloak

The original screenshots use `keycloak-spring-boot-starter` for security. That adapter
needs a running Keycloak server, so this project uses simple in-memory Basic auth with the
same role names (`user`, `admin`). Swap `SecurityConfig` if you want Keycloak back.
