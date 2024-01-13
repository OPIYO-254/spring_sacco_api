package com.sojrel.saccoapi.flashapi.service;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.flashapi.dto.request.FlashLoanRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanResponseDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashRepaymentAndTotalRepaid;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface FlashLoanService {
    FlashLoanResponseDto addFlashLoan(FlashLoanRequestDto dto);
    FlashLoan getFlashLoanById(Long id);
    FlashLoanResponseDto getFlashLoan(Long id);
    public FlashLoanResponseDto approveFlashLoan(Long id);
    public FlashLoanResponseDto rejectFlashLoan(Long id);
    public FlashLoanResponseDto completeFlashLoan(Long id);
    public FlashLoanResponseDto writeOffFlashLoan(Long id);
    List<FlashLoanResponseDto> getAllFlashLoans();
    List<FlashLoanResponseDto> getNewFlashLoans();
    List<FlashLoanResponseDto> getFlashLoanByMemberId(String memberId);
    List<FlashLoanResponseDto> getFlashLoanByStatus(String loanStatus);

    FlashRepaymentAndTotalRepaid getLoanAndRepayment(Long id);
    List<FlashLoanResponseDto> getFlashLoanByApplicationDate(LocalDateTime date);

    List<FlashLoanResponseDto> getMemberLoansByLoanStatus(String memberId, FlashLoan.Status loanStatus);

    Double determineLoanLimit(String memberId);

}
