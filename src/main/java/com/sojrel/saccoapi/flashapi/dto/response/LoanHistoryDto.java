package com.sojrel.saccoapi.flashapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistoryDto {
    private Long loanId;
    private double borrowedAmount;
    private LocalDateTime setRepayDate;
    private double repaidAmount;
    private LocalDateTime actualRepayment;
}
