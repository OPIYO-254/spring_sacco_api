package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.LoanGuarantorId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface LoanService {
    public LoanResponseDto addLoan(LoanRequestDto loanRequestDto);
    public Loan getLoanById(Long loanId);
    public LoanResponseDto getLoan(Long loanId);
    public List<LoanResponseDto> getLoans();
    public LoanResponseDto editLoan(Long loanId, LoanRequestDto loanRequestDto);
    public LoanResponseDto updateLoan(Long loanId, Map<String, String> fields);
    public LoanResponseDto deleteLoan(Long loanId);
    public LoanResponseDto addBorrowerToLoan(Long loanId, String borrowerId);
    public LoanResponseDto removeBorrowerFromLoan(Long loanId, String borrowerId);
    public LoanResponseDto addRepaymentToLoan(Long loanId, Long repaymentId);
    public LoanResponseDto removeRepaymentFromLoan(Long loanId, Long repaymentId);
    public LoanResponseDto addGuarantorToLoan(String memberId, Long loanId);
    public LoanResponseDto removeGuarantorFromLoan(Long loanId, String guarantorId);
    public List<MemberLoansResponseDto> getAppliedLoans();
    public List<MemberLoansResponseDto> getRejectedLoans();
    public List<MemberLoansResponseDto> getApprovedLoans();
    public ItemCountDto countAppliedLoans();
    public ItemCountDto countRejectedLoans();
    public ItemCountDto countApprovedLoans();
    public ItemCountDto countCompletedLoans();
    public void updateGuaranteedAmount(String memberId, Long loanId, Double amount);
    public List<LoanGuarantorResponseDto> getLoanGuarantors(Long loanId);
    public ItemTotalDto getTotalGuaranteed(Long loanId);
    public void approveLoan(Long loanId);
    public void rejectLoan(Long loanId);

}
