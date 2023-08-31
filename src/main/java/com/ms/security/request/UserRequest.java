package com.ms.security.request;

import lombok.Data;

@Data
public class UserRequest {


    private String userId;
    private Integer  appId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private Integer status;
   // private Integer isPioneerUser;
    private String countryCode;
    private String regionCode;


}
