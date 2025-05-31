package com.shino.ecommerce.features.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String sessionId;
    private String message;
}
