package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto;
import com.sojrel.saccoapi.dto.responses.ItemCountDto;
import com.sojrel.saccoapi.dto.responses.MemberLoansResponseDto;
import com.sojrel.saccoapi.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT new com.sojrel.saccoapi.dto.responses.MemberLoansResponseDto(m.id, m.firstName, m.midName, l.id, l.loanType, l.applicationDate, l.principal, l.instalments, l.loanStatus)\n" +
            "from Loan l join Member m on l.borrower = m where l.loanStatus=:loanStatus")
    List<MemberLoansResponseDto> findAppliedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query(value = "UPDATE LoanGuarantor g SET g.amount = :amount WHERE g.loan = (SELECT l from Loan l WHERE l.id=:loanId ) AND g.member= (SELECT m from Member m WHERE m.id = :memberId)", nativeQuery = true)
    int addGuaranteedAmount(@Param("memberId") String memberId, @Param("loanId") Long loanId, @Param("amount") Double amount);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Loan l WHERE l.loanStatus=:loanStatus")
    ItemCountDto countAppliedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);
}
/*

private String memberId;
    private String firstName;
    private String midName;
    private Long id;
    private String loanType;
    private LocalDateTime applicationDate;
    private double principal;
    private int instalments;
    private String loanStatus;
 */