package com.sojrel.saccoapi.dto.responses;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanGuarantorResponseDto {
    private String memberId;
    private Long loanId;
    private String firstName;
    private String midName;
    private Double amount;
    private String phone;
    private String email;
}
