package com.enyoi.arka.adapters.out.repository;

import com.enyoi.arka.domain.entities.Customer;
import com.enyoi.arka.domain.valueobjects.CustomerId;
import com.enyoi.arka.domain.valueobjects.Email;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JpaCustomerRepository - Tests de Integración")
class JpaCustomerRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private JpaCustomerRepository repository;

    @BeforeAll
    static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @AfterAll
    static void tearDownClass() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        repository = new JpaCustomerRepository(entityManager);
        limpiarBaseDeDatos();
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    private void limpiarBaseDeDatos() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM CustomerEntity").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private Customer crearCustomer(String id, String nombre, String email, String city) {
        return Customer.builder()
                .id(CustomerId.of(id))
                .name(nombre)
                .email(Email.of(email))
                .city(city)
                .build();
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar un customer nuevo correctamente")
        void debeGuardarCustomerNuevo() {
            // Given
            Customer customer = crearCustomer("prod-001", "Teclado Mecánico", "jhon@test.com", "Bogotá");

            // When
            Customer resultado = repository.save(customer);

            // Then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId().value()).isEqualTo("prod-001");
            assertThat(resultado.getName()).isEqualTo("Teclado Mecánico");
        }

        @Test
        @DisplayName("Debe actualizar un customer existente")
        void debeActualizarCustomerExistente() {
            // Given
            Customer customer = crearCustomer("prod-001", "Teclado Mecánico", "jhon@test.com", "Bogotá");
            repository.save(customer);

            Customer customerActualizado = crearCustomer("prod-002", "Mouse Gaming RGB", "jhon2@test.com", "Bogotá");

            // When
            repository.save(customerActualizado);

            // Then
            Optional<Customer> encontrado = repository.findById(CustomerId.of("prod-002"));
            assertThat(encontrado).isPresent();
            assertThat(encontrado.get().getName()).isEqualTo("Mouse Gaming RGB");
            assertThat(encontrado.get().getEmail().value()).isEqualTo("jhon2@test.com");
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Debe encontrar customer por ID existente")
        void debeEncontrarCustomerPorIdExistente() {
            // Given
            Customer customer = crearCustomer("prod-001", "Monitor 27\"", "jhon@test.com", "Bogotá");
            repository.save(customer);

            // When
            Optional<Customer> resultado = repository.findById(CustomerId.of("prod-001"));

            // Then
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getName()).isEqualTo("Monitor 27\"");
        }

        @Test
        @DisplayName("Debe retornar vacío para ID inexistente")
        void debeRetornarVacioParaIdInexistente() {
            // When
            Optional<Customer> resultado = repository.findById(CustomerId.of("no-existe"));

            // Then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay customers")
        void debeRetornarListaVaciaCuandoNoHayCustomeros() {
            // When
            List<Customer> resultado = repository.findAll();

            // Then
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("Debe retornar todos los customers")
        void debeRetornarTodosLosCustomeros() {
            // Given
            repository.save(crearCustomer("prod-004", "john", "john@test.com", "Madrid"));
            repository.save(crearCustomer("prod-005", "jane", "jane@test.com", "Barcelona"));
            repository.save(crearCustomer("prod-006", "doe", "doe@test.com", "Medellin"));

            // When
            List<Customer> resultado = repository.findAll();

            // Then
            assertThat(resultado).hasSize(3);
        }
    }

    @Nested
    @DisplayName("findByEmail()")
    class FindByEmailTests {

        @Test
        @DisplayName("Debe encontrar customers por categoría")
        void debeEncontrarCustomerosPorCategoria() {
            // Given
            repository.save(crearCustomer("prod-004", "john", "john@test.com", "Madrid"));
            repository.save(crearCustomer("prod-005", "jane", "jane@test.com", "Barcelona"));
            repository.save(crearCustomer("prod-006", "doe", "doe@test.com", "Medellin"));

            // When
            Optional<Customer> resultado = repository.findByEmail("john@test.com");

            // Then
            assertThat(resultado).isPresent();
            assertThat(resultado).get().hasFieldOrPropertyWithValue("name", "john");
            assertThat(resultado).get().hasFieldOrPropertyWithValue("email", Email.of("john@test.com"));
        }

        @Test
        @DisplayName("Debe retornar lista vacía si no hay customers con email")
        void debeRetornarListaVaciaSiNoHayCustomersConEmail() {
            // Given
            repository.save(crearCustomer("prod-004", "john", "john@test.com", "Madrid"));

            // When
            Optional<Customer> resultado = repository.findByEmail("a@a.a");

            // Then
            assertThat(resultado).isNotPresent();
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("Debe retornar true si customer existe")
        void debeRetornarTrueSiCustomeroExiste() {
            // Given
            repository.save(crearCustomer("prod-004", "john", "john@test.com", "Madrid"));

            // When
            boolean existe = repository.existsById(CustomerId.of("prod-004"));

            // Then
            assertThat(existe).isTrue();
        }

        @Test
        @DisplayName("Debe retornar false si customer no existe")
        void debeRetornarFalseSiCustomeroNoExiste() {
            // When
            boolean existe = repository.existsById(CustomerId.of("no-existe"));

            // Then
            assertThat(existe).isFalse();
        }
    }

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("Debe eliminar customer existente")
        void debeEliminarCustomeroExistente() {
            // Given
            repository.save(crearCustomer("prod-004", "john", "john@test.com", "Madrid"));
            assertThat(repository.existsById(CustomerId.of("prod-004"))).isTrue();

            // When
            repository.deleteById(CustomerId.of("prod-004"));

            // Then
            assertThat(repository.existsById(CustomerId.of("prod-004"))).isFalse();
        }

        @Test
        @DisplayName("No debe lanzar excepción al eliminar customer inexistente")
        void noDebeLanzarExcepcionAlEliminarCustomeroInexistente() {
            // When & Then - No debería lanzar excepción
            repository.deleteById(CustomerId.of("no-existe"));
        }
    }
}