package com.ms.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Model {

    @Column(name="CREATED_DATE")
    private  Date createdDate;

    @Column(name="MODIFIED_DATE")
    private Date modifiedDate;


    @Column(name="CREATED_BY")
    private  String  createdBy;


    @Column(name="MODIFIED_BY")
    private  String  modifiedBy;


}
