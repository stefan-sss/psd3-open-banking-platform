## API Gateway

API Gateway reprezintă punctul unic de intrare în platformă. 
Acesta expune API-urile publice, aplică responsabilități transversale precum autentificare, 
correlation ID, request logging, rate limiting și error normalization, 
apoi rutează cererile către serviciile interne corespunzătoare.

### Responsibilities

- punct unic de intrare pentru acces extern
- rutarea request-urilor către serviciile interne
- validarea și propagarea token-urilor
- propagarea correlation ID-ului
- logarea request-urilor și a răspunsurilor
- limitarea de request-uri (rate limiting)
- răspunsuri de eroare unificate
- endpoint-uri pentru health și observabilita

Implementare Resilience4j Circuit Breaker pentru a proteja rutele către serviciile interne. 
Pentru fiecare rută critică există un circuit breaker dedicat și un fallback endpoint intern care întoarce un răspuns standardizat 503 Service Unavailable.

### Internal Routing

- /api/consents/** → consent-service
- /api/payments/** → payment-initiation-service
- /api/accounts/** → account-information-service
- /api/fraud/**    → fraud-detection-service
- /api/disputes/** → dispute-management-service

### Structura proiectului: 

```text
api-gateway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ro/stefan/gateway/
│   │   │       ├── config/
│   │   │       │   ├── GatewayRoutesConfig.java
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   ├── WebClientConfig.java
│   │   │       │   └── OpenApiConfig.java
│   │   │       │
│   │   │       ├── filter/
│   │   │       │   ├── CorrelationIdFilter.java
│   │   │       │   ├── RequestLoggingFilter.java
│   │   │       │   ├── AuthTokenRelayFilter.java
│   │   │       │   └── RateLimitFilter.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── GatewayFallbackController.java
│   │   │       │   └── HealthController.java
│   │   │       │
│   │   │       ├── client/
│   │   │       │   ├── ConsentServiceClient.java
│   │   │       │   ├── PaymentServiceClient.java
│   │   │       │   ├── AccountServiceClient.java
│   │   │       │   ├── FraudServiceClient.java
│   │   │       │   └── DisputeServiceClient.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── ApiErrorResponse.java
│   │   │       │   └── GatewayHealthResponse.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── GatewayExceptionHandler.java
│   │   │       │   ├── DownstreamServiceException.java
│   │   │       │   └── UnauthorizedGatewayException.java
│   │   │       │
│   │   │       └── ApiGatewayApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── logback-spring.xml
│
└── pom.xml