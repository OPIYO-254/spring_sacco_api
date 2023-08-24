package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto;
import com.sojrel.saccoapi.dto.responses.LoanResponseDto;
import com.sojrel.saccoapi.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT member.id, member.first_name, member.mid_name, loan.id, loan.loan_type, loan.application_date, loan.principal, loan.instalments, loan.loan_status\n" +
            "from loan inner join member on loan.member_id = member.id where loan.loan_status = 'REVIEW';", nativeQuery = true)
    List<Object[]> findAppliedLoans();
    @Query("SELECT COUNT(*) FROM Loan l WHERE l.loanStatus='REVIEW'")
    int countAppliedLoans();
    @Query(value = "UPDATE LoanGuarantor g SET g.amount = :amount WHERE g.loan = (SELECT l from Loan l WHERE l.id=:loanId ) AND g.member= (SELECT m from Member m WHERE m.id = :memberId)", nativeQuery = true)
    int addGuaranteedAmount(@Param("memberId") String memberId, @Param("loanId") Long loanId, @Param("amount") Double amount);

}
