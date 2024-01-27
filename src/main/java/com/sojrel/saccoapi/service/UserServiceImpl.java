package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.requests.RegistrationRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.exceptions.UserNotFoundException;
import com.sojrel.saccoapi.model.Role;
import com.sojrel.saccoapi.model.User;
import com.sojrel.saccoapi.repository.RoleRepository;
import com.sojrel.saccoapi.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RoleRepository roleRepository;

//    @Bean
//    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }
    @Override
    public User addUser(RegistrationRequestDto registrationRequestDto, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        User user = new User();
        user.setName(registrationRequestDto.getName());
        user.setEmail(registrationRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        user.setAuthorities(authorities);

        // generate verification code to use in user activation
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        //System.out.println(randomCode);

        boolean send = sendVerificationEmail(user, siteURL);
        // save user
        if(send) {
            userRepository.save(user);
        }
        return user;
    }

    public LoginResponseDTO loginUser(LoginRequestDto dto){

        try{
//            System.out.println("logging..");
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            String token = tokenService.generateJwt(auth);
//            System.out.println(token);
            User user = userRepository.findByEmail(dto.getEmail());
//            System.out.println("This is the user "+user.getName());
            return new LoginResponseDTO(user, token);

        } catch(AuthenticationException e){
//            e.printStackTrace();
            log.error("authentication error "+ e.getLocalizedMessage());
            return new LoginResponseDTO(null, "");
        }
    }


    private boolean sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        boolean send = false;
        String toAddress = user.getEmail();
        String fromAddress = "sojrelsacco@gmail.com";
        String senderName = "Sojrel Sacco";
        String subject = "Account Verification and Activation";
        String content = "Hello [[name]],<br>"
                            +"You are receiving this email because you tried to create account with us. "
                            +"Please click the button below to verify your registration:<br>"
                            + "<h1><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h1><br><br>"
                            + "Thank you.<br>"
                            + "Sojrel Sacco Management.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        try {
            mailSender.send(message);
            send = true;
        }
        catch (Exception e){
            log.error("Sending error: " + e.getLocalizedMessage());
        }
        return  send;
    }

    public void setResetPasswordToken(String email, String token) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user != null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException("Unable to find user with email "+email);
        }
    }
    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void resetPassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public void sendResetEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("sojrelsacco@gmail.com", "Sojrel Sacco");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<h1><a href=\"" + link + "\">Change my password</a></h1>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
//        System.out.println(user.getName());
        if (user == null || user.isEnabled()) {
            return false;

        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }


}
