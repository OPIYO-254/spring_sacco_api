package com.sojrel.saccoapi.dto.requests;


import com.sojrel.saccoapi.model.Loan;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDto {
    private String loanType;
    private double principal;
    private int instalments;
    private double interest;
    private String loanStatus;
    private String memberId;
//    private List<String> guarantorIds;


}
