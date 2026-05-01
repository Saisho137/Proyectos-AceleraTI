package exercise2_orders.refactored.discount;

import exercise2_orders.refactored.model.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(BigDecimal subtotal);
    BigDecimal getDiscountPercentage();
}
