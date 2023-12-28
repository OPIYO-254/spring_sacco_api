package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.Repayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberLoansResponseDto {
    private String memberId;
    private String firstName;
    private String midName;
    private Long loanId;
    private Loan.LoanType loanType;
    private LocalDateTime applicationDate;
    private double principal;
    private int instalments;
    private Loan.LoanStatus loanStatus;

}
