package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.Loan;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentResponseDto {
    private Long id;
    private LocalDateTime repaymentDate;
    private double amount;
    private Long loanId;
}
