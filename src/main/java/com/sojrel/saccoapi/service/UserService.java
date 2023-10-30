package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.requests.RegistrationRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.model.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

@Service
public interface UserService {
    public User addUser(RegistrationRequestDto registrationRequestDto, String siteURL) throws MessagingException, UnsupportedEncodingException, ConnectException;

    public boolean verifyUser(String verificationCode);

//    public LoginResponseDTO loginUser(LoginRequestDto dto);

}
