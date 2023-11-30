package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.Repayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDto {
    private Long id;
    private String loanType;
    private LocalDateTime applicationDate;
    private double principal;
    private int instalments;
    private double amount;
    private double interest;
    private String loanStatus;
    private List<Repayment> repayments;
    private String memberId;
    private String borrowerFname;
    private String borrowerMname;
    private String borrowerLname;
//    private List<Member> guarantors;

}
