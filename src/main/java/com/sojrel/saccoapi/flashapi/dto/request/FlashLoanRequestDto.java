package com.sojrel.saccoapi.flashapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashLoanRequestDto {
     private double principal;
     private String repayDate;
     private String loanStatus;
     private double amount;
     private String memberId;

}
