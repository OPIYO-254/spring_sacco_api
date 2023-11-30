package com.sojrel.saccoapi.flashapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashLoanRequestDto {
     private double principal;
     private LocalDateTime applicationDate;
     private LocalDateTime repayDate;
     private String loanStatus;
     private double amount;
     private String memberId;

}
