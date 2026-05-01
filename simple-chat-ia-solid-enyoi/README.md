# Simple Chat IA con Arquitectura Hexagonal

## Descripción

Este proyecto es una actividad práctica diseñada para aprender a desarrollar una aplicación de chat simple con inteligencia artificial generativa, siguiendo principios de diseño de software sólidos. El desarrollo se realiza mediante Test-Driven Development (TDD), donde las pruebas unitarias guían la implementación del código.

## Objetivos de Aprendizaje

- Implementar una aplicación siguiendo la Arquitectura Hexagonal (Ports and Adapters)
- Aplicar los principios SOLID de diseño orientado a objetos
- Integrar servicios de IA generativa (Google Gemini)
- Desarrollar guiado por pruebas (Test-Driven Development)
- Crear una aplicación de línea de comandos interactiva

## Arquitectura del Proyecto

El proyecto sigue la Arquitectura Hexagonal, organizando el código en tres capas principales:

### Domain (Núcleo del Negocio)
- `model/`: Modelos de dominio puros
  - `ChatMessage`: Representa un mensaje en la conversación
  - `Conversation`: Gestiona el contexto de la conversación
- `port/`: Interfaces que definen contratos
  - `ChatService`: Puerto de salida para servicios de chat

### Application (Casos de Uso)
- `SimpleChatUseCase`: Orquesta la lógica de negocio del chat

### Infrastructure (Adaptadores)
- `gemini/`: Implementación del adaptador para Google Gemini AI
- `main/`: Punto de entrada de la aplicación

## Principios SOLID Aplicados

- **Single Responsibility Principle**: Cada clase tiene una única responsabilidad bien definida
- **Open/Closed Principle**: El código está abierto a extensión pero cerrado a modificación
- **Liskov Substitution Principle**: Las implementaciones pueden sustituirse por sus interfaces
- **Interface Segregation Principle**: Interfaces específicas en lugar de interfaces generales
- **Dependency Inversion Principle**: Las dependencias apuntan hacia abstracciones, no implementaciones concretas

## Requisitos Previos

- Java 17 o superior
- Gradle 7.x o superior
- API Key de Google Gemini AI
- Git

## Configuración Inicial

1. Clona el repositorio:
```bash
git clone <url-del-repositorio>
cd simple-chat-ia-solid
```

2. Configura tu API Key de Google Gemini como variable de entorno:
```bash
export GEMINI_API_KEY="tu-api-key-aqui"
```

3. Verifica que el proyecto compile:
```bash
./gradlew build
```

## Desarrollo Guiado por Pruebas (TDD)

Este proyecto sigue la metodología TDD. Las pruebas ya están escritas pero comentadas. Tu tarea es:

1. **Descomenta las pruebas progresivamente**
2. **Implementa el código necesario para que las pruebas pasen**
3. **Refactoriza si es necesario**
4. **Repite el ciclo**

### Orden Sugerido de Implementación

#### Fase 1: Modelos de Dominio

1. **ChatMessage** (`src/test/java/com/enyoi/simplechatia/domain/model/ChatMessageTest.java`)
   - Implementa el enum `Role` (USER, ASSISTANT, SYSTEM)
   - Crea el record `ChatMessage` con: id, role, content, timestamp
   - Implementa métodos estáticos de fábrica: `userMessage()`, `assistantMessage()`

2. **Conversation** (`src/test/java/com/enyoi/simplechatia/domain/model/ConversationTest.java`)
   - Crea el record `Conversation` con: id, messages, systemPrompt
   - Implementa `getLastMessage()`
   - Implementa `addMessage()`
   - Implementa método de fábrica estático para crear nueva conversación

#### Fase 2: Caso de Uso

3. **SimpleChatUseCase** (`src/test/java/com/enyoi/simplechatia/application/SimpleChatUseCaseTest.java`)
   - Implementa validación de mensajes de usuario
   - Implementa gestión de conversación
   - Integra con el puerto `ChatService`
   - Maneja el flujo de mensajes usuario-asistente

#### Fase 3: Infraestructura

4. **GeminiChatService** (`src/test/java/com/enyoi/simplechatia/infrastructure/gemini/GeminiChatServiceTest.java`)
   - Implementa la interfaz `ChatService`
   - Configura el cliente de Google Gemini
   - Transforma mensajes del dominio a formato Gemini
   - Maneja respuestas de la API

5. **SimpleChatMain**
   - Configura la inyección de dependencias manual
   - Implementa el bucle interactivo de consola
   - Maneja entrada/salida del usuario

## Ejecutar las Pruebas

```bash
# Ejecutar todas las pruebas
./gradlew test

# Ver reporte de cobertura
./gradlew jacocoTestReport
# El reporte HTML estará en: build/reports/jacoco/test/html/index.html
```

## Ejecutar la Aplicación

Una vez implementado todo el código:

```bash
./gradlew runSimpleChat
```

O usando Gradle directamente:

```bash
./gradlew :run
```

## Estructura de Paquetes

```
com.enyoi.simplechatia
├── domain
│   ├── model
│   │   ├── ChatMessage.java
│   │   └── Conversation.java
│   └── port
│       └── ChatService.java
├── application
│   └── SimpleChatUseCase.java
├── infrastructure
│   └── gemini
│       └── GeminiChatService.java
└── main
    └── SimpleChatMain.java
```

## Dependencias Principales

- **Google GenAI SDK**: Cliente oficial para Google Gemini
- **JUnit 5**: Framework de pruebas
- **Mockito**: Biblioteca de mocking para pruebas
- **AssertJ**: Aserciones fluidas para pruebas
- **SLF4J + Logback**: Sistema de logging

## Criterios de Éxito

Tu implementación debe:

1. Pasar todas las pruebas unitarias
2. Alcanzar al menos 80% de cobertura de código
3. Seguir los principios SOLID documentados
4. Mantener la separación clara entre capas (Hexagonal Architecture)
5. Funcionar correctamente en la terminal con interacción usuario-IA

## Recursos Adicionales

- [Arquitectura Hexagonal explicada](https://alistair.cockburn.us/hexagonal-architecture/)
- [Principios SOLID](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Google Gemini API Documentation](https://ai.google.dev/gemini-api/docs)
- [Test-Driven Development](https://martinfowler.com/bliki/TestDrivenDevelopment.html)

## Notas Importantes

- No modifiques las pruebas existentes
- Asegúrate de que tu API Key de Gemini esté configurada correctamente
- Las pruebas están comentadas para que las descomentes progresivamente
- Cada fase construye sobre la anterior, sigue el orden sugerido
- Realiza commits frecuentes con mensajes descriptivos

## Soporte

Si encuentras problemas durante el desarrollo:

1. Revisa que todas las dependencias estén instaladas correctamente
2. Verifica que tu API Key esté configurada
3. Consulta los mensajes de error de las pruebas
4. Revisa la estructura de archivos esperada

## Licencia

Este proyecto es material educativo para fines de aprendizaje.
