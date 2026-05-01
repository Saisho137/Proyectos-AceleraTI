package exercise2_orders.refactored.tax;

import exercise2_orders.refactored.model.CustomerType;

import java.math.BigDecimal;

public class ShippingCalculator {

    public ShippingCalculator() {
    }

    public BigDecimal calculate(CustomerType type, BigDecimal subtotal) {
        if (type == CustomerType.VIP) {
            return BigDecimal.ZERO;
        }
        if (type == CustomerType.PREMIUM && subtotal.compareTo(BigDecimal.valueOf(100)) > 0) {
            return BigDecimal.ZERO;
        }
        if (subtotal.compareTo(BigDecimal.valueOf(200)) > 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(9.99);
    }
}
