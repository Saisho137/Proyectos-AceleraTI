# Proyectos AceleraTI

Repositorio centralizado con los proyectos prácticos desarrollados en el programa **AceleraTI**. Cada directorio es un proyecto independiente enfocado en una temática específica de desarrollo backend con Java.

---

## Proyectos

### Arquitectura y Patrones de Diseño

| Proyecto | Descripción | Tecnologías |
|---|---|---|
| [`arka-hexagonal-simple-enyoi`](./arka-hexagonal-simple-enyoi) | Sistema de gestión de inventario y órdenes implementando Arquitectura Hexagonal (Ports & Adapters) | Java 17, JPA/Hibernate, SQLite |
| [`docker-practice-java-scaffold-enyoi`](./docker-practice-java-scaffold-enyoi) | Scaffold base con Clean Architecture para proyectos Java containerizados | Java, Docker, Gradle, Clean Architecture |
| [`scaffold-bancolombia`](./scaffold-bancolombia) | Proyecto base usando el scaffold de Bancolombia con Clean Architecture | Java, Gradle, Clean Architecture |

### Pruebas y Calidad de Código

| Proyecto | Descripción | Tecnologías |
|---|---|---|
| [`unit-test-practice-enyoi`](./unit-test-practice-enyoi) | Guía práctica de pruebas unitarias desde conceptos básicos hasta patrones avanzados | Java, JUnit, Mockito, Clean Architecture |
| [`refactoring-solid-practice-enyoi`](./refactoring-solid-practice-enyoi) | Refactorización de código legacy aplicando principios SOLID con enfoque TDD inverso | Java, SOLID, TDD, JUnit |

### Inteligencia Artificial

| Proyecto | Descripción | Tecnologías |
|---|---|---|
| [`rag-query-ia-solid-enyoi`](./rag-query-ia-solid-enyoi) | Sistema RAG (Retrieval-Augmented Generation) construido desde cero con principios SOLID y Arquitectura Hexagonal | Java, IA Generativa, SOLID, Hexagonal |
| [`simple-chat-ia-solid-enyoi`](./simple-chat-ia-solid-enyoi) | Chat simple con IA generativa aplicando Arquitectura Hexagonal y TDD | Java, IA Generativa, Hexagonal, TDD |

### Infraestructura y Microservicios

| Proyecto | Descripción | Tecnologías |
|---|---|---|
| [`arka-system-simple-lab-enyoi`](./arka-system-simple-lab-enyoi) | Lab de microservicios reactivos con patrones DDD, SAGA (Coreografía), Circuit Breaker y Event-Driven | Java 17, Spring WebFlux, Kafka, Docker |
| [`localstack-compose-lab-enyoi`](./localstack-compose-lab-enyoi) | Lab de servicios AWS simulados localmente con LocalStack y Docker Compose | Docker, LocalStack, AWS (S3, SQS, Lambda, Secrets Manager) |

### Fundamentos

| Proyecto | Descripción | Tecnologías |
|---|---|---|
| [`rest-demo`](./rest-demo) | API REST demo con Spring Boot | Java, Spring Boot, Gradle |
| [`spring_data`](./spring_data) | Sistema de gestión educativa implementando Spring Data con arquitectura hexagonal y DDD | Java, Spring Boot, Spring Data, JPA |
| [`native-java`](./native-java) | Ejercicios básicos de Java nativo | Java |

---

## Cómo usar este repositorio

Cada proyecto es independiente y tiene su propio `README.md` con instrucciones de instalación y ejecución. Navega al directorio del proyecto que te interese para ver los detalles.

```bash
# Ejemplo: clonar y abrir un proyecto específico
git clone https://github.com/Saisho137/Proyectos-AceleraTI.git
cd Proyectos-AceleraTI/arka-hexagonal-simple-enyoi
```

## Requisitos Generales

- **Java 17+**
- **Gradle 8+** (la mayoría de proyectos incluyen Gradle Wrapper)
- **Docker** (para proyectos con microservicios e infraestructura)
- **IntelliJ IDEA** o cualquier IDE compatible con Java
