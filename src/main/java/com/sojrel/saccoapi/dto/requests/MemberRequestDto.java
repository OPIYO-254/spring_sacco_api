package com.sojrel.saccoapi.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String firstName;
    private String midName;
    private String lastName;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phone;
    private Long idNo;
    private String kraPin;
    private String gender;
    private String address;
    private String residence;
}
