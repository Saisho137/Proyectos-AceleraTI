package exercise2_orders;

 import exercise2_orders.refactored.model.*;
 import exercise2_orders.refactored.discount.*;
 import exercise2_orders.refactored.repository.*;
 import exercise2_orders.refactored.validator.*;
 import exercise2_orders.refactored.tax.*;
 import exercise2_orders.refactored.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("refactoring")
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 2: Order Processing - Strategy & Repository Pattern")
public class OrderProcessorRefactoredTest {
    
    // ═══════════════════════════════════════════════════════════════════════════
    // PARTE 1: Tests para el Modelo de Dominio
    // ═══════════════════════════════════════════════════════════════════════════
    
    @Nested
    @DisplayName("2.1 - Modelo de Dominio (Order, OrderItem, Customer)")
    class DomainModelTests {

        @Test
        @DisplayName("OrderItem debe calcular su total correctamente")
        void orderItemShouldCalculateTotal() {
            OrderItem item = new OrderItem("Laptop", new BigDecimal("999.99"), 2);

            assertThat(item.getName()).isEqualTo("Laptop");
            assertThat(item.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
            assertThat(item.getQuantity()).isEqualTo(2);
            assertThat(item.getTotal()).isEqualByComparingTo(new BigDecimal("1999.98"));
        }

        @Test
        @DisplayName("Order debe calcular subtotal de todos los items")
        void orderShouldCalculateSubtotal() {
            List<OrderItem> items = Arrays.asList(
                    new OrderItem("Laptop", new BigDecimal("1000"), 1),
                    new OrderItem("Mouse", new BigDecimal("50"), 2)
            );

            Customer customer = new Customer("C001", "John Doe", CustomerType.REGULAR);
            Order order = new Order("ORD-001", customer, items);

            assertThat(order.getSubtotal()).isEqualByComparingTo(new BigDecimal("1100"));
        }

        @Test
        @DisplayName("Customer debe tener tipo y nombre")
        void customerShouldHaveTypeAndName() {
            Customer customer = new Customer("C001", "John Doe", CustomerType.PREMIUM);

            assertThat(customer.getId()).isEqualTo("C001");
            assertThat(customer.getName()).isEqualTo("John Doe");
            assertThat(customer.getType()).isEqualTo(CustomerType.PREMIUM);
        }

        @Test
        @DisplayName("CustomerType enum debe tener los tipos correctos")
        void customerTypeShouldHaveCorrectTypes() {
            assertThat(CustomerType.values())
                    .contains(CustomerType.REGULAR, CustomerType.PREMIUM,
                             CustomerType.VIP, CustomerType.EMPLOYEE);
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 2: Tests para DiscountStrategy (Strategy Pattern + OCP)
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.2 - DiscountStrategy (Strategy Pattern)")
    class DiscountStrategyTests {

        @Test
        @DisplayName("RegularCustomerDiscount no debe aplicar descuento")
        void regularCustomerShouldHaveNoDiscount() {
            DiscountStrategy strategy = new RegularCustomerDiscount();
            BigDecimal subtotal = new BigDecimal("100");

            BigDecimal discount = strategy.calculateDiscount(subtotal);

            assertThat(discount).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(strategy.getDiscountPercentage()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("PremiumCustomerDiscount debe aplicar 10% de descuento")
        void premiumCustomerShouldHave10PercentDiscount() {
            DiscountStrategy strategy = new PremiumCustomerDiscount();
            BigDecimal subtotal = new BigDecimal("100");

            BigDecimal discount = strategy.calculateDiscount(subtotal);

            assertThat(discount).isEqualByComparingTo(new BigDecimal("10"));
            assertThat(strategy.getDiscountPercentage()).isEqualByComparingTo(new BigDecimal("0.10"));
        }

        @Test
        @DisplayName("VipCustomerDiscount debe aplicar 20% de descuento")
        void vipCustomerShouldHave20PercentDiscount() {
            DiscountStrategy strategy = new VipCustomerDiscount();
            BigDecimal subtotal = new BigDecimal("100");

            BigDecimal discount = strategy.calculateDiscount(subtotal);

            assertThat(discount).isEqualByComparingTo(new BigDecimal("20"));
            assertThat(strategy.getDiscountPercentage()).isEqualByComparingTo(new BigDecimal("0.20"));
        }

        @Test
        @DisplayName("EmployeeDiscount debe aplicar 30% de descuento")
        void employeeShouldHave30PercentDiscount() {
            DiscountStrategy strategy = new EmployeeCustomerDiscount();
            BigDecimal subtotal = new BigDecimal("100");

            BigDecimal discount = strategy.calculateDiscount(subtotal);

            assertThat(discount).isEqualByComparingTo(new BigDecimal("30"));
        }

        @Test
        @DisplayName("Todas las estrategias deben implementar DiscountStrategy")
        void allStrategiesShouldImplementInterface() {
            assertThat(new RegularCustomerDiscount()).isInstanceOf(DiscountStrategy.class);
            assertThat(new PremiumCustomerDiscount()).isInstanceOf(DiscountStrategy.class);
            assertThat(new VipCustomerDiscount()).isInstanceOf(DiscountStrategy.class);
            assertThat(new EmployeeCustomerDiscount()).isInstanceOf(DiscountStrategy.class);
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 3: Tests para DiscountStrategyFactory
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.3 - DiscountStrategyFactory (Factory Pattern)")
    class DiscountStrategyFactoryTests {

        private DiscountStrategyFactory factory;

        @BeforeEach
        void setUp() {
            factory = new DiscountStrategyFactory();
        }

        @Test
        @DisplayName("Debe crear la estrategia correcta para cada tipo de cliente")
        void shouldCreateCorrectStrategyForEachCustomerType() {
            assertThat(factory.createStrategy(CustomerType.REGULAR))
                    .isInstanceOf(RegularCustomerDiscount.class);
            assertThat(factory.createStrategy(CustomerType.PREMIUM))
                    .isInstanceOf(PremiumCustomerDiscount.class);
            assertThat(factory.createStrategy(CustomerType.VIP))
                    .isInstanceOf(VipCustomerDiscount.class);
            assertThat(factory.createStrategy(CustomerType.EMPLOYEE))
                    .isInstanceOf(EmployeeCustomerDiscount.class);
        }

        @Test
        @DisplayName("Debe lanzar excepción para tipo null")
        void shouldThrowExceptionForNullType() {
            assertThatThrownBy(() -> factory.createStrategy(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 4: Tests para OrderRepository (Repository Pattern + DIP)
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.4 - OrderRepository (Repository Pattern)")
    class OrderRepositoryTests {

        private OrderRepository repository;

        @BeforeEach
        void setUp() {
            repository = new InMemoryOrderRepository();
        }

        @Test
        @DisplayName("Debe guardar y recuperar una orden")
        void shouldSaveAndRetrieveOrder() {
            Order order = createSampleOrder("ORD-001");

            repository.save(order);
            Optional<Order> found = repository.findById("ORD-001");

            assertThat(found).isPresent();
            assertThat(found.get().getOrderId()).isEqualTo("ORD-001");
        }

        @Test
        @DisplayName("Debe retornar empty cuando no existe la orden")
        void shouldReturnEmptyWhenOrderNotFound() {
            Optional<Order> found = repository.findById("NON-EXISTENT");

            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Debe listar todas las órdenes")
        void shouldListAllOrders() {
            repository.save(createSampleOrder("ORD-001"));
            repository.save(createSampleOrder("ORD-002"));

            List<Order> orders = repository.findAll();

            assertThat(orders).hasSize(2);
        }

        @Test
        @DisplayName("Debe actualizar una orden existente")
        void shouldUpdateExistingOrder() {
            Order order = createSampleOrder("ORD-001");
            repository.save(order);

            order.setStatus(OrderStatus.PAID);
            repository.save(order);

            Optional<Order> found = repository.findById("ORD-001");
            assertThat(found.get().getStatus()).isEqualTo(OrderStatus.PAID);
        }

        @Test
        @DisplayName("Debe eliminar una orden")
        void shouldDeleteOrder() {
            repository.save(createSampleOrder("ORD-001"));

            repository.delete("ORD-001");

            assertThat(repository.findById("ORD-001")).isEmpty();
        }

        private Order createSampleOrder(String orderId) {
            List<OrderItem> items = List.of(
                    new OrderItem("Product", new BigDecimal("100"), 1)
            );
            Customer customer = new Customer("C001", "John Doe", CustomerType.REGULAR);
            return new Order(orderId, customer, items);
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 5: Tests para OrderValidator (SRP)
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.5 - OrderValidator (Single Responsibility)")
    class OrderValidatorTests {

        private OrderValidator validator;

        @BeforeEach
        void setUp() {
            validator = new OrderValidator();
        }

        @Test
        @DisplayName("Debe validar orden con items válidos")
        void shouldValidateOrderWithValidItems() {
            List<OrderItem> items = List.of(
                    new OrderItem("Product", new BigDecimal("50"), 2)
            );

            ValidationResult result = validator.validate(items);

            assertThat(result.isValid()).isTrue();
        }

        @Test
        @DisplayName("Debe fallar validación con lista vacía")
        void shouldFailValidationWithEmptyList() {
            ValidationResult result = validator.validate(List.of());

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Order must have at least one item");
        }

        @Test
        @DisplayName("Debe fallar validación con lista null")
        void shouldFailValidationWithNullList() {
            ValidationResult result = validator.validate(null);

            assertThat(result.isValid()).isFalse();
        }

        @Test
        @DisplayName("Debe fallar validación con precio negativo")
        void shouldFailValidationWithNegativePrice() {
            List<OrderItem> items = List.of(
                    new OrderItem("Product", new BigDecimal("-10"), 1)
            );

            ValidationResult result = validator.validate(items);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).anyMatch(e -> e.contains("Price"));
        }

        @Test
        @DisplayName("Debe fallar validación con cantidad cero")
        void shouldFailValidationWithZeroQuantity() {
            List<OrderItem> items = List.of(
                    new OrderItem("Product", new BigDecimal("100"), 0)
            );

            ValidationResult result = validator.validate(items);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).anyMatch(e -> e.contains("Quantity"));
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 6: Tests para TaxCalculator (SRP)
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.6 - TaxCalculator (Single Responsibility)")
    class TaxCalculatorTests {

        @Test
        @DisplayName("Debe calcular impuesto del 19%")
        void shouldCalculate19PercentTax() {
            TaxCalculator calculator = new TaxCalculator(new BigDecimal("0.19"));
            BigDecimal amount = new BigDecimal("100");

            BigDecimal tax = calculator.calculate(amount);

            assertThat(tax).isEqualByComparingTo(new BigDecimal("19"));
        }

        @Test
        @DisplayName("Debe retornar cero para monto cero")
        void shouldReturnZeroForZeroAmount() {
            TaxCalculator calculator = new TaxCalculator(new BigDecimal("0.19"));

            BigDecimal tax = calculator.calculate(BigDecimal.ZERO);

            assertThat(tax).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Debe exponer la tasa de impuesto")
        void shouldExposeRate() {
            TaxCalculator calculator = new TaxCalculator(new BigDecimal("0.19"));

            assertThat(calculator.getRate()).isEqualByComparingTo(new BigDecimal("0.19"));
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 7: Tests para ShippingCalculator (SRP + Strategy opcional)
    ////// ═══════════════════════════════════════════════════════════════════════════
    ////
    @Nested
    @DisplayName("2.7 - ShippingCalculator")
    class ShippingCalculatorTests {

        private ShippingCalculator calculator;

        @BeforeEach
        void setUp() {
            calculator = new ShippingCalculator();
        }

        @Test
        @DisplayName("VIP siempre tiene envío gratis")
        void vipShouldAlwaysHaveFreeShipping() {
            BigDecimal cost = calculator.calculate(CustomerType.VIP, new BigDecimal("50"));

            assertThat(cost).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Premium tiene envío gratis si compra > $100")
        void premiumShouldHaveFreeShippingOver100() {
            assertThat(calculator.calculate(CustomerType.PREMIUM, new BigDecimal("150")))
                    .isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(calculator.calculate(CustomerType.PREMIUM, new BigDecimal("50")))
                    .isGreaterThan(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Compras > $200 tienen envío gratis")
        void ordersOver200ShouldHaveFreeShipping() {
            BigDecimal cost = calculator.calculate(CustomerType.REGULAR, new BigDecimal("250"));

            assertThat(cost).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Envío estándar cuesta $9.99")
        void standardShippingShouldCost999() {
            BigDecimal cost = calculator.calculate(CustomerType.REGULAR, new BigDecimal("50"));

            assertThat(cost).isEqualByComparingTo(new BigDecimal("9.99"));
        }
    }

    ////// ═══════════════════════════════════════════════════════════════════════════
    ////// PARTE 8: Tests para OrderService Refactorizado (Orquestador con DIP)
    ////// ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("2.8 - OrderService Refactorizado (DIP)")
    class OrderServiceTests {

        @Mock
        private OrderRepository mockRepository;

        @Mock
        private DiscountStrategyFactory mockDiscountFactory;

        @Mock
        private DiscountStrategy mockDiscountStrategy;

        @Mock
        private OrderValidator mockValidator;

        private OrderService orderService;

        @BeforeEach
        void setUp() {
            TaxCalculator taxCalculator = new TaxCalculator(new BigDecimal("0.19"));
            ShippingCalculator shippingCalculator = new ShippingCalculator();

            orderService = new OrderService(
                    mockRepository,
                    mockDiscountFactory,
                    mockValidator,
                    taxCalculator,
                    shippingCalculator
            );
        }

        @Test
        @DisplayName("Debe validar items antes de procesar")
        void shouldValidateItemsBeforeProcessing() {
            // Arrange
            List<OrderItem> items = List.of(new OrderItem("Test", new BigDecimal("100"), 1));
            Customer customer = new Customer("C001", "John Doe", CustomerType.REGULAR);
            when(mockValidator.validate(items))
                    .thenReturn(ValidationResult.invalid(List.of("Validation failed")));

            // Act & Assert
            assertThatThrownBy(() -> orderService.processOrder(customer, items))
                    .isInstanceOf(Exception.class);

            verify(mockValidator).validate(items);
            verify(mockRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe usar la factory para obtener estrategia de descuento")
        void shouldUseFactoryToGetDiscountStrategy() throws Exception {
            // Arrange
            List<OrderItem> items = List.of(new OrderItem("Test", new BigDecimal("100"), 1));
            Customer customer = new Customer("C001", "John Doe", CustomerType.PREMIUM);
            when(mockValidator.validate(items)).thenReturn(ValidationResult.valid());
            when(mockDiscountFactory.createStrategy(CustomerType.PREMIUM))
                    .thenReturn(mockDiscountStrategy);
            when(mockDiscountStrategy.calculateDiscount(any())).thenReturn(new BigDecimal("10"));

            // Act
            orderService.processOrder(customer, items);

            // Assert
            verify(mockDiscountFactory).createStrategy(CustomerType.PREMIUM);
        }

        @Test
        @DisplayName("Debe guardar la orden en el repositorio")
        void shouldSaveOrderToRepository() throws Exception {
            // Arrange
            List<OrderItem> items = List.of(new OrderItem("Test", new BigDecimal("100"), 1));
            Customer customer = new Customer("C001", "John Doe", CustomerType.REGULAR);
            when(mockValidator.validate(items)).thenReturn(ValidationResult.valid());
            when(mockDiscountFactory.createStrategy(any())).thenReturn(mockDiscountStrategy);
            when(mockDiscountStrategy.calculateDiscount(any())).thenReturn(BigDecimal.ZERO);

            // Act
            orderService.processOrder(customer, items);

            // Assert
            verify(mockRepository).save(any(Order.class));
        }

        @Test
        @DisplayName("Debe calcular el total correctamente")
        void shouldCalculateTotalCorrectly() throws Exception {
            // Arrange
            List<OrderItem> items = List.of(new OrderItem("Product", new BigDecimal("100"), 1));
            Customer customer = new Customer("C001", "John Doe", CustomerType.VIP);

            when(mockValidator.validate(items)).thenReturn(ValidationResult.valid());
            when(mockDiscountFactory.createStrategy(CustomerType.VIP))
                    .thenReturn(new VipCustomerDiscount());

            // Act
            Order order = orderService.processOrder(customer, items);

            // Assert - VIP: 100 - 20 (20% desc) = 80 + 15.20 (19% tax) + 0 (free ship) = 95.20
            assertThat(order.getTotal()).isEqualByComparingTo(new BigDecimal("95.20"));
        }
    }

}
