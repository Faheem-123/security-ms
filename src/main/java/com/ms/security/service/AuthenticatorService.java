package com.ms.security.service;

import com.ms.security.bo.UserBO;
import com.ms.security.clients.EmailClient;
import com.ms.security.clients.PaymentClient;
import com.ms.security.constants.AppConstants;
import com.ms.security.dao.UserRepository;
import com.ms.security.jwtutils.TokenManager;
import com.ms.security.model.User;
import com.ms.security.model.UserOtp;
import com.ms.security.request.*;
import com.ms.security.response.ApiResponse;
import com.ms.security.response.LoginResponse;
import com.ms.security.util.CommonUtil;
import com.ms.security.util.ResponseUtil;
import com.ms.security.util.UserTransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Service
public class AuthenticatorService {
    @Autowired
    private PasswordEncoder bcryptEncoder;


    @Autowired
    private TokenManager tokenManager;


    @Autowired
    private AuthenticatorService authenticatorService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOtpService userOtpService;

    @Autowired
    private UserTransformUtil userTransformUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private EmailClient emailClient;


    @Autowired
    private SpringTemplateEngine emailTemplate;

    @Value("${email.template.path}")
    private String emailTemplatePath;

    @Value("${email.template.otp}")
    private String emailTemplateOtp;


