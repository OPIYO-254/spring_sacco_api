package com.sojrel.saccoapi.flashapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashRepaymentResponseDto {
    private Long id;
    private Long loanId;
    private double amount;
    private LocalDateTime transactionDate;

}
