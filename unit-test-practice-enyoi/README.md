# Unit Test Practice - Guía de Pruebas Unitarias en Java

**Proyecto educativo para aprender a escribir pruebas unitarias efectivas en Java.**

Este repositorio es una guía práctica para pruebas unitarias partiendo desde conceptos básicos hasta patrones avanzados como Clean Architecture, mocking y cobertura de código.

## Descripción General

Las pruebas unitarias son **pruebas automatizadas que verifican el comportamiento correcto de pequeñas unidades de código** (métodos, funciones) de manera aislada. Este proyecto te muestra tres niveles de complejidad creciente:

### Ruta de Aprendizaje Progresiva

#### **Nivel 1: SimpleExample** - Fundamentos de Testing
**Ideal para**: Principiantes en testing
- Métodos simples sin dependencias externas
- Pruebas unitarias puras con JUnit 5
- Aprender conceptos clave: Arrange-Act-Assert, casos positivos y negativos
- **Ejemplos**: Validar números pares, detectar años bisiestos, búsqueda en listas
- **Técnicas**: Assertions básicas, testing de excepciones

#### **Nivel 2: DbExample** - Testing con Dependencias (Mocking)
**Ideal para**: Intermedio - Testing de lógica con base de datos
- Implementación de **Clean Architecture** (Use Cases y Adapters)
- **Mocking** con Mockito: simular comportamiento de la BD
- Separación de responsabilidades
- **Conceptos**: Inversión de dependencias, interfaces
- **Ejemplo**: Crear y listar tareas de una BD SQLite

#### **Nivel 3: RestExample** - Testing de APIs Externas
**Ideal para**: Avanzado - Testing con servicios REST
- Consumo de APIs externas con Retrofit
- Mocking de llamadas HTTP
- Manejo de DTOs (Data Transfer Objects)
- **Ejemplo**: Obtener geolocalización por IP, consumir APIs públicas

## Requisitos Previos

- **Java 17** o superior
- **Gradle** 9.0 o superior (el proyecto incluye gradlew, no necesitas instalarlo)
- **Git** (opcional, para clonar el repositorio)
- Un editor o IDE: VS Code, IntelliJ IDEA, Eclipse, etc.

## Guía de Inicio Rápido

### Ver el reporte de cobertura

**¿Qué es la cobertura?** Es el porcentaje de código que está siendo probado por tus tests. Objetivo: >80%

## Conceptos Clave de Testing

### ¿Qué es una Prueba Unitaria?
Una prueba unitaria verifica que **una pequeña unidad de código** (típicamente un método) funciona correctamente **en aislamiento**.

```
- CORRECTO: Probar un método específico con entradas conocidas
- INCORRECTO: Probar toda la aplicación de una vez
```

### Patrón AAA (Arrange-Act-Assert)
Toda prueba unitaria sigue este patrón:

```java
@Test
void miPruebaEjemplo() {
    // 1. ARRANGE: Preparar datos de entrada
    String nombre = "Juan";
    
    // 2. ACT: Ejecutar el código a probar
    String resultado = simpleFunctions.helloHuman(nombre);
    
    // 3. ASSERT: Verificar que el resultado es correcto
    assertThat(resultado).isEqualTo("Hola, Juan!");
}
```

### Nomenclatura de Pruebas
Las buenas pruebas tienen nombres descriptivos:
```
- Malo:    testMethod()
- Bueno:   shouldReturnTrueWhenNumberIsEven()
```

### Tipos de Pruebas en este Proyecto

| Tipo | Ejemplo | Herramientas |
|------|---------|-------------|
| **Unitarias simples** | SimpleFunctionsTest | JUnit 5, AssertJ |
| **Con mocks** | CreateTaskUseCaseTest | Mockito |
| **Integración** | RestApiAdapterTest | Retrofit Mock |

## Actividades Recomendadas para Estudiantes

### Nivel 1: SimpleExample - Para empezar
1. **Lee el código** de `SimpleFunctions.java`
2. **Ejecuta las pruebas** ya existentes: `./gradlew test`
3. **Escribe tu propia prueba** para un método, siguiendo el patrón AAA
4. **Crea nuevos métodos** y escribe tests para ellos
5. **Aprende AssertJ**: Usa `assertThat()` en lugar de `assertEquals()`

**Ejercicio práctico**:
- Agrega un método en `SimpleFunctions`
- Escribe un test que valide casos positivos y negativos
- Ejecuta tu test y asegúrate de que pase

