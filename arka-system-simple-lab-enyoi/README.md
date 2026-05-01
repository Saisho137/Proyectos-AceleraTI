# Arka — Lab Microservicios Reactivos

Laboratorio práctico de microservicios reactivos con **Java 17 + Spring Boot (WebFlux)**, aplicando patrones de arquitectura distribuida como **DDD**, **SAGA (Coreografía)**, **Circuit Breaker** y **Event-Driven Architecture** con Kafka.

## Stack Tecnológico

| Componente      | Tecnología                                                                 |
| --------------- | -------------------------------------------------------------------------- |
| Framework       | Spring Boot 3 (WebFlux) — Reactive Stack                                   |
| Arquitectura    | Clean Architecture (Scaffold Bancolombia)                                  |
| Base de Datos   | PostgreSQL (R2DBC) — Database per Service                                  |
| Mensajería      | Apache Kafka (KRaft Mode)                                                  |
| Infraestructura | Docker Compose + LocalStack (Secrets Manager, API Gateway, CloudFormation) |
| Resiliencia     | Resilience4j (Circuit Breaker)                                             |
| Balanceo        | Traefik                                                                    |

## Microservicios

- **ms-orders** — Orquestador / Iniciador de la SAGA
- **ms-inventory** — Gestión de stock reactiva (escalable con Consumer Groups)
- **ms-payment** — Simulador de pagos (falla aleatoriamente para probar compensación)

## Guía del Laboratorio

Sigue el workshop paso a paso aquí:

**[Acceder al Lab](https://manulasker.github.io/enyoi_java_slides/lab_2_arka_microservicios_reactivos/)**
