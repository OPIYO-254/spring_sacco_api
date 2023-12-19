package com.sojrel.saccoapi.flashapi.service;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.TotalDoubleItem;
import com.sojrel.saccoapi.flashapi.dto.request.FlashRepaymentRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanRepaidAmountDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashRepaymentResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlashRepaymentService {
    public FlashRepaymentResponseDto makeRepayment(FlashRepaymentRequestDto dto);

    public List<FlashRepaymentResponseDto> getLoanRepayments(Long loanId);

    public List<FlashRepaymentResponseDto> getAllRepayments();

    TotalDoubleItem getTotalLoanRepaid(Long loanId);
    public List<FlashLoanRepaidAmountDto> getLoansAndRepaidAmount();

}