### Nivel 2: DbExample - Mocking básico
1. **Entiende Clean Architecture**: UseCase vs Adapter
2. **Estudia Mockito**: Cómo simular comportamiento de BD
3. **Analiza** `CreateTaskUseCaseTest` para ver mocking en acción
4. **Escribe un test** que mockee el adapter

**Concepto clave**: 
```
UseCase = Lógica de negocio (lo que quieres probar)
Repository = Detalles de implementación (lo que quieres simular/mockear)
```

**Ejercicio práctico**:
- Crea un nuevo UseCase: `DeleteTaskUseCase`
- Escribe su test usando Mockito para mockear el repositorio
- Verifica que se llama al método correcto del repositorio

### Nivel 3: RestExample - APIs externas
1. **Entiende las APIs REST** y DTOs
2. **Aprende a mockear llamadas HTTP** sin hacer requests reales
3. **Estudia** [ApiUseCaseMockExampleTest.java](src/test/java/restexample/ApiUseCaseMockExampleTest.java)
4. **Escribe tests** para casos de error (API caída, timeout, etc.)

**Ejercicio práctico**:
- Agrega un test que simule una respuesta de error (HTTP 404)
- Verifica que tu UseCase maneja el error correctamente
- No hagas requests reales, usa mocks!

## Best Practices - Cómo Escribir Buenas Pruebas

### **DO** (Haz esto)
```
- Prueba una cosa por test
- Usa nombres muy descriptivos: shouldReturnTrueWhenNumberIsEven()
- Agrupa tests con @Nested o clases internas
- Prueba casos: positivos, negativos, límite
- Mantén tests independientes
- Simula (mockea) dependencias externas
- Escribe tests antes o junto con el código (TDD)
```

### **DON'T** (Evita esto)
```
- Hagas requests HTTP reales en tests (usa mocks)
- Accedas a BD real durante pruebas (usa mocks)
- Mezcles múltiples asserts (una verificación por test)
- Hardcodees valores mágicos (usa constantes)
- Confíes en el orden de ejecución
- Escribas tests que dependan de otros tests
- Hagas tests demasiado complejos
```

### Ejemplo de Test Bien Escrito

```java
@Test
void shouldReturnTrueWhenCheckingEvenNumberWithTwo() {
    // Arrange: Preparar entrada
    int evenNumber = 2;
    
    // Act: Ejecutar el método
    boolean result = simpleFunctions.checkEvenNumber(evenNumber);
    
    // Assert: Verificar resultado
    assertThat(result)
        .as("El número 2 debería ser par")
        .isTrue();
}
```

**Notas**:
- Nombre descriptivo del test 
- Patrón AAA claro 
- Un solo assert principal 
- Mensaje descriptivo en el assert 

## Dependencias (Entender qué es cada una)

### Dependencias de Producción (Código real)
| Librería | Versión | Para qué sirve |
|----------|---------|----------------|
| Retrofit | 2.9.0 | Cliente HTTP para llamar APIs REST |
| Gson | 2.9.0 | Convertir JSON ↔ Objetos Java |
| SQLite JDBC | 3.45.1.0 | Usar bases de datos SQLite |

### Dependencias de Testing (Solo para pruebas)
| Librería | Versión | Para qué sirve |
|----------|---------|----------------|
| JUnit 5 | 5.10.0 | Framework principal para escribir tests |
| AssertJ | 3.25.1 | Assertions más legibles (`assertThat()`) |
| Mockito | 5.x | Crear mocks/simulaciones de dependencias |

###  Herramientas
| Herramienta | Función |
|------------|---------|
| **JaCoCo** | Medir cobertura de código |
| **Gradle** | Automatizar compilación, pruebas y build |

## Glosario de Términos

| Término | Definición |
|---------|-----------|
| **Unit Test** | Prueba automatizada de una pequeña unidad de código |
| **Mock** | Simulación de un objeto/servicio para testing |
| **Cobertura** | % de código que está siendo probado |
| **Assertions** | Verificaciones que confirman el resultado esperado |
| **Fixture** | Datos/estado preparado para una prueba |
| **Stub** | Simulación simple que retorna valores fijos |
| **Clean Architecture** | Separación de responsabilidades (Use Cases, Adapters) |
| **DTO** | Data Transfer Object - objeto para transferir datos |

## Recursos de Aprendizaje Adicionales

### Documentación Oficial
- [JUnit 5 Documentation](https://junit.org/junit5/docs/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
