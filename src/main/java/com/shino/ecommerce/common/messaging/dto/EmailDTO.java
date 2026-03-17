package com.shino.ecommerce.common.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmailDTO implements java.io.Serializable {
    private String to;
    private String subject;
    private String body;
    
}
