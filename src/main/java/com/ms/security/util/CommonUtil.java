package com.ms.security.util;

import com.ms.security.model.Model;

import java.sql.Date;

public class CommonUtil {

    public static Model getCreatedUser(String createdUser){
        Model model=new Model();
        Date currentDate = new Date(System.currentTimeMillis());
        model.setCreatedDate(currentDate);
        model.setCreatedBy(createdUser);
        return model;

    }

    public static String getRandomPassword(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";


        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {


            int index = (int)(AlphaNumericString.length() * Math.random());


            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static String getRandomOTP(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {


            int index = (int)(AlphaNumericString.length() * Math.random());


            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}