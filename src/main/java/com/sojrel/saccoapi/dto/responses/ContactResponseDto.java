package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDto {
    private Long id;
    private LocalDateTime contactDate;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private boolean isRead;
}
