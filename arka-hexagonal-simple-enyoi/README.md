# Arka Distribution - Sistema de Gestion de Inventario y Ordenes

## Descripcion del Proyecto

Arka Distribution es un sistema de gestion de inventario y ordenes desarrollado siguiendo los principios de **Arquitectura Hexagonal** (Ports and Adapters). El proyecto esta implementado en Java 17 utilizando JPA/Hibernate para la persistencia y SQLite como base de datos.

## Objetivo de la Actividad

El objetivo de esta actividad es, **partiendo de los Ports (interfaces) definidos**, implementar:

1. **Adaptadores de salida (out)**: Implementaciones de los repositorios usando JPA
2. **Adaptadores de entrada (in)**: Implementaciones de los servicios de dominio
3. **Entidades de dominio**: Modelos de negocio con su logica
4. **Value Objects**: Objetos de valor inmutables
5. **Pruebas unitarias**: Cobertura completa de todas las implementaciones

### Ports Proporcionados (Interfaces)

Los siguientes ports fueron proporcionados como punto de partida:

**Ports de Entrada (in):**
- `InventoryService`: Operaciones de gestion de inventario
- `OrderService`: Operaciones de gestion de ordenes

**Ports de Salida (out):**
- `ProductRepository`: Persistencia de productos
- `CustomerRepository`: Persistencia de clientes
- `OrderRepository`: Persistencia de ordenes
- `NotificationService`: Servicio de notificaciones

---

## Arquitectura del Proyecto

```
src/main/java/com/enyoi/arka/
|
|-- domain/                          # Nucleo del dominio (sin dependencias externas)
|   |-- entities/                    # Entidades de dominio
|   |   |-- Product.java
|   |   |-- Customer.java
|   |   |-- Order.java
|   |   |-- OrderItem.java
|   |   |-- ProductCategory.java
|   |   |-- OrderStatus.java
|   |
|   |-- valueobjects/                # Objetos de valor
|   |   |-- ProductId.java
|   |   |-- CustomerId.java
|   |   |-- OrderId.java
|   |   |-- Money.java
|   |   |-- Email.java
|   |
|   |-- ports/                       # Interfaces (contratos)
|   |   |-- in/                      # Ports de entrada (casos de uso)
|   |   |   |-- InventoryService.java
|   |   |   |-- OrderService.java
|   |   |
|   |   |-- out/                     # Ports de salida (infraestructura)
|   |       |-- ProductRepository.java
|   |       |-- CustomerRepository.java
|   |       |-- OrderRepository.java
|   |       |-- NotificationService.java
|   |
|   |-- exception/                   # Excepciones de dominio
|       |-- ArkaDomainException.java
|       |-- ProductNotFoundException.java
|       |-- CustomerNotFoundException.java
|       |-- InsufficientStockException.java
|
|-- adapters/                        # Implementaciones de los ports
    |-- in/                          # Adaptadores de entrada
    |   |-- InventoryServiceImpl.java
    |   |-- OrderServiceImpl.java
    |
    |-- out/                         # Adaptadores de salida
        |-- repository/              # Implementaciones JPA
        |   |-- JpaProductRepository.java
        |   |-- JpaCustomerRepository.java
        |   |-- JpaOrderRepository.java
        |   |-- entity/              # Entidades JPA
        |   |   |-- ProductEntity.java
        |   |   |-- CustomerEntity.java
        |   |   |-- OrderEntity.java
        |   |   |-- OrderItemEntity.java
        |   |-- config/
        |       |-- DatabaseConfig.java
        |
        |-- service/
            |-- ConsoleNotificationService.java
```

---

## Modelo de Dominio

### Entidades

| Entidad | Descripcion |
|---------|-------------|
| `Product` | Representa un producto del inventario con stock, precio y categoria |
| `Customer` | Representa un cliente con datos de contacto |
| `Order` | Representa una orden de compra con items y estado |
| `OrderItem` | Representa un item dentro de una orden |

### Value Objects

| Value Object | Descripcion |
|--------------|-------------|
| `ProductId` | Identificador unico de producto |
| `CustomerId` | Identificador unico de cliente |
| `OrderId` | Identificador unico de orden |
| `Money` | Representa un valor monetario con moneda |
| `Email` | Representa una direccion de correo validada |

### Estados de Orden

```
PENDIENTE -> CONFIRMADO -> EN_DESPACHO -> ENTREGADO
```

### Categorias de Producto

- ACCESORIOS_PC
- PERIFERICOS
- COMPONENTES
- ALMACENAMIENTO
- ENFRIAMIENTO
- OTROS

---

## Pruebas Unitarias

### Estrategia de Testing

