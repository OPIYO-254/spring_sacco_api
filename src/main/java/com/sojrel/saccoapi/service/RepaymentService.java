package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.RepaymentRequestDto;
import com.sojrel.saccoapi.dto.responses.RepaymentResponseDto;
import com.sojrel.saccoapi.model.Repayment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RepaymentService {
    public RepaymentResponseDto addRepayment(RepaymentRequestDto repaymentRequestDto);
    public Repayment getRepaymentById(Long repaymentId);
    public RepaymentResponseDto getRepayment(Long repaymentId);
    public List<RepaymentResponseDto> getRepayments();
    public RepaymentResponseDto editRepayment(Long repaymentId, RepaymentRequestDto repaymentRequestDto);
    public RepaymentResponseDto deleteRepayment(Long repaymentId);

    RepaymentResponseDto addLoanToRepayment(Long repaymentId, Long loanId);

    RepaymentResponseDto removeLoanFromRepayment(Long repaymentId, Long loanId);


}
