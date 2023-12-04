package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String id;
    private String firstName;
    private String midName;
    private String lastName;
    private LocalDateTime regDate;
    private Long idNo;
    private Date dob;
    private String email;
    private String phone;
    private String alternativePhone;
    private String kraPin;
    private String gender;
    private String residence;
    private String address;
    private List<Contribution> contributions;
    private List<Loan> loansTaken;
    private List<Loan> loansGuaranteed;
    private List<FlashLoan> flashLoans;
}
