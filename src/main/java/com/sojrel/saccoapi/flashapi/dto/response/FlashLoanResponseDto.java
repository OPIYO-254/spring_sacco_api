package com.sojrel.saccoapi.flashapi.dto.response;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashLoanResponseDto {
    private Long id;
    private double principal;
    private LocalDateTime applicationDate;
    private LocalDateTime repayDate;
    private String loanStatus;
    private double amount;
    private double loanPenalty;
    private String memberId;
    private Boolean repaidInTime;
    private List<FlashRepayment> repayments;

}
