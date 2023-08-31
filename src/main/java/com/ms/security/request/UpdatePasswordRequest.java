package com.ms.security.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
