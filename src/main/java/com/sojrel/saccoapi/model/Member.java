package com.sojrel.saccoapi.model;

//import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GenericGenerator(name = "id", strategy = "com.sojrel.saccoapi.model.MemberIdGenerator")
    @GeneratedValue(generator = "id")
    private String id;

    @Column(nullable = false)
    private String firstName;

//    @Column(updatable = false)
    private String midName;

    @Column(nullable = false)
    private String lastName;

    @Column(updatable = false)
    private LocalDateTime regDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private Long idNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(unique = true)
    private String phone;

    private String alternativePhone;

    @Column(unique = true)
    private String kraPin;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String residence;

    private String address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contribution> contributions;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Loan> loansTaken;

    @ManyToMany(mappedBy = "guarantors", fetch = FetchType.LAZY)
    private List<Loan> loansGuaranteed;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlashLoan> flashLoans;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserFiles> userFiles;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RegistrationFee> registrationFees;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private Boolean isActive;

    public enum Gender{
        MALE,FEMALE,NONE
    }
    public void addContribution(Contribution contribution){
        contributions.add(contribution);
    }

    public void removeContribution(Contribution contribution){
        contributions.remove(contribution);
    }

    public void addLoanTaken(Loan loan){
        loansTaken.add(loan);
    }

    public void removeLoanTaken(Loan loan){
        loansTaken.remove(loan);
    }

    public void addLoanGuaranteed(Loan loan){
        loansGuaranteed.add(loan);
    }

    public void removeLoanGuaranteed(Loan loan){
        loansGuaranteed.remove(loan);
    }
    public void addRegistrationFee(RegistrationFee registrationFee){
        registrationFees.add(registrationFee);
    }

    public void removeRegistrationFee(RegistrationFee registrationFee){
        registrationFees.remove(registrationFee);
    }


    @PrePersist
    public void prePersist(){
        if(regDate==null){
            regDate = LocalDateTime.now();
        }
    }
}
