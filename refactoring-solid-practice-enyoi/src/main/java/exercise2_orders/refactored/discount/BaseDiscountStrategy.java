package exercise2_orders.refactored.discount;

import java.math.BigDecimal;

public abstract class BaseDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        return subtotal.multiply(getDiscountPercentage());
    }
}
