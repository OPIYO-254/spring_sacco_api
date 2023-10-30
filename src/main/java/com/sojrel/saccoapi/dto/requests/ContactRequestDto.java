package com.sojrel.saccoapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
}
