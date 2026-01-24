package com.buvette.buvette_backend.enumAttribute;

public enum Types {

    ORDER_PENDING("Order placed", "Your order has been placed and is waiting for confirmation."),
    ORDER_PREPARING("Order in preparation", "Your order is being prepared."),
    ORDER_READY("Order ready", "Your order is ready for pickup."),
    ORDER_DELIVERED("Order delivered", "Your order has been delivered."),
    ORDER_CANCELLED("Order cancelled", "Your order has been cancelled.");

    private final String title;
    private final String messageTemplate;

    Types(String title, String messageTemplate) {
        this.title = title;
        this.messageTemplate = messageTemplate;
    }

    public String getTitle() {
        return title;
    }

    public String buildMessage(String orderCode) {
        // future-proof: can inject order number, shop name, etc.
        return messageTemplate + " (Order #" + orderCode + ")";
    }

    public static Types fromStatus(Status status) {
        return switch (status) {
            case PENDING -> ORDER_PENDING;
            case PREPARING -> ORDER_PREPARING;
            case READY -> ORDER_READY;
            case DELIVERED -> ORDER_DELIVERED;
            case CANCELLED -> ORDER_CANCELLED;
        };
    }
}
