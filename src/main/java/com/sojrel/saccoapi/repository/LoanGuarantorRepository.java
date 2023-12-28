package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.GuarantorsDto;
import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto;
import com.sojrel.saccoapi.dto.responses.TotalDoubleItem;
import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.LoanGuarantorId;
import jakarta.persistence.NamedAttributeNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanGuarantorRepository extends JpaRepository<LoanGuarantor, LoanGuarantorId> {
    Optional<LoanGuarantor> findByMemberIdAndLoanId(String memberId, Long loanId);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto(m.id, l.id, m.firstName, m.midName, g.amount, m.phone, m.email) FROM LoanGuarantor g " +
            "join Member m on g.member = m join Loan l on g.loan=l WHERE l.id =:loanId")
    List<LoanGuarantorResponseDto> findLoanGuarantorById(@Param("loanId") Long loanId);

    @Query(value="SELECT SUM(amount) FROM loan_guarantors WHERE loan_id =:loanId", nativeQuery = true)
    Long getTotalGuaranteed(@Param("loanId") Long loanId);

//    @Query(value = "SELECT loan_id, member_id, amount FROM loan_guarantors WHERE member_id =:memberId")
//    TotalDoubleItem getOustandingGuaranteeAmount(String memberId);

    @Query(value = "SELECT new com.sojrel.saccoapi.dto.responses.GuarantorsDto(lg.member.id, lg.loan.id, lg.amount) FROM LoanGuarantor lg WHERE lg.member.id=:memberId AND lg.loan.loanStatus='APPROVED'")
    List<GuarantorsDto> getMemberApprovedLoansGuaranteed(@Param("memberId") String memberId);

}

/*
private String memberId;
    private Long loanId;
    private String firstName;
    private String midName;
    private Double amount;
 */