package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.flashapi.model.FlashDisbursement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
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
    private LocalDateTime applicationDate;
    @UpdateTimestamp
    private LocalDateTime repayDate;
    @Column(nullable = false)
    private Status loanStatus;
    @Column(nullable = false)
    private double amount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(mappedBy = "loan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FlashDisbursement flashDisbursement;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlashRepayment> repayments;
    public enum Status{REVIEWING,APPROVED,REJECTED,PAID, WRITTEN_OFF}




}
