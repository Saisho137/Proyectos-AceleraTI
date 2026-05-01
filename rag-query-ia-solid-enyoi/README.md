# RAG Query IA - Práctica de Java con IA

> **Implementación de un sistema RAG (Retrieval-Augmented Generation) desde cero usando principios SOLID y Arquitectura Hexagonal**

Este proyecto es una práctica educativa diseñada para estudiantes de Java que desean aprender sobre:
- Inteligencia Artificial aplicada (RAG, Embeddings, LLMs)
- Principios SOLID
- Arquitectura Hexagonal (Ports & Adapters)
- Testing con JUnit 5 y Mockito
- Desarrollo guiado por pruebas (TDD)

---

## Tabla de Contenidos

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Arquitectura](#-arquitectura)
- [Requisitos Previos](#-requisitos-previos)
- [Configuración](#-configuración)
- [Instrucciones de la Práctica](#-instrucciones-de-la-práctica)
- [Estructura de Tareas](#-estructura-de-tareas)
- [Cómo Ejecutar](#-cómo-ejecutar)
- [Recursos de Aprendizaje](#-recursos-de-aprendizaje)

---

## Descripción del Proyecto

Este proyecto implementa un sistema **RAG (Retrieval-Augmented Generation)** simple que permite:

1. **Almacenar documentos** con sus embeddings vectoriales
2. **Buscar documentos relevantes** usando similitud coseno
3. **Generar respuestas** contextualizadas usando Google Gemini

### ¿Qué es RAG?

RAG es una técnica que combina:
- **Retrieval**: Búsqueda de información relevante en una base de conocimiento
- **Augmented**: Aumenta el prompt del LLM con contexto relevante
- **Generation**: El LLM genera respuestas basadas en el contexto proporcionado

```
┌─────────────────────────────────────────────────────────────────────┐
│                        FLUJO RAG                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   Usuario          Sistema RAG              Base de Conocimiento    │
│      │                  │                          │                │
│      │   Pregunta       │                          │                │
│      │─────────────────>│                          │                │
│      │                  │                          │                │
│      │                  │  1. Generar Embedding    │                │
│      │                  │─────────────────────────>│                │
│      │                  │                          │                │
│      │                  │  2. Buscar Similares     │                │
│      │                  │<─────────────────────────│                │
│      │                  │                          │                │
│      │                  │  3. Construir Contexto   │                │
│      │                  │                          │                │
│      │                  │  4. Prompt + Contexto    │                │
│      │                  │─────────────────────────>│  LLM (Gemini)  │
│      │                  │                          │                │
│      │   Respuesta      │  5. Respuesta Generada   │                │
│      │<─────────────────│<─────────────────────────│                │
│      │                  │                          │                │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Arquitectura

El proyecto sigue los principios de **Arquitectura Hexagonal** (Ports & Adapters):

```
src/main/java/com/enyoi/ragquery/ia/
├── domain/                    # Núcleo del dominio
│   ├── model/
│   │   └── Document.java      # Entidad de dominio
│   └── port/
│       ├── ChatService.java       # Puerto para chat con LLM
│       ├── EmbeddingService.java  # Puerto para embeddings
│       └── VectorStore.java       # Puerto para almacenamiento vectorial
│
├── application/               # Casos de uso
│   └── RAGQueryUseCase.java   # Orquestador del flujo RAG
│
├── infrastructure/            # Adaptadores
│   ├── gemini/
│   │   ├── GeminiClientWrapper.java    # Wrapper del cliente Gemini
│   │   ├── GeminiChatService.java      # Implementación ChatService
│   │   └── GeminiEmbeddingService.java # Implementación EmbeddingService
│   ├── memory/
│   │   └── InMemoryVectorStore.java    # Implementación VectorStore
│   └── data/
│       └── SolidArchitectureDocuments.java # Documentos de ejemplo
│
└── main/                      # Punto de entrada
    └── RAGMain.java           # Aplicación principal
```

### Diagrama de Dependencias

```
┌─────────────────────────────────────────────────────────────────────┐
│                         ARQUITECTURA HEXAGONAL                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌───────────────────────────────────────────────────────────┐     │
│   │                    INFRASTRUCTURE                          │     │
│   │  ┌─────────────────┐  ┌─────────────────────────────────┐ │     │
│   │  │  GeminiChat     │  │     InMemoryVectorStore         │ │     │
│   │  │  Service        │  │                                 │ │     │
│   │  └────────┬────────┘  └───────────────┬─────────────────┘ │     │
│   │           │                           │                   │     │
│   │  ┌────────┴────────┐  ┌───────────────┴─────────────────┐ │     │
│   │  │ GeminiEmbedding │  │                                 │ │     │
│   │  │    Service      │  │                                 │ │     │
│   │  └────────┬────────┘  └─────────────────────────────────┘ │     │
│   └───────────┼───────────────────────────┼───────────────────┘     │
│               │                           │                         │
│               ▼                           ▼                         │
│   ┌───────────────────────────────────────────────────────────┐     │
│   │                       DOMAIN (PORTS)                       │     │
│   │  ┌─────────────────┐ ┌─────────────┐ ┌──────────────────┐ │     │
│   │  │  ChatService    │ │EmbeddingServ│ │   VectorStore    │ │     │
│   │  │   (interface)   │ │ (interface) │ │   (interface)    │ │     │
│   │  └────────┬────────┘ └──────┬──────┘ └────────┬─────────┘ │     │
│   │           │                 │                 │           │     │
│   │           └─────────────────┼─────────────────┘           │     │
│   │                             │                             │     │
│   │                             ▼                             │     │
│   │           ┌─────────────────────────────────┐             │     │
│   │           │          Document               │             │     │
│   │           │          (model)                │             │     │
│   │           └─────────────────────────────────┘             │     │
│   └───────────────────────────────────────────────────────────┘     │
│                                 │                                   │
│                                 ▼                                   │
│   ┌───────────────────────────────────────────────────────────┐     │
│   │                      APPLICATION                           │     │
│   │           ┌─────────────────────────────────┐             │     │
│   │           │       RAGQueryUseCase           │             │     │
│   │           │  (orquesta todo el flujo RAG)   │             │     │
│   │           └─────────────────────────────────┘             │     │
│   └───────────────────────────────────────────────────────────┘     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📌 Requisitos Previos

- **Java 17** o superior
- **Gradle** (incluido wrapper)
- **API Key de Google AI (Gemini)** - [Obtener aquí](https://aistudio.google.com/app/apikey)
- IDE recomendado: IntelliJ IDEA o VS Code con extensiones Java

---

## ⚙ Configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd rag-query-ia-solid
```

### 2. Configurar API Key de Gemini

```bash
export GOOGLE_API_KEY=tu_api_key_aqui
```

### 3. Verificar la instalación

```bash
./gradlew build
```

---

## Instrucciones de la Práctica

Esta práctica está diseñada con un enfoque **TDD (Test-Driven Development)**:

### 🔴 Modalidad 1: Implementar Clases (Tests ya existen)

Para estas clases, **las pruebas unitarias ya están escritas y comentadas**. Tu tarea es:

1. Descomentar los tests en el archivo de prueba correspondiente
2. Implementar la clase para que los tests pasen
3. Ejecutar `./gradlew test` para verificar

| Clase a Implementar | Archivo de Tests | Descripción |
|---------------------|------------------|-------------|
| `Document` | `DocumentTest.java` | Record que representa un documento con embeddings |
| `VectorStore` (interface) | `InMemoryVectorStoreTest.java` | Interfaz del puerto para almacenamiento vectorial |
| `InMemoryVectorStore` | `InMemoryVectorStoreTest.java` | Almacén vectorial en memoria con similitud coseno |
| `RAGQueryUseCase` | `RAGQueryUseCaseTest.java` | Caso de uso principal que orquesta el flujo RAG |

### 🟢 Modalidad 2: Escribir Tests (Clases ya implementadas)

Para estas clases, **la implementación ya existe**. Tu tarea es:

1. Analizar el código de la clase
2. Escribir pruebas unitarias completas
3. Alcanzar cobertura > 80%

| Clase Implementada | Archivo para Tests | Descripción |
|-------------------|-------------------|-------------|
| `GeminiChatService` | `GeminiChatServiceTest.java` | Servicio de chat con Gemini (ya tiene ejemplos) |
| `GeminiEmbeddingService` | `GeminiEmbeddingServiceTest.java` | Servicio de embeddings (ya tiene ejemplos) |
| `GeminiClientWrapper` | Crear `GeminiClientWrapperTest.java` | Wrapper del cliente oficial de Gemini |

---

## Estructura de Tareas

### Tarea 1: Domain Layer - `Document`

**Objetivo**: Implementar el record `Document` que representa un documento en el sistema.

**Archivo de Tests**: `src/test/java/com/enyoi/ragquery/ia/domain/model/DocumentTest.java`

**Pasos**:
1. Descomentar los tests en `DocumentTest.java`
2. El record debe tener: `id`, `content`, `metadata`, `embedding`
3. Implementar el método `getMetadataValue(String key)`

```java
// Ejemplo de uso esperado:
Document doc = new Document(
    "doc-1",
    "Contenido del documento",
    Map.of("source", "SOLID", "topic", "SRP"),
    List.of(0.1f, 0.2f, 0.3f)
);
String source = doc.getMetadataValue("source"); // "SOLID"
```

---

### Tarea 2: Domain Layer - `VectorStore` Interface

**Objetivo**: Definir la interfaz del puerto para almacenamiento vectorial.

**Archivo de Tests**: `src/test/java/com/enyoi/ragquery/ia/infrastructure/memory/InMemoryVectorStoreTest.java`

**Pasos**:
1. Descomentar los métodos en la interfaz `VectorStore.java`
2. La interfaz debe definir los contratos para:
   - `store(Document document)`
   - `storeBatch(List<Document> documents)`
   - `similaritySearch(List<Float> queryEmbedding, int topK)`
   - `similaritySearch(String query, int topK)`
   - `delete(String documentId)`
   - `clear()`

---

### Tarea 3: Infrastructure Layer - `InMemoryVectorStore`

**Objetivo**: Implementar el almacén vectorial en memoria.

**Archivo de Tests**: `src/test/java/com/enyoi/ragquery/ia/infrastructure/memory/InMemoryVectorStoreTest.java`

**Conceptos clave a implementar**:

1. **Almacenamiento**: Usar `Map<String, Document>` para guardar documentos
2. **Similitud Coseno**: Algoritmo para comparar vectores

```
                    A · B
Similitud(A,B) = ─────────────
                  ‖A‖ × ‖B‖

Donde:
- A · B = Producto punto (dot product)
- ‖A‖ = Magnitud del vector A
```

**Pasos**:
1. Descomentar los tests en `InMemoryVectorStoreTest.java`
2. Implementar la clase `InMemoryVectorStore`
3. Debe implementar `VectorStore`
4. Implementar el algoritmo de similitud coseno

---

### Tarea 4: Application Layer - `RAGQueryUseCase`

**Objetivo**: Implementar el caso de uso principal que orquesta todo el flujo RAG.

**Archivo de Tests**: `src/test/java/com/enyoi/ragquery/ia/application/RAGQueryUseCaseTest.java`

**Flujo a implementar**:

```
query(question) {
    1. Validar pregunta
    2. Generar embedding de la pregunta
    3. Buscar documentos similares
    4. Si no hay documentos → retornar mensaje
    5. Construir contexto con documentos
    6. Llamar al LLM con contexto + pregunta
    7. Retornar respuesta
}
```

**Pasos**:
1. Descomentar los tests en `RAGQueryUseCaseTest.java`
2. Implementar la clase `RAGQueryUseCase`
3. Implementar: `query()`, `queryWithSources()`, `ingestDocument()`


---

## Cómo Ejecutar

### Ejecutar Tests

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests con reporte de cobertura
./gradlew test jacocoTestReport

# Ver reporte de cobertura (abrir en navegador)
open build/reports/jacoco/test/html/index.html
```

### Ejecutar la Aplicación

```bash
# Asegúrate de tener configurada GOOGLE_API_KEY
export GOOGLE_API_KEY=tu_api_key

# Ejecutar
./gradlew runRAG
```

### Verificar Cobertura de Código

El reporte de JaCoCo se genera en:
- HTML: `build/reports/jacoco/test/html/index.html`
- XML: `build/reports/jacoco/test/jacocoTestReport.xml`

---

## Recursos de Aprendizaje

### Conceptos de IA

- [¿Qué es RAG?](https://www.pinecone.io/learn/retrieval-augmented-generation/)
- [Embeddings explicados](https://platform.openai.com/docs/guides/embeddings)
- [Similitud Coseno](https://en.wikipedia.org/wiki/Cosine_similarity)
- [Google Gemini API](https://ai.google.dev/docs)

### Principios SOLID

- **S**ingle Responsibility Principle
- **O**pen/Closed Principle
- **L**iskov Substitution Principle
- **I**nterface Segregation Principle
- **D**ependency Inversion Principle

### Testing en Java

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Framework](https://site.mockito.org/)
- [AssertJ Assertions](https://assertj.github.io/doc/)


---

## Licencia

Este proyecto es para fines educativos.
 