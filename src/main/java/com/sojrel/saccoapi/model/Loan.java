package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
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

    @ManyToMany
    @JoinTable(
            name = "loan_guarantors",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> guarantors;
//    @PrePersist
//    private void getActualInterest() {
//        if(interest==null) {
//            if (loanType.equals(LoanType.NORMAL)) {
//                interest = 2.0;
//            } else if (loanType.equals(LoanType.EMERGENCY)) {
//                interest = 2.5;
//            } else if (loanType.equals(LoanType.BUSINESS)) {
//                interest = 2.0;
//            } else if (loanType.equals(LoanType.ASSET)) {
//                interest = 2.5;
//            } else if (loanType.equals(LoanType.DEVELOPMENT)) {
//                interest = 2.0;
//            } else if (loanType.equals(LoanType.JIJENGE)) {
//                interest = 2.0;
//            }
//        }
////        return interest;
//    }


    public double calculatedAmount(){
//        double numerator = principal * (interest/100) * Math.pow((1+(interest/100)),instalments);
//        double denominator = Math.pow((1+(interest/100)), instalments)-1;
        amount = instalments * (principal * (interest / 100) * Math.pow((1 + (interest / 100)), instalments)) / (Math.pow((1 + (interest / 100)), instalments) - 1);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String formattedNumber = decimalFormat.format(amount);

        double formatteAmount = Double.parseDouble(formattedNumber);
        return formatteAmount;
    }


    public enum LoanType {
        EMERGENCY,NORMAL,BUSINESS,DEVELOPMENT,JIJENGE,ASSET
    }
    public enum LoanStatus {
        REVIEW,APPROVED,REJECTED,COMPLETED
    }
    @PrePersist
    public void prePersist(){
        if(applicationDate == null){
            applicationDate = LocalDateTime.now();
        }
    }

//    public double getInterestValue(){
//        if(interest==null){
//            if(LoanType.valueOf(getLoanType().name()).equals(LoanType.NORMAL.name())){
//                interest=2.0;
//            }
//            else if(LoanType.valueOf(getLoanType().name()).equals(LoanType.EMERGENCY.name())){
//                interest = 2.5;
//            }
//            else if(LoanType.valueOf(getLoanType().name()).equals(LoanType.BUSINESS.name())){
//                interest = 3.0;
//            }
//            else if(LoanType.valueOf(getLoanType().name()).equals(LoanType.JIJENGE.name())){
//                interest = 2.0;
//            }
//            else if(LoanType.valueOf(getLoanType().name()).equals(LoanType.ASSET.name())){
//                interest = 2.0;
//            }
//            else if(LoanType.valueOf(getLoanType().name()).equals(LoanType.DEVELOPMENT.name())){
//                interest = 2.0;
//            }
//        }


    /**
     * calculating the amount based on the principal, interest and instalments
     */






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
