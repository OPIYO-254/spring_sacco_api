package com.sojrel.saccoapi.dto.requests;

import com.sojrel.saccoapi.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFeeRequestDto {
    private Date payDate;
    private int amount;
    private String memberId;
}
