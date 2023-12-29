package com.sojrel.saccoapi.flashapi.repository;

import com.sojrel.saccoapi.flashapi.dto.response.LoanHistoryDto;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlashLoanRepository extends JpaRepository<FlashLoan, Long> {
    List<FlashLoan> findByMember(Member member);

    List<FlashLoan> findByLoanStatus(FlashLoan.Status loanStatus);

    List<FlashLoan> findByApplicationDate(LocalDateTime date);

    List<FlashLoan> findByMemberAndLoanStatus(Member member, FlashLoan.Status loanStatus);

//    @Query("SELECT new com.sojrel.saccoapi.flashapi.dto.response.LoanHistoryDto(l.id, l.principal,l.repayDate, r.amount, " +
//            "r.transactionDate)")
//    List<LoanHistoryDto> findMemberHistory(@Param("memberId") String memberId);
}

/*
private Long loanId;
    private double borrowedAmount;
    private LocalDateTime setRepayDate;
    private double repaidAmount;
    private LocalDateTime actualRepayment;
 */
