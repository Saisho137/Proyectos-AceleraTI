package com.enyoi.arka.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Excepciones de Dominio")
class DomainExceptionTest {

    @Test
    @DisplayName("ArkaDomainException debe contener mensaje")
    void arkaDomainExceptionDebeContenerMensaje() {
        // When
        ArkaDomainException exception = new ArkaDomainException("Error de prueba");

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error de prueba");
    }

    @Test
    @DisplayName("ArkaDomainException debe contener mensaje y causa")
    void arkaDomainExceptionDebeContenerMensajeYCausa() {
        // Given
        Throwable causa = new RuntimeException("Causa original");

        // When
        ArkaDomainException exception = new ArkaDomainException("Error de prueba", causa);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error de prueba");
        assertThat(exception.getCause()).isEqualTo(causa);
    }

    @Test
    @DisplayName("ProductNotFoundException debe contener ID del producto")
    void productNotFoundExceptionDebeContenerIdDelProducto() {
        // When
        ProductNotFoundException exception = new ProductNotFoundException("prod-001");

        // Then
        assertThat(exception.getMessage()).contains("prod-001");
        assertThat(exception.getMessage()).contains("Product not found");
    }

    @Test
    @DisplayName("CustomerNotFoundException debe contener ID del cliente")
    void customerNotFoundExceptionDebeContenerIdDelCliente() {
        // When
        CustomerNotFoundException exception = new CustomerNotFoundException("cust-001");

        // Then
        assertThat(exception.getMessage()).contains("cust-001");
        assertThat(exception.getMessage()).contains("Customer not found");
    }

    @Test
    @DisplayName("InsufficientStockException debe contener detalles del stock")
    void insufficientStockExceptionDebeContenerDetallesDelStock() {
        // When
        InsufficientStockException exception = new InsufficientStockException("prod-001", 10, 5);

        // Then
        assertThat(exception.getMessage()).contains("prod-001");
        assertThat(exception.getMessage()).contains("requested 10");
        assertThat(exception.getMessage()).contains("available 5");
        assertThat(exception.getMessage()).contains("Insufficient stock");
    }

    @Test
    @DisplayName("Todas las excepciones deben extender ArkaDomainException")
    void todasLasExcepcionesDebenExtenderArkaDomainException() {
        // Then
        assertThat(new ProductNotFoundException("id")).isInstanceOf(ArkaDomainException.class);
        assertThat(new CustomerNotFoundException("id")).isInstanceOf(ArkaDomainException.class);
        assertThat(new InsufficientStockException("id", 1, 0)).isInstanceOf(ArkaDomainException.class);
    }

    @Test
    @DisplayName("Todas las excepciones deben ser RuntimeException")
    void todasLasExcepcionesDebenSerRuntimeException() {
        // Then
        assertThat(new ArkaDomainException("test")).isInstanceOf(RuntimeException.class);
        assertThat(new ProductNotFoundException("id")).isInstanceOf(RuntimeException.class);
        assertThat(new CustomerNotFoundException("id")).isInstanceOf(RuntimeException.class);
        assertThat(new InsufficientStockException("id", 1, 0)).isInstanceOf(RuntimeException.class);
    }
}
