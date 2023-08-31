package com.ms.security.util;

import com.ms.security.dao.UserOtpRepository;
import com.ms.security.model.UserOtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OtpDeActiveScheduler {
    @Autowired
    private UserOtpRepository userOtpRepository;

    @Scheduled(cron = "0 */1 * ? * *")
    public void scheduleTask() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");


        LocalDateTime currentDateAndTime = LocalDateTime.now();

        Date currentDateAndTime1 = java.sql.Timestamp.valueOf(currentDateAndTime);


        List<UserOtp> userOtpList = userOtpRepository.findByStatus(1);
        if (userOtpList != null && userOtpList.size() > 0) {
            for (UserOtp userOtp : userOtpList) {
                String optCreationTime = userOtp.getOtpTime();
                Date formattedOptCreationTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(optCreationTime);
                System.out.println("Otp Cron Job Executed");
                long diff = currentDateAndTime1.getTime() - formattedOptCreationTime.getTime();//as given
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                if (minutes >= 3) {
                    userOtpRepository.updateUserOTPStatus(userOtp.getOtp());
                    System.out.println("Otp " + userOtp.getOtp() + " is Expired");
                }
            }//end For
        }


    }
}
