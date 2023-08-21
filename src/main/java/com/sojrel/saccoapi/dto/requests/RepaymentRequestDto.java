package com.sojrel.saccoapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentRequestDto {
    private double amount;
    private Long loanId;
}
