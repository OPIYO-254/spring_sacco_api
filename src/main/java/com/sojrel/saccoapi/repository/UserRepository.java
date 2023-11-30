package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String verificationCode);

    User findByResetPasswordToken(String token);


}
