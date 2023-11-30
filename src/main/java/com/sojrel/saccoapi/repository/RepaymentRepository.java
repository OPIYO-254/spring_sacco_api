package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

    List<Repayment> findByLoan(Loan loan);

}
