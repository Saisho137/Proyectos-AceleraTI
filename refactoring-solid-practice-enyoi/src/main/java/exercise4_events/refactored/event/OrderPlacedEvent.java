package exercise4_events.refactored.event;

public class OrderPlacedEvent extends BaseEvent {
    private final String orderId;
    private final String email;
    private final String number;
    private final double price;

    public OrderPlacedEvent(String orderId, String email, String number, double price) {
        this.orderId = orderId;
        this.email = email;
        this.number = number;
        this.price = price;
    }

    @Override
    public String getEventType() {
        return "ORDER_PLACED";
    }

    public String getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }
}
