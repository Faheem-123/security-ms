package com.ms.security.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponse {
    private Integer code;
    private String message;
}
