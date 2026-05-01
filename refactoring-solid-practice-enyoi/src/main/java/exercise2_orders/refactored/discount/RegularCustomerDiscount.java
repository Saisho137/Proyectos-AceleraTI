package exercise2_orders.refactored.discount;

import java.math.BigDecimal;

public class RegularCustomerDiscount extends BaseDiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage() {
        return BigDecimal.ZERO;
    }
}
