package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Table(name="loan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;

    @Column(updatable = false)
    private LocalDateTime applicationDate;

    @Column(nullable = false)
    private double principal;

    @Column(nullable = false)
    private int instalments;

    @Transient
    private double amount;

    @Column(nullable = false)
    private double interest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus;

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Repayment> repayments;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member borrower;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "loan_guarantors",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> guarantors;

    public double calculatedAmount(){
//        amount = instalments * (principal * (interest / 100) * Math.pow((1 + (interest / 100)), instalments)) /
//                (Math.pow((1 + (interest / 100)), instalments) - 1);
//        DecimalFormat decimalFormat = new DecimalFormat("#");
//        String formattedNumber = decimalFormat.format(instalmentAmount);
//        double formattedAmount = Double.parseDouble(formattedNumber);
//        if(loanType.equals(LoanType.EMERGENCY)){
//            return Math.floor(formattedAmount/10f)*10;
//        }
//        return Math.round(formattedAmount/100f)*100;
        double instalmentAmount = ((principal*interest/100)+(principal/instalments * interest/100))/2*instalments;//calculates total interest for the loan
        double amount = principal + instalmentAmount; //amount of the loan
        return  amount;

    }

    public enum LoanType {
        EMERGENCY,NORMAL,BUSINESS,DEVELOPMENT,JIJENGE,ASSET, SCHOOL_FEES,CORPORATE
    }
    public enum LoanStatus {
        REVIEW,APPROVED,REJECTED,COMPLETED
    }
    @PrePersist
    public void prePersist(){
        if(applicationDate == null){
            applicationDate = LocalDateTime.now(ZoneId.of("Africa/Nairobi"));
        }
    }



    public void addRepayment(Repayment repayment){
        repayments.add(repayment);
    }

    public void removeRepayment(Repayment repayment){
        repayments.remove(repayment);
    }
    public void addGuarantor(Member member){
        guarantors.add(member);
    }
    public void removeGuarantor(Member member){
        guarantors.remove(member);
    }

}
