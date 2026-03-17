package com.shino.ecommerce.features.user.dto.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String sessionId;
    private String newPassword;
}
