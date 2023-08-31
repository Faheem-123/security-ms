package com.ms.security.model;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Target;

@Entity
@Table(name = "USERS")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User{


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "APP_ID")
    private Integer appId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;


    @Column(name = "STATUS")
    private Integer status;

    /*@Column(name = "IS_PIONEER_USER")
    private Integer isPioneerUser;*/

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="MODIFIED_DATE")
    private Date modifiedDate;


    @Column(name="CREATED_BY")
    private  String  createdBy;


    @Column(name="MODIFIED_BY")
    private  String  modifiedBy;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "REGION_CODE")
    private String regionCode;

    @Column(name = "IS_FIRST_LOGIN")
    private Integer firstLogin;



}
