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

}
