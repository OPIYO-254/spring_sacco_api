package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
