package com.myecom.myecomapp.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender  mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Buyest - Password Reset OTP");
        message.setText("Your OTP for resetting password is: " + otp
                + "\n\nThis OTP is valid for 10 minutes.");

        mailSender.send(message);
    }
}
