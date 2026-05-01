# Refactoring SOLID Practice

**Proyecto educativo para practicar refactorización aplicando principios SOLID y patrones de diseño.**

Este proyecto utiliza un enfoque de **TDD inverso**: las pruebas unitarias ya están escritas como especificación del código ideal, y tu trabajo es refactorizar el código legacy para que las pruebas pasen.

---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Requisitos Previos](#requisitos-previos)
- [Guía de Inicio Rápido](#guía-de-inicio-rápido)
- [Ejercicios](#ejercicios)
- [Principios SOLID](#principios-solid)
- [Patrones de Diseño](#patrones-de-diseño)
- [Flujo de Trabajo Recomendado](#flujo-de-trabajo-recomendado)
- [Comandos Útiles](#comandos-útiles)
- [Evaluación y Cobertura](#evaluación-y-cobertura)

---

## Descripción General

### ¿Qué vas a aprender?

1. **Identificar código que viola SOLID**: Reconocer "code smells" y violaciones de principios
2. **Aplicar refactoring**: Transformar código legacy a código limpio
3. **Usar patrones de diseño**: Strategy, Factory, Repository, Observer, Adapter
4. **Escribir código testeable**: Aplicar inyección de dependencias y mocking

### Metodología

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        FLUJO DE TRABAJO                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   1. LEE el código LEGACY     →   Identifica qué principios viola           │
│      (src/main/java/*/legacy/)                                              │
│                                                                             │
│   2. LEE los TESTS            →   Entiende cómo debe ser el código          │
│      (src/test/java/*)            refactorizado (tu especificación)         │
│                                                                             │
│   3. CREA las clases          →   En el paquete 'refactored'                │
│      (src/main/java/*/refactored/)                                          │
│                                                                             │
│   4. EJECUTA los tests        →   ./gradlew testRefactoring --tests "..."   │
│                                                                             │
│   5. REPITE hasta que         →   Todos los tests deben pasar               │
│      todos pasen                                                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Requisitos Previos

- **Java 17+** instalado
- **IDE** (IntelliJ IDEA, VS Code con Extension Pack for Java, o Eclipse)
- Conocimientos básicos de:
  - Java (clases, interfaces, herencia, polimorfismo)
  - JUnit 5 y Mockito
  - Gradle

---

##  Guía de Inicio Rápido

### 1. Clonar y preparar

```bash
cd refactoring-solid-practice
```

### 2. Verificar que el build funciona

```bash
./gradlew test
```

## Ejercicios

### Resumen de Ejercicios

| # | Dominio | Principios SOLID | Patrones | Duración |
|---|---------|------------------|----------|----------|
| 1 | Sistema de Notificaciones | SRP, OCP, DIP | Strategy, Factory | ~1.5h |
| 2 | Procesamiento de Pedidos | SRP, OCP, LSP, DIP | Strategy, Repository, Factory | ~2h |
| 3 | Generación de Reportes | ISP, SRP, OCP, DIP | Adapter, Factory | ~1.5h |
| 4 | Sistema de Eventos | SRP, OCP, DIP | Observer | ~1.5h |

**Total estimado: ~8 horas** (2 clases de 3-4 horas)

---

### Ejercicio 1: Sistema de Notificaciones (EJEMPLO)

**Ubicación:** `exercise1_notifications/`

**Principios a aplicar:** SRP, OCP, DIP

**Patrones:** Strategy, Factory

#### El Problema

El código legacy (`legacy/NotificationService.java`) tiene una clase gigante que:
- Envía emails, SMS y push notifications
- Formatea mensajes
- Valida datos
- Guarda logs

Todo en una sola clase con un switch/case enorme.

#### Tu Tarea (EJEMPLO)

Crear en `refactored/`:

```
refactored/
├── NotificationSender.java        # Interface (Strategy)
├── EmailNotificationSender.java   # Implementación
├── SmsNotificationSender.java     # Implementación
├── PushNotificationSender.java    # Implementación
├── NotificationSenderFactory.java # Factory
├── NotificationResult.java        # Value Object
├── NotificationLogger.java        # Interface para logging
├── ConsoleNotificationLogger.java # Implementación
├── NotificationLogEntry.java      # Value Object
└── NotificationService.java       # Orquestador refactorizado
```
---

## Recursos Adicionales

### Libros
- "Clean Code" - Robert C. Martin
- "Refactoring" - Martin Fowler
- "Head First Design Patterns" - Freeman & Freeman

### Enlaces
- [SOLID Principles](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Refactoring Guru - Patterns](https://refactoring.guru/design-patterns)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)