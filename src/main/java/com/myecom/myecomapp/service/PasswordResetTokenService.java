package com.myecom.myecomapp.service;

public interface PasswordResetTokenService {

    public String createPasswordToken(String email);

    public boolean validateOtp(String email, String otp);

    public void markTokenAsUsed(String email);
}
