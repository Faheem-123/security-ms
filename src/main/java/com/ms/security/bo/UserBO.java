package com.ms.security.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBO {


    private String userId;
    private Integer appId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;


}