    public ApiResponse authenticate(LoginRequest request) {
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        ApiResponse response = null;
        LoginResponse loginResponse = null;
        String email = request.getEmail();
        Integer appId = 1;
        Authentication authentication = null;
        if (!Objects.isNull(userDetails)) {

            try {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // if (isUserPaid(email)) {
                if (true) {
                    // if (isPasswordModified(email, appId)) {
                    if (true) {
                        if (authentication != null && authentication.isAuthenticated() == true) {
                            final String jwtToken = tokenManager.generateJwtToken(userDetails);
                            User dbUSer = userRepository.findByEmail(email);
                            UserBO userBO = userTransformUtil.transformUser(dbUSer);
                            loginResponse = LoginResponse.builder().token(jwtToken).refreshToken("").isPasswordChange(userDetails.getFirstLogin() == 0 ? false : true).userBO(userBO).build();
                            if (loginResponse != null && loginResponse.getIsPasswordChange()) {
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

                                String otp = CommonUtil.getRandomOTP(6);
                                UserOtp userOtp = new UserOtp();
                                userOtp.setEmail(request.getEmail());
                                userOtp.setOtp(otp);
                                userOtp.setOtpTime(timeStamp);
                                userOtp.setStatus(1);
                                UserOtp userOtpDB = userOtpService.saveOtp(userOtp);
                                loginResponse.setOtp(userOtpDB.getOtp());

                                EmailRequest emailRequest = new EmailRequest();
                                emailRequest.setTo(request.getEmail());
                                emailRequest.setSubject("One Time Password");
                                emailRequest.setBody(generateOTpEmail(userOtpDB.getOtp()));
                                emailClient.sendEmail(emailRequest);
                            }

                            response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_LOGIN_MESSAGE, loginResponse);
                        }

                    } else {
                        loginResponse = LoginResponse.builder().isPasswordChange(false).isPaidUser(true).build();
                        response = ResponseUtil.getResponse(AppConstants.FIRST_LOGIN, AppConstants.FIRST_LOGIN_MESSAGE, loginResponse);
                    }

                } else {
                    loginResponse = LoginResponse.builder().isPaidUser(false).build();
                    response = ResponseUtil.getResponse(AppConstants.UN_PAID, AppConstants.UN_PAID_MESSAGE, loginResponse);
                }


            } catch (Exception e) {
                response = ResponseUtil.getResponse(AppConstants.INVALID_CREDENTIALS, AppConstants.INVALID_CREDENTIALS_MESSAGE, request);

            }


        } else {

            response = ResponseUtil.getResponse(AppConstants.FAILED_CODE, AppConstants.FAILED_LOGIN_MESSAGE, request);
        }
        return response;
    }

    private boolean isUserPaid(String email) {
        ApiResponse response = paymentClient.getPaymentInfo(email);
        String code = response.getCode();
        return "404".equals(code) ? false : true;
    }

    private Boolean isPasswordModified(String email, Integer appId) {
        User user = userRepository.findByEmailAndAppId(email, appId);
        Date modifiedDate = user.getModifiedDate();
        return modifiedDate != null ? true : false;
    }


    public ApiResponse createUser(UserRequest request) {
        ApiResponse response = null;
        User dbUser = null;
        String randomPassword = CommonUtil.getRandomPassword(15);
        String encodePassword = passwordEncoder.encode(randomPassword);
        request.setPassword(encodePassword);

        User user = userTransformUtil.transform(request);
        User userByEmail = userRepository.findByEmailAndAppId(request.getEmail(), 1);
        if (userByEmail != null) {

            response = ResponseUtil.getResponse(AppConstants.ALREADY_EXITS, AppConstants.ALREADY_EXITS_MESSAGE, null);
        } else {

            Date currentDate = new Date(System.currentTimeMillis());
            user.setCreatedDate(currentDate);
            user.setCreatedBy("Created User");
            user.setFirstLogin(0);


            dbUser = userRepository.save(user);

             //cal for keyclcok create user
            if (dbUser != null) {
                UserBO userBO = userTransformUtil.transformUser(dbUser);
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setTo(request.getEmail());
                emailRequest.setSubject("User Account Information");
                emailRequest.setBody(generateEmail(randomPassword));
                emailClient.sendEmail(emailRequest);
                response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.CREATE_USER_MESSAGE, userBO);

            } else {

                response = ResponseUtil.getResponse(AppConstants.SERVER_ERROR, AppConstants.SERVER_ERROR_MESSAGE, null);
            }

        }


        return response;
    }

    public ApiResponse updatePassword(UpdatePasswordRequest request, String email) {
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();
        String oldPassword = request.getOldPassword();
        ApiResponse response = null;
        User dbUSer = userRepository.findByEmail(email);
        //  boolean isPasswordMatched = bcryptEncoder.matches(password, hashedPassword);

        if (dbUSer == null) {
            response = ResponseUtil.getResponse(AppConstants.INVALID_EMAIL_CODE, AppConstants.INVALID_EMAIL_CODE_MESSAGE, null);
        } else if (oldPassword.equals(newPassword)) {
            response = ResponseUtil.getResponse(AppConstants.FAILED_CODE, AppConstants.FAILED_UPDATE_PASSWORD_MESSAGE, null);
        } else if (!newPassword.equals(confirmPassword)) {
            response = ResponseUtil.getResponse(AppConstants.PASSWORD_NOT_SAME, AppConstants.PASSWORD_NOT_SAME_MESSAGE, null);
        } else {
            boolean isPasswordMatched = bcryptEncoder.matches(request.getOldPassword(), dbUSer.getPassword());
            if (!isPasswordMatched) {
                response = ResponseUtil.getResponse(AppConstants.INVALID_OLD_PASSWORD_CODE, AppConstants.INVALID_OLD_PASSWORD_MESSAGE, null);
            } else {

                User user = userRepository.findByEmailAndAppId(email, AppConstants.APP_ID);
                String encodePassword = passwordEncoder.encode(request.getConfirmPassword());
                user.setPassword(encodePassword);
                user.setModifiedDate(new Date(System.currentTimeMillis()));
                user.setFirstLogin(1);

                userRepository.save(user);
                response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_UPDATE_PASSWORD_MESSAGE, null);
            }
        }


        return response;
    }



    public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest, String email) {
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmPassword = changePasswordRequest.getConfirmPassword();
//        String oldPassword = request.getOldPassword();
        ApiResponse response = null;
        User dbUser = userRepository.findByEmail(email);
        //  boolean isPasswordMatched = bcryptEncoder.matches(password, hashedPassword);

        if (dbUser == null) {
            response = ResponseUtil.getResponse(AppConstants.INVALID_EMAIL_CODE, AppConstants.INVALID_EMAIL_CODE_MESSAGE, null);
        }
//        else if (oldPassword.equals(newPassword)) {
//            response = ResponseUtil.getResponse(AppConstants.FAILED_CODE, AppConstants.FAILED_UPDATE_PASSWORD_MESSAGE, null);
//        }
        else if (!newPassword.equals(confirmPassword)) {
            response = ResponseUtil.getResponse(AppConstants.PASSWORD_NOT_SAME, AppConstants.PASSWORD_NOT_SAME_MESSAGE, null);
        }
//        else {
//            boolean isPasswordMatched = bcryptEncoder.matches(request.getOldPassword(), dbUSer.getPassword());
//            if (!isPasswordMatched) {
//                response = ResponseUtil.getResponse(AppConstants.INVALID_OLD_PASSWORD_CODE, AppConstants.INVALID_OLD_PASSWORD_MESSAGE, null);
//            }
            else {

                User user = userRepository.findByEmail(email);

//                String encodePassword = passwordEncoder.encode(changePasswordRequest.getConfirmPassword());
                user.setPassword(changePasswordRequest.getConfirmPassword());
                user.setModifiedDate(new Date(System.currentTimeMillis()));
//                user.setFirstLogin(1);

                userRepository.save(user);
                response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_UPDATE_PASSWORD_MESSAGE, null);
            }



        return response;
    }


    private String generateEmail(String password) {
        StringBuilder html = new StringBuilder();
        String result = "";
        try {
            FileReader fr = new FileReader(emailTemplatePath);
            BufferedReader br = new BufferedReader(fr);

            String val;

            // Reading the String till we get the null
            // string and appending to the string
            while ((val = br.readLine()) != null) {
                html.append(val);
            }


            result = html.toString();
            br.close();

        } catch (Exception e) {

        }
        result = result.replace("${password}", password);

        return result;

    }

    private String generateOTpEmail(String otp) {
        StringBuilder html = new StringBuilder();
        String result = "";
        try {
            FileReader fr = new FileReader(emailTemplateOtp);
            BufferedReader br = new BufferedReader(fr);

            String val;

            // Reading the String till we get the null
            // string and appending to the string
            while ((val = br.readLine()) != null) {
                html.append(val);
            }


            result = html.toString();
            br.close();

        } catch (Exception e) {

        }
        result = result.replace("${otp}", otp);

        return result;

    }

    public ApiResponse validateOtp(ValidateOtpRequest request) {
        String otp = request.getOtp();
        String email = request.getEmail();

        ApiResponse response = null;

        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            response = ResponseUtil.getResponse(AppConstants.INVALID_EMAIL_CODE, AppConstants.INVALID_EMAIL_CODE_MESSAGE, null);
        } else {
            UserOtp userOtp = userOtpService.findByOTP(otp);
            if (userOtp == null) {
                response = ResponseUtil.getResponse(AppConstants.INVALID_OTP_CODE, AppConstants.INVALID_OTP_CODE_MESSAGE, otp);
            } else {
                Integer status = userOtp.getStatus();
                if (status == 0) {
                    response = ResponseUtil.getResponse(AppConstants.OTP_CODE_EXPIRED, AppConstants.OTP_CODE_EXPIRED_MESSAGE, otp);
                } else {
                    response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.OTP_CODE_VALIDATED_MESSAGE, true);
                }
            }
        }
        return response;
    }

    public ApiResponse resentOtp(String email) {


        ApiResponse response = null;

        User dbUSer = userRepository.findByEmail(email);
        if (dbUSer == null) {
            response = ResponseUtil.getResponse(AppConstants.INVALID_EMAIL_CODE, AppConstants.INVALID_EMAIL_CODE_MESSAGE, null);
        } else {
            userOtpService.updateUserOTPStatusByEmail(email);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            String otp = CommonUtil.getRandomOTP(6);
            UserOtp userOtp = new UserOtp();
            userOtp.setEmail(email);
            userOtp.setOtp(otp);
            userOtp.setOtpTime(timeStamp);
            userOtp.setStatus(1);
            UserOtp userOtpDB = userOtpService.saveOtp(userOtp);

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(email);
            emailRequest.setSubject("One Time Password");
            emailRequest.setBody(generateOTpEmail(userOtpDB.getOtp()));
            emailClient.sendEmail(emailRequest);
            response = ResponseUtil.getResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_SENT_OTP, otp);
        }
        return response;
    }
}
