package com.ms.security.clients;


import com.ms.security.request.EmailRequest;
import com.ms.security.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "MS-EMAIL", url = "http://localhost:8087")
public interface EmailClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/email")
    ResponseEntity<EmailResponse> sendEmail(EmailRequest request);
}
