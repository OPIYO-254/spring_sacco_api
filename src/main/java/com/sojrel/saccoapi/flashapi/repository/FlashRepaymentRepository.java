package com.sojrel.saccoapi.flashapi.repository;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.TotalDoubleItem;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanRepaidAmountDto;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashRepaymentRepository extends JpaRepository<FlashRepayment, Long> {
    List<FlashRepayment> findByLoan(FlashLoan loan);

    @Query(value = "SELECT new com.sojrel.saccoapi.dto.responses.TotalDoubleItem(SUM(fr.amount)) FROM FlashRepayment fr WHERE fr.loan.id = :loanId")
    TotalDoubleItem getLoansTotalRepaid(@Param("loanId") Long loanId);

    @Query(value = "select flash_loan_id, sum(amount) from flash_repayment group by flash_loan_id", nativeQuery = true)
    List<Object[]> getLoanAndRepaidAmount();
}
