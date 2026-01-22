package com.buvette.buvette_backend.dto.worker;

import com.buvette.buvette_backend.enumAttribute.Status;

public class UpdateOrderStatusRequest {
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
