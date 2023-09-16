package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT new com.sojrel.saccoapi.dto.responses.MemberLoansResponseDto(m.id, m.firstName, m.midName, l.id, l.loanType, l.applicationDate, l.principal, l.instalments, l.loanStatus)\n" +
            "from Loan l join Member m on l.borrower = m where l.loanStatus=:loanStatus AND l.repayments IS EMPTY")
    List<MemberLoansResponseDto> findAppliedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query(value = "SELECT new com.sojrel.saccoapi.dto.responses.MemberLoansResponseDto(m.id, m.firstName, m.midName, l.id, l.loanType, l.applicationDate, l.principal, l.instalments, l.loanStatus)\n" +
            "from Loan l join Member m on l.borrower = m where l.loanStatus=:loanStatus AND SIZE(l.repayments) > 0")
    List<MemberLoansResponseDto> findRepayingLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.MemberLoansResponseDto(m.id, m.firstName, m.midName, l.id, l.loanType, l.applicationDate, l.principal, l.instalments, l.loanStatus)\n" +
            "from Loan l join Member m on l.borrower = m where l.loanStatus=:loanStatus")
    List<MemberLoansResponseDto> findCompletedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query(value = "UPDATE LoanGuarantor g SET g.amount = :amount WHERE g.loan = (SELECT l from Loan l WHERE l.id=:loanId ) AND g.member= (SELECT m from Member m WHERE m.id = :memberId)", nativeQuery = true)
    int addGuaranteedAmount(@Param("memberId") String memberId, @Param("loanId") Long loanId, @Param("amount") Double amount);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Loan l WHERE l.loanStatus=:loanStatus AND l.repayments IS EMPTY")
    ItemCountDto countAppliedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Loan l WHERE l.loanStatus=:loanStatus AND SIZE(l.repayments) > 0")
    ItemCountDto countRepayingLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Loan l WHERE l.loanStatus='COMPLETED'")
    ItemCountDto countCompletedLoans(@Param("loanStatus") Loan.LoanStatus loanStatus);
    @Query("SELECT new com.sojrel.saccoapi.dto.responses.RepaymentResponseDto(r.id, r.repaymentDate, r.amount, r.loan.id) FROM Repayment r WHERE r.loan.id =:loanId")
    List<RepaymentResponseDto> getLoanRepayments(@Param("loanId") Long loanId);

    /*
    * The function gets the total amount repaid for a particular loan
    * */
    @Query("SELECT new com.sojrel.saccoapi.dto.responses.TotalDoubleItem(SUM(amount)) FROM Repayment r WHERE r.loan.id =:loanId")
    TotalDoubleItem getTotalRepaid(@Param("loanId") Long loanId);

    /**
     * get total guaranteed by each member
     *
     */
//    @Query("SELECT new com.sojrel.saccoapi.dto.responses.TotalDoubleItem(SUM(l.amount)) FROM Loan l WHERE l.loanStatus = 'COMPLETE' OR l.loanStatus = 'APPROVED'")
//    TotalDoubleItem getTotalDisbursed();
}


