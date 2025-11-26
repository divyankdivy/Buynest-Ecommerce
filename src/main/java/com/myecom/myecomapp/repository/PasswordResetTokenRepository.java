package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    public PasswordResetToken findByEmail(String email);

    public PasswordResetToken findByToken(String token);

    public void deleteByEmail(String email);
}
