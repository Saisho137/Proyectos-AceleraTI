package exercise2_orders.refactored;

import exercise2_orders.refactored.discount.DiscountStrategy;
import exercise2_orders.refactored.discount.DiscountStrategyFactory;
import exercise2_orders.refactored.model.*;
import exercise2_orders.refactored.repository.OrderRepository;
import exercise2_orders.refactored.tax.ShippingCalculator;
import exercise2_orders.refactored.tax.TaxCalculator;
import exercise2_orders.refactored.validator.OrderValidator;
import exercise2_orders.refactored.validator.ValidationResult;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final OrderRepository repository;
    private final DiscountStrategyFactory factory;
    private final OrderValidator validator;
    private final TaxCalculator taxCalculator;
    private final ShippingCalculator shippingCalculator;

    public OrderService(OrderRepository repository,
                        DiscountStrategyFactory factory,
                        OrderValidator validator,
                        TaxCalculator taxCalculator,
                        ShippingCalculator shippingCalculator) {
        this.repository = repository;
        this.factory = factory;
        this.validator = validator;
        this.taxCalculator = taxCalculator;
        this.shippingCalculator = shippingCalculator;
    }

    public Order processOrder(Customer customer, List<OrderItem> items) throws Exception {
        ValidationResult result = validator.validate(items);

        if (!result.isValid())
            throw new Exception("Invalid order");

        DiscountStrategy strategy = factory.createStrategy(customer.getType());

        int count = repository.findAll().size();

        String orderId = "ORD-" + String.format("%05d", count++);
        Order order = new Order(orderId, customer, items);

        BigDecimal subtotal = order.getSubtotal();
        BigDecimal discount = strategy.calculateDiscount(subtotal);
        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal taxAmount = taxCalculator.calculate(afterDiscount);
        BigDecimal total = afterDiscount.add(taxAmount);
        BigDecimal shippingCost = shippingCalculator.calculate(customer.getType(), subtotal);
        total = total.add(shippingCost);

        order.setTotal(total);
        repository.save(order);

        return order;
    }
}
