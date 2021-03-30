package com.github.prgrms.orders;

import javax.validation.constraints.NotEmpty;

public class RejectRequestBody {
    @NotEmpty(message = "message must be provided")
    private String message;

    public RejectRequestBody() {}

    public RejectRequestBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
