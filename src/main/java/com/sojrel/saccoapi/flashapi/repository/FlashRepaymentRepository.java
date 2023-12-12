package com.sojrel.saccoapi.flashapi.repository;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashRepaymentRepository extends JpaRepository<FlashRepayment, Long> {
    List<FlashRepayment> findByLoan(FlashLoan loan);

}
