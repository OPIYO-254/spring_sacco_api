package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sojrel.saccoapi.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="flash_loan")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private double principal;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime applicationDate;

    @Column(nullable = false)
    private LocalDateTime repayDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status loanStatus;
    @Column(nullable = false)
    private Boolean repaidInTime = false;
    @Column(nullable = false)
    private double amount;
    @Transient
    private double processingFee;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(mappedBy = "loan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FlashDisbursement flashDisbursement;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlashRepayment> repayments;
    public enum Status{REVIEWING,APPROVED,REJECTED,PAID, WRITEOFF}

    public double calculateFee(){
        if(principal > 1000.0 && principal <=1500.0){
            processingFee = 28.0;
        }
        else if(principal > 1500.0 && principal <=2500.0){
            processingFee = 33.0;
        }
        else if(principal > 2500.0 && principal <=5000.0){
            processingFee = 57.0;
        }
        else if(principal > 5000.0 && principal <=7500.0){
            processingFee = 78.0;
        }
        else if(principal > 7500.0 && principal <=1000.0){
            processingFee = 90.0;
        }
        else{
            processingFee = 23.0;
        }
        return processingFee;
    }

    @PrePersist
    public void prePersist(){
        if(applicationDate == null){
            applicationDate = LocalDateTime.now();
        }
    }


}
