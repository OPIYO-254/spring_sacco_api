package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.requests.RegistrationRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.model.Role;
import com.sojrel.saccoapi.model.User;
import com.sojrel.saccoapi.repository.RoleRepository;
import com.sojrel.saccoapi.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private TokenService tokenService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RoleRepository roleRepository;

//    @Bean
    @Autowired
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
    public User addUser(RegistrationRequestDto registrationRequestDto, String siteURL) throws MessagingException, UnsupportedEncodingException, ConnectException {
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

        // save user
        userRepository.save(user);

        // send verification email to user
//        sendVerificationEmail(user, siteURL);
//        System.out.println("sending email...");

        return user;
    }

//    public LoginResponseDTO loginUser(LoginRequestDto dto){
//
//        try{
//            System.out.println("logging..");
//            Authentication auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
//            );
//            String token = tokenService.generateJwt(auth);
//            System.out.println(token);
//            User user = userRepository.findByEmail(dto.getEmail());
//            System.out.println("This is the user "+user.getName());
//            return new LoginResponseDTO(user, token);

//        } catch(AuthenticationException e){
//            e.printStackTrace();
//            return new LoginResponseDTO(null, "");
//        }
//    }


    private void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "sojrelsacco@gmail.com";
        String senderName = "Sojrel Sacco";
        String subject = "Account verification";
        String content = "Dear [[name]],<br>"
                            + "Please click the link below to verify your registration:<br>"
                            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                            + "Thank you,<br>"
                            + "Sojrel Sacco Ltd";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        System.out.println(user.getName());
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
