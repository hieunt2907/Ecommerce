package com.shino.ecommerce.features.user.dto.request;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String sessionId;
    private String otp;
}
