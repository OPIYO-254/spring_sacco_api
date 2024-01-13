package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sojrel.saccoapi.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
//@Data
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
    private double loanPenalty;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(mappedBy = "loan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FlashDisbursement flashDisbursement;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlashRepayment> repayments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(LocalDateTime repayDate) {
        this.repayDate = repayDate.atZone(ZoneId.of("Africa/Nairobi")).toLocalDateTime();;
    }

    public Status getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(Status loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Boolean getRepaidInTime() {
        return repaidInTime;
    }

    public void setRepaidInTime(Boolean repaidInTime) {
        this.repaidInTime = repaidInTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLoanPenalty() {
        return loanPenalty;
    }

    public void setLoanPenalty(double loanPenalty) {
        this.loanPenalty = loanPenalty;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public FlashDisbursement getFlashDisbursement() {
        return flashDisbursement;
    }

    public void setFlashDisbursement(FlashDisbursement flashDisbursement) {
        this.flashDisbursement = flashDisbursement;
    }

    public List<FlashRepayment> getRepayments() {
        return repayments;
    }

    public void setRepayments(List<FlashRepayment> repayments) {
        this.repayments = repayments;
    }

    public enum Status{REVIEWING,APPROVED,REJECTED,PAID, WRITEOFF}

    public double calculatePenalty(){
        if(LocalDateTime.now().isAfter(repayDate)){
            Duration duration = Duration.between(repayDate, LocalDateTime.now());
            Long days = duration.toDays();
            loanPenalty = amount*0.01*days;
        }
        else {
            loanPenalty = 0;
        }
        return  loanPenalty;
    }

    @PrePersist
    public void prePersist(){
        if(applicationDate == null){
            applicationDate = LocalDateTime.now(ZoneId.of("Africa/Nairobi"));
        }
    }


}
