package com.sojrel.saccoapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanGuarantorRequestDto {
    private String memberId;
    private Long loanId;
    private double amount;
}


