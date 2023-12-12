package com.sojrel.saccoapi.flashapi.dto.request;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashRepaymentRequestDto {
    private Long loanId;
    private double amount;
}
