package com.ms.security.dao;

import com.ms.security.model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_OTP u set STATUS =0 where u.otp=:otp", nativeQuery = true)
    void updateUserOTPStatus(@Param("otp") String otp);


    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_OTP u set STATUS =0 where u.email=:email", nativeQuery = true)
    void updateUserOTPStatusByEmail(@Param("email") String email);

    List<UserOtp> findByStatus(Integer status);

    UserOtp findByOtp(String otp);
}
