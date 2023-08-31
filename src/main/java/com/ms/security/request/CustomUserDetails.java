package com.ms.security.request;

import com.ms.security.constants.AppConstants;
import com.ms.security.model.User;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private String userEmail;
    private Integer appId;
    private String password;
    private String firstName;
    private String lastName;
    private String userName;
    private Integer status;
    private Date createdDate;
    private Date modifiedDate;
    private  String  createdBy;
    private  String  modifiedBy;
    private  Integer  firstLogin;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public static CustomUserDetails build(User user) {
        Optional optional = Optional.ofNullable(user);
        CustomUserDetails customUserDetails = null;
        if (optional.isPresent()) {
            customUserDetails = new CustomUserDetails();
            customUserDetails.setUserEmail(user.getEmail());
            customUserDetails.setAppId(AppConstants.APP_ID);
            customUserDetails.setFirstName(user.getFirstName());
            customUserDetails.setLastName(user.getLastName());
            customUserDetails.setUserName(user.getUserName());
            customUserDetails.setPassword(user.getPassword());
            customUserDetails.setStatus(user.getStatus());
//            customUserDetails.setUserName(user.getUserName());
            customUserDetails.setFirstLogin(user.getFirstLogin());


        }
        return customUserDetails;
    }


}
