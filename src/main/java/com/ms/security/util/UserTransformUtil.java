package com.ms.security.util;

import com.ms.security.bo.UserBO;
import com.ms.security.model.User;
import com.ms.security.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserTransformUtil {

    public User transform(UserRequest request){

        User user=new User();
        user.setAppId(request.getAppId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
      //  user.setIsPioneerUser(request.getIsPioneerUser());
        user.setCountryCode(request.getCountryCode());
        user.setRegionCode(request.getRegionCode());

        return user;
    }

    public UserBO transformUser(User request){

        UserBO user=new UserBO();
        user.setUserId(request.getUserId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        return user;
    }
}
