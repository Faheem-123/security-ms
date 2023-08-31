package com.ms.security.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpRequest {

    private String email;
    private String  otp;
}
