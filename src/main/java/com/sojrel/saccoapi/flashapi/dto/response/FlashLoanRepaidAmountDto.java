package com.sojrel.saccoapi.flashapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashLoanRepaidAmountDto {
    private Long loanId;
    private double amount;
}
