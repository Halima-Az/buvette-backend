package com.buvette.buvette_backend.enumAttribute;

public enum Status {
    PENDING,
    PREPARING,
    READY,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(Status next) {
        return switch (this) {
            case PENDING -> next == PREPARING || next == CANCELLED;
            case PREPARING -> next == READY || next == CANCELLED;
            case READY -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

}
