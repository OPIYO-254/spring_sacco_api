package com.sojrel.saccoapi.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        var authaurities = authentication.getAuthorities();
        var roles = authaurities.stream().map(r -> r.getAuthority()).findFirst();
        if (roles.orElse("").equals("MANAGER")) {
            response.sendRedirect("/home");
        } else if (roles.orElse("").equals("USER")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/error");
        }

    }
}
