package com.ms.security.controller;

import com.ms.security.dao.UserRepository;
import com.ms.security.model.User;
import com.ms.security.model.UserOtp;
import com.ms.security.request.*;
import com.ms.security.response.ApiResponse;
import com.ms.security.service.AuthenticatorService;
import com.ms.security.service.EmailService;
import com.ms.security.service.UserOtpService;
import com.ms.security.util.CommonUtil;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Random;


@RestController
@CrossOrigin
@RequestMapping("/public")
public class AuthenticatorController {

    @Autowired
    private AuthenticatorService authenticatorService;
@Autowired
private EmailService emailService;
@Autowired
private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserOtpService userOtpService;


    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody LoginRequest request) {
        ApiResponse response = authenticatorService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest request) {

        ApiResponse response = authenticatorService.createUser(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/api/v1/update-password/{email}")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePasswordRequest request,@PathVariable String email) {

        ApiResponse response = authenticatorService.updatePassword(request,email);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/api/v1/forgot-password")
    public String sendOtp(@RequestParam("email") String email, HttpSession session) {
    //generate code for otp

        String otp = CommonUtil.getRandomOTP(6);
//        Random randomOtp = new Random(1000);
//        int otp=randomOtp.nextInt(99999);
        System.out.println(otp);
        //code for otp send to email

        String to=email;
//        String from="faheempanhwar62@gmail.com";
        String subject="OTP generate";
        String content="Here is your OTP !!"+otp+" ";
        //call email web api usinf RestTemplate microservices

      boolean f=  emailService.sendEmail(subject,content,to);

      if(f){
          String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
          UserOtp userOtp=new UserOtp();
          userOtp.setOtp(otp);
          userOtp.setEmail(email);
          userOtp.setOtpTime(timeStamp);
          userOtp.setStatus(1);
          this.userOtpService.saveOtp(userOtp);
//          session.setAttribute("sessionEmail",email);
//          session.setAttribute("sessionOTP",otp);
          return "OTP sent to your email!!";
      }else {
          return "Your email id is not right!!";
      }


    }

    @PostMapping("/api/v1/verify-otp")
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody ValidateOtpRequest request) {
        ApiResponse response = authenticatorService.validateOtp(request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/api/v1/verify-otp")
//    public String verifyOtp(@RequestParam("otp") String otp, HttpSession session){
//
//        String sessionOtp= (String) session.getAttribute("sessionOTP");
//        String sessionEmail= (String) session.getAttribute("sessionEmail");
//        if(sessionOtp==otp){
//
//            User user = this.userRepository.getUserByEmail(sessionEmail);
//            if(user==null){
//
//                return "User does not exit with this email!!";
//            }
////            else {
////
////            }
//            return "OTP matched successfully";
//        }
//        else{
//            return "Otp not matched !!";
//        }
//    }

    @PostMapping("/api/v1/change-password/{email}")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable String email) {

        ApiResponse response = authenticatorService.changePa-ssword(changePasswordRequest,email);
        return ResponseEntity.ok(response);

    }

//    @PostMapping("/api/v1/change-password")
//    public String changePassword(@RequestParam("new-password") String newPassword, HttpSession session) {
//
//        String sessionEmail=(String)session.getAttribute("sessionEmail");
//
//        User user = this.userRepository.getUserByEmail(sessionEmail);
//        user.setPassword(this.passwordEncoder.encode(newPassword));
////        user.setPassword(newPassword);
//        this.userRepository.save(user);
//        return "Password changed successfuly !!";
//    }


    @PostMapping("/api/v1/validate-otp")
    public ResponseEntity<ApiResponse> validateOTP(@RequestBody ValidateOtpRequest request) {
        ApiResponse response = authenticatorService.validateOtp(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/v1/resent-otp")
    public ResponseEntity<ApiResponse> resentOtp(@RequestParam("email") String email) {
        ApiResponse response = authenticatorService.resentOtp(email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/api/v1/test-api")
    public String test() throws Exception {

       final String ZAP_PROXY_HOST = "localhost";
       final int ZAP_PROXY_PORT = 8080;
        String TARGET_URL = "https://public-firing-range.appspot.com";

        try {
            disableCertificateValidation();
            // Create a new instance of the ZAP API client
            ClientApi clientApi = new ClientApi(ZAP_PROXY_HOST, ZAP_PROXY_PORT);

            // Start the ZAP daemon
            clientApi.core.newSession("","");

            // Access the target URL through ZAP proxy
            clientApi.accessUrl(TARGET_URL);

            // Spider the target URL to discover all accessible URLs
            clientApi.spider.scan(TARGET_URL, null, null, null, null);

            // Wait for the spider to finish
            while (true) {
                Thread.sleep(1000);
                String status=clientApi.spider.status(TARGET_URL).getName();
                if ("100".equals(status)) {
                    break;
                }
            }

            // Scan the URLs discovered by the spider for vulnerabilities
            clientApi.ascan.scan(TARGET_URL, null, null, null, null, null);

            // Wait for the active scan to finish
            while (true) {
                Thread.sleep(5000);
                if ("100".equals(clientApi.ascan.status(null).getName())) {
                    break;
                }
            }

            // Get the alerts (vulnerabilities) found during the scan
            List<Alert> alerts = clientApi.getAlerts(null, 0, 0);
            for (Alert alert : alerts) {
                System.out.println(alert.getAlert());
            }

            // Shutdown ZAP
            clientApi.core.shutdown();

        } catch (ClientApiException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void disableCertificateValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Disable hostname verification
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
