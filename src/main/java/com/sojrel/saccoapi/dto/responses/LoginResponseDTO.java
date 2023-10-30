package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private User user;
    private String token;
}
