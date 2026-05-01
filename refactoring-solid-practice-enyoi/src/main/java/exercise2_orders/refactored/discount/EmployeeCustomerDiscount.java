package exercise2_orders.refactored.discount;

import java.math.BigDecimal;

public class EmployeeCustomerDiscount extends BaseDiscountStrategy {
    @Override
    public BigDecimal getDiscountPercentage() {
        return BigDecimal.valueOf(.3);
    }
}
