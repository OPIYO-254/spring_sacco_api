package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto;
import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.LoanGuarantorId;
import jakarta.persistence.NamedAttributeNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanGuarantorRepository extends JpaRepository<LoanGuarantor, LoanGuarantorId> {
    Optional<LoanGuarantor> findByMemberIdAndLoanId(String memberId, Long loanId);
    //SELECT loan.id, member.id, member.first_name, member.mid_name, loan_guarantors.amount from
    //loan_guarantors inner join loan on loan_guarantors.loan_id = loan.id
    //inner join member on loan_guarantors.member_id = member.id where loan.id=1;
    @Query("SELECT new com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto(m.id, l.id, m.firstName, m.midName, g.amount, m.phone, m.email) FROM LoanGuarantor g " +
            "join Member m on g.member = m join Loan l on g.loan=l WHERE l.id =:loanId")
    List<LoanGuarantorResponseDto> findLoanGuarantorById(@Param("loanId") Long loanId);

    @Query(value="SELECT SUM(amount) FROM loan_guarantors WHERE loan_id =:loanId", nativeQuery = true)
    Long getTotalGuaranteed(@Param("loanId") Long loanId);

}

/*
private String memberId;
    private Long loanId;
    private String firstName;
    private String midName;
    private Double amount;
 */