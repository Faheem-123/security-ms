package com.ms.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ms.security.bo.UserBO;
import com.ms.security.util.UserTransformUtil;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String token;
    private String refreshToken;
    private Boolean isPasswordChange =false;
    private Boolean isPaidUser=false;
    private String otp;
    private UserBO userBO;
}
