package com.ms.security.util;

import com.ms.security.response.ApiResponse;

public class ResponseUtil {

    public static ApiResponse getResponse(String code, String message, Object object){

        ApiResponse response=new ApiResponse();
        response.setCode(code);
        response.setMessage(message);
        response.setResponse(object);
        return response;
    }
}