1. **Repositorios**: Pruebas de integracion con base de datos SQLite en memoria
2. **Value Objects**: Pruebas unitarias de validacion y comportamiento
3. **Entidades de Dominio**: Pruebas unitarias de logica de negocio
4. **Servicios**: Pruebas unitarias con mocks (Mockito)
5. **Excepciones**: Pruebas de mensajes y jerarquia

### Estructura de Tests

```
src/test/java/com/enyoi/arka/
|
|-- adapters/
|   |-- in/
|   |   |-- InventoryServiceImplTest.java
|   |   |-- OrderServiceImplTest.java
|   |
|   |-- out/
|       |-- repository/
|       |   |-- JpaProductRepositoryTest.java
|       |   |-- JpaCustomerRepositoryTest.java
|       |   |-- JpaOrderRepositoryTest.java
|       |
|       |-- service/
|           |-- ConsoleNotificationServiceTest.java
|
|-- domain/
    |-- entities/
    |   |-- ProductTest.java
    |   |-- CustomerTest.java
    |   |-- OrderTest.java
    |   |-- OrderItemTest.java
    |
    |-- valueobjects/
    |   |-- ProductIdTest.java
    |   |-- CustomerIdTest.java
    |   |-- OrderIdTest.java
    |   |-- MoneyTest.java
    |   |-- EmailTest.java
    |
    |-- exception/
        |-- DomainExceptionTest.java
```

### Configuracion de Base de Datos para Tests

El archivo `src/test/resources/META-INF/persistence.xml` configura SQLite en modo memoria:

```xml
<property name="hibernate.connection.url" 
          value="jdbc:sqlite:file:testdb?mode=memory&amp;cache=shared"/>
<property name="hibernate.hbm2ddl.auto" value="create-drop"/>
```

---

## Tecnologias Utilizadas

| Tecnologia | Version | Proposito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programacion |
| Gradle | 9.0 | Build tool |
| Hibernate | 6.4.1 | ORM / JPA Provider |
| SQLite | 3.45.1 | Base de datos |
| JUnit 5 | 5.10 | Framework de testing |
| AssertJ | 3.25.1 | Aserciones fluidas |
| Mockito | 5.x | Mocking framework |
| JaCoCo | - | Cobertura de codigo |

---

## Comandos de Ejecucion

### Compilar el proyecto

```bash
./gradlew build
```

### Ejecutar todas las pruebas

```bash
./gradlew test
```

### Ejecutar pruebas especificas

```bash
# Solo repositorios
./gradlew test --tests "*Repository*"

# Solo servicios
./gradlew test --tests "*ServiceImpl*"

# Solo value objects
./gradlew test --tests "*IdTest" --tests "*MoneyTest" --tests "*EmailTest"
```

### Ejecutar la aplicacion

```bash
./gradlew run
```

### Generar reporte de cobertura

```bash
./gradlew test jacocoTestReport
```

El reporte se genera en: `build/reports/jacocoHtml/index.html`

---

## Principios de Arquitectura Hexagonal Aplicados

### 1. Independencia del Dominio

El nucleo del dominio (`domain/`) no tiene dependencias de frameworks externos. Las entidades y value objects son POJOs puros.

### 2. Inversion de Dependencias

Los adaptadores dependen de los ports (interfaces), no al reves. El dominio define los contratos que la infraestructura debe cumplir.

### 3. Separacion de Concerns

- **Ports de entrada**: Definen los casos de uso del sistema
- **Ports de salida**: Definen las necesidades de infraestructura
- **Adaptadores**: Implementan los detalles tecnicos

### 4. Testabilidad

La arquitectura permite:
- Probar el dominio de forma aislada
- Usar mocks para los ports de salida en tests de servicios
- Usar base de datos en memoria para tests de repositorios

---

## Flujo de una Operacion

Ejemplo: Crear una orden

```
1. [Adaptador IN] OrderServiceImpl.createOrder()
        |
        v
2. [Port OUT] CustomerRepository.findById() -> Valida cliente
        |
        v
3. [Port OUT] ProductRepository.findById() -> Valida stock
        |
        v
4. [Dominio] Order.builder().build() -> Crea orden
        |
        v
5. [Port OUT] OrderRepository.save() -> Persiste orden
        |
        v
6. [Dominio] Product.reduceStock() -> Reduce inventario
        |
        v
7. [Port OUT] ProductRepository.save() -> Actualiza stock
        |
        v
8. [Port OUT] NotificationService.notifyOrderStatusChange() -> Notifica
```

---

## Autor

Proyecto desarrollado como ejercicio de implementacion de Arquitectura Hexagonal.

## Licencia

Este proyecto es de uso educativo.
