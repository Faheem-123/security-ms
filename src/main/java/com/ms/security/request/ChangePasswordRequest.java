package com.ms.security.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String newPassword;
    private String confirmPassword;
}
