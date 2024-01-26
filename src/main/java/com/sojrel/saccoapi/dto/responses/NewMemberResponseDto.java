package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMemberResponseDto {
    private String id;
    private String firstName;
    private String midName;
    private String lastName;
    private Long idNo;
    private String email;
    private String phone;
    private String residence;
}
