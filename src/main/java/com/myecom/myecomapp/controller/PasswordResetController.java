package com.myecom.myecomapp.controller;

import com.myecom.myecomapp.service.PasswordResetTokenService;
import com.myecom.myecomapp.service.UserService;
import com.myecom.myecomapp.serviceimpl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @GetMapping("/forgetPassword")
    public String forgetPassword() {
        return "password_recovery/forget_password";
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@RequestParam("email") String email, Model model) {
        String otp = tokenService.createPasswordToken(email);

        if (otp == null) {
            model.addAttribute("error", "Email does not exist");
            return "password_recovery/forget_password";
        }
        emailService.sendOtpEmail(email, otp);

        model.addAttribute("email", email);

        return "password_recovery/verify_otp";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {

        boolean valid = tokenService.validateOtp(email, otp);

        if (!valid) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid or expired OTP");
            return "password_recovery/verify_otp";
        }
        model.addAttribute("email", email);
        return "password_recovery/reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword, Model model) {
        userService.updatePassword(email,  newPassword);

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Passwords do not match");
            return "password_recovery/reset_password";
        }

        tokenService.markTokenAsUsed(email);

        model.addAttribute("email", email);

        return "/login";
    }
}
