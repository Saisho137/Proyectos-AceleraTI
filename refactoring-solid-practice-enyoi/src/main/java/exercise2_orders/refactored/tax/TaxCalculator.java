package exercise2_orders.refactored.tax;

import java.math.BigDecimal;

public class TaxCalculator {
    private final BigDecimal rate;


    public TaxCalculator(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal calculate(BigDecimal subtotal) {
        return subtotal.multiply(rate);
    }
}
