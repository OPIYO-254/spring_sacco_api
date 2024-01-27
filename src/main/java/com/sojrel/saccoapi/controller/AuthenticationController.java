package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.requests.RegistrationRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.exceptions.UserNotFoundException;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.User;
import com.sojrel.saccoapi.service.MemberService;
import com.sojrel.saccoapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
//@CrossOrigin(origins={"http://10.0.2.2:8080"})
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegistrationRequestDto registrationRequestDto, HttpServletRequest request){
        String siteURl = getSiteURL(request);
        Member member = memberService.getMemberByEmail(registrationRequestDto.getEmail());
        if(member!=null) {
            try {
                userService.addUser(registrationRequestDto,siteURl);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Registered Successfully. \nPlease check your email to verify your account.");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                log.error("error in creating account: "+e.getLocalizedMessage());
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Error creating account. Try again");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
        }
        else{
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "You are not authorized to signup at this moment. Contact admin.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

//        catch (Exception e){
//            Map<String, String> response = new HashMap<>();
//            response.put("status", "error");
//            response.put("message", "You are not authorized to create signup. Contact admin.");
//            log.error("error "+e.getLocalizedMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(response);
//        }


    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(HttpServletRequest request, Model model) throws UserNotFoundException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        Map<String, String> response = new HashMap<>();
        try {
            userService.setResetPasswordToken(email, token);
            String resetPasswordLink = getSiteURL(request) + "/reset-password?token=" + token;
            userService.sendResetEmail(email, resetPasswordLink);
            response.put("status", "success");
            response.put("message", "We have sent a reset password link to your email.");
        }catch (Exception e){
            log.error("error sending email "+e.getLocalizedMessage());
            response.put("error", "error in sending email");
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getUserByToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
//			return "message";
        } else {
            userService.resetPassword(user, password);
            model.addAttribute("message", "Password reset successful");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){
        Map<String, String> response = new HashMap<>();
        try {
            LoginResponseDTO loginResponseDTO = userService.loginUser(dto);
            String token = loginResponseDTO.getToken();
            String email = loginResponseDTO.getUser().getEmail();
            Member member = memberService.getMemberByEmail(email);
            String id = member.getId();
            if(member.getIsActive()) {
                response.put("status", "success");
                response.put("message", "Login successful!");
                response.put("token", token);
                response.put("id", id);
            }
            else{
                response.put("status", "inactive");
                response.put("message", "You account is not active. Please contact admin.");
            }
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            log.error("Authentication error "+e.getLocalizedMessage());
            response.put("status", "error");
            response.put("message", "Invalid login credentials. Check and try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }
}
