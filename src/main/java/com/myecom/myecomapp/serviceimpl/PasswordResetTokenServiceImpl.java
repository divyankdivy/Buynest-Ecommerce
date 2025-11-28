package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.PasswordResetToken;
import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.repository.PasswordResetTokenRepository;
import com.myecom.myecomapp.repository.UserRepository;
import com.myecom.myecomapp.service.PasswordResetTokenService;
import com.myecom.myecomapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private UserRepository userRepo;

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public String createPasswordToken(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return null; // email does not exist
        }

        // Delete previous token if exists
        tokenRepo.deleteByEmail(email);

        String otp = generateOtp();

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setToken(otp);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);

        tokenRepo.save(token);

        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {

        PasswordResetToken token = tokenRepo.findByEmail(email);

        if (token == null) return false;
        if (token.getUsed()) return false;
        if (!token.getToken().equals(otp)) return false;
        if (token.getExpiryTime().isBefore(LocalDateTime.now())) return false;

        return true;
    }

    @Override
    public void markTokenAsUsed(String email) {
        PasswordResetToken token = tokenRepo.findByEmail(email);
        if (token != null) {
            token.setUsed(true);
            tokenRepo.save(token);
        }
    }
}
