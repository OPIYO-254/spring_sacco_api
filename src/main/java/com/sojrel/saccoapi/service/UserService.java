package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.requests.RegistrationRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.exceptions.UserNotFoundException;
import com.sojrel.saccoapi.model.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

@Service
public interface UserService {
    public User addUser(RegistrationRequestDto registrationRequestDto, String siteURL) throws MessagingException, UnsupportedEncodingException, ConnectException;

    public void sendResetEmail(String email, String url) throws MessagingException, UnsupportedEncodingException;

    public boolean verifyUser(String verificationCode);

        public LoginResponseDTO loginUser(LoginRequestDto dto);
    private void sendVerificationEmail(User user, String siteURL) {

    }
//    public void sendResetEmail(String recipientEmail, String link);
    public void setResetPasswordToken(String email, String token) throws UserNotFoundException;
    public User getUserByToken(String token);

    public void resetPassword(User user, String newPassword);

//    public User sendResetEmail(String email, String url);
}
