package com.ms.security.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {
    private Integer code;
    private String message;
}
