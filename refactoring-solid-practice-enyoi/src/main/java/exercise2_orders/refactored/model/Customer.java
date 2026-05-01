package exercise2_orders.refactored.model;

public class Customer {
    private final String id;
    private final String name;
    private final CustomerType type;

    public Customer(String id, String name, CustomerType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public CustomerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
