package exercise2_orders.refactored.discount;

import exercise2_orders.refactored.model.CustomerType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DiscountStrategyFactory {
    private final Map<CustomerType, Supplier<DiscountStrategy>> strategies;

    public DiscountStrategyFactory() {
        this.strategies = new HashMap<>();

        this.strategies.put(CustomerType.REGULAR, RegularCustomerDiscount::new);
        this.strategies.put(CustomerType.PREMIUM, PremiumCustomerDiscount::new);
        this.strategies.put(CustomerType.VIP, VipCustomerDiscount::new);
        this.strategies.put(CustomerType.EMPLOYEE, EmployeeCustomerDiscount::new);
    }

    public DiscountStrategy createStrategy(CustomerType customerType) {
        if (customerType == null || !strategies.containsKey(customerType)) {
            throw new IllegalArgumentException("Invalid customer type: " + customerType);
        }

        return strategies.get(customerType).get();
    }
}
