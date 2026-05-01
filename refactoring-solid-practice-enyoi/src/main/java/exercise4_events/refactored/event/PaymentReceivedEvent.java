package exercise4_events.refactored.event;

public class PaymentReceivedEvent extends BaseEvent {
    private final String paymentId;
    private final String orderId;
    private final double amount;
    private final String email;

    public PaymentReceivedEvent(String paymentId, String orderId, double amount, String email) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }

    @Override
    public String getEventType() {
        return "PAYMENT_RECEIVED";
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }
}
