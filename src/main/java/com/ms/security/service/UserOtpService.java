package com.ms.security.service;

import com.ms.security.dao.UserOtpRepository;
import com.ms.security.model.UserOtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOtpService {
    @Autowired
    private UserOtpRepository userOtpRepository;

    public UserOtp saveOtp(UserOtp userOtp) {
        UserOtp userOtpDB=userOtpRepository.save(userOtp);
        return userOtpDB;
    }

    public UserOtp findByOTP(String otp) {
        UserOtp userOtpDB=userOtpRepository.findByOtp(otp);
        return userOtpDB;
    }

    public void updateUserOTPStatusByEmail(String email) {
        userOtpRepository.updateUserOTPStatusByEmail(email);

    }
}
