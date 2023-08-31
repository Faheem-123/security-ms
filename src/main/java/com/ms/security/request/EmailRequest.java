package com.ms.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailRequest {

    @JsonProperty(value = "to")
    private String to;


    @JsonProperty(value = "subject")
    private String subject;


    @JsonProperty(value = "body")
    private String body;
}
