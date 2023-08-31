package com.ms.security.clients;

import com.ms.security.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "MS-PAYMENT", url = "http://localhost:8084/payment")
public interface PaymentClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/payment-info")
    ApiResponse getPaymentInfo(@RequestParam("email") String email);


}
