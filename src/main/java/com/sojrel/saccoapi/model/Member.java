package com.sojrel.saccoapi.model;

//import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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

    @Column(nullable = false, updatable = false)
    private String firstName;

    @Column(updatable = false)
    private String midName;

    @Column(nullable = false, updatable = false)
    private String lastName;

    @Column(updatable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private Long idNo;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(nullable = false, unique = true)
    private String phone;

    private String alternativePhone;

    @Column(nullable = false,unique = true, updatable = false)
    private String kraPin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String residence;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contribution> contributions;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Loan> loansTaken;

    @ManyToMany(mappedBy = "guarantors", fetch = FetchType.LAZY)
    private List<Loan> loansGuaranteed;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlashLoan> flashLoans;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id")
    private Credentials credentials;

    public enum Gender{
        MALE,FEMALE
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

    public void addCredentials(Credentials credentials){this.addCredentials(credentials);}


    @PrePersist
    public void prePersist(){
        if(regDate==null){
            regDate = LocalDateTime.now();
        }
    }
}
