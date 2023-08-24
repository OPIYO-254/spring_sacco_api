package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.LoanGuarantorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanGuarantorRepository extends JpaRepository<LoanGuarantor, LoanGuarantorId> {
    Optional<LoanGuarantor> findByMemberIdAndLoanId(String memberId, Long loanId);
}
