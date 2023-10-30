package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.LoginRequestDto;
import com.sojrel.saccoapi.dto.responses.LoginResponseDTO;
import com.sojrel.saccoapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/login",produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){
        try {
//            LoginResponseDTO loginResponseDTO = userService.loginUser(dto);
//            String token = loginResponseDTO.getToken();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login Successfully.");
//            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Login error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
