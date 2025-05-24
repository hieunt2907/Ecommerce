package com.shino.ecommerce.features.user.dto.response;

public class CreateUserResponse {
    private String message;
    private String userId;

    public CreateUserResponse() {
    }

    public CreateUserResponse(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    } 

}