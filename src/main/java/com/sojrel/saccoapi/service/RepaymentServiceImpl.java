package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.RepaymentRequestDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.RepaymentResponseDto;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Repayment;
import com.sojrel.saccoapi.repository.RepaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RepaymentServiceImpl implements RepaymentService{
    @Autowired
    RepaymentRepository repaymentRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    LoanService loanService;
    @Override
    public RepaymentResponseDto addRepayment(RepaymentRequestDto repaymentRequestDto) {
        Repayment repayment = new Repayment();
        repayment.setLoan(loanService.getLoanById(repaymentRequestDto.getLoanId()));
        repayment.setAmount(repaymentRequestDto.getAmount());
        repaymentRepository.save(repayment);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }

    @Override
    public Repayment getRepaymentById(Long repaymentId) {

        return repaymentRepository.findById(repaymentId).orElseThrow(()->
                new IllegalArgumentException("Repayment with id "+repaymentId+" not found"));
    }

    @Override
    public RepaymentResponseDto getRepayment(Long repaymentId) {
        Repayment repayment = getRepaymentById(repaymentId);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }

    @Override
    public List<RepaymentResponseDto> getRepayments() {
        List<Repayment> repayments = StreamSupport.stream(repaymentRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return Mapper.repaymentToRepaymentResponseDtos(repayments);
    }
    @Transactional
    @Override
    public RepaymentResponseDto editRepayment(Long repaymentId, RepaymentRequestDto repaymentRequestDto) {
        Repayment repayment = getRepaymentById(repaymentId);
        if(repaymentRequestDto.getLoanId()!=null) {
            repayment.setLoan(loanService.getLoanById(repaymentRequestDto.getLoanId()));
        }
        repayment.setAmount(repaymentRequestDto.getAmount());
        repaymentRepository.save(repayment);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }

    @Override
    public RepaymentResponseDto deleteRepayment(Long repaymentId) {
        Repayment repayment = getRepaymentById(repaymentId);
        repaymentRepository.delete(repayment);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }

    @Override
    public RepaymentResponseDto addLoanToRepayment(Long repaymentId, Long loanId) {
        Repayment repayment = getRepaymentById(repaymentId);
        Loan loan = loanService.getLoanById(loanId);
        if(Objects.nonNull(repayment.getLoan())){
            throw new IllegalArgumentException("The repayment has a loan");
        }
        repayment.setLoan(loan);
        loan.addRepayment(repayment);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }

    @Override
    public RepaymentResponseDto removeLoanFromRepayment(Long repaymentId, Long loanId) {
        Repayment repayment = getRepaymentById(repaymentId);
        Loan loan = loanService.getLoanById(loanId);
        if(!Objects.nonNull(repayment.getLoan())){
            throw new IllegalArgumentException("The repayment has no loan");
        }
        repayment.setLoan(null);
        loan.addRepayment(repayment);
        return Mapper.repaymentToRepaymentResponseDto(repayment);
    }
}
