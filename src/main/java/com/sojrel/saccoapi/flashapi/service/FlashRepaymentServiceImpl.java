package com.sojrel.saccoapi.flashapi.service;

import com.sojrel.saccoapi.flashapi.dto.request.FlashRepaymentRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanMapper;
import com.sojrel.saccoapi.flashapi.dto.response.FlashRepaymentResponseDto;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import com.sojrel.saccoapi.flashapi.repository.FlashRepaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FlashRepaymentServiceImpl implements FlashRepaymentService{

    @Autowired
    private FlashLoanService flashLoanService;
    @Autowired
    private FlashRepaymentRepository flashRepaymentRepository;
    @Override
    public FlashRepaymentResponseDto addRepayment(FlashRepaymentRequestDto dto) {
        FlashRepayment repayment = new FlashRepayment();
        repayment.setLoan(flashLoanService.getFlashLoanById(dto.getLoanId()));
        repayment.setAmount(dto.getAmount());
        flashRepaymentRepository.save(repayment);
        return FlashLoanMapper.flashRepaymentToFlashRepaymentResponseDto(repayment);
    }

    @Override
    public List<FlashRepaymentResponseDto> getLoanRepayments(Long loanId) {
        FlashLoan loan = flashLoanService.getFlashLoanById(loanId);
        List<FlashRepayment> list=flashRepaymentRepository.findByLoan(loan);
        return FlashLoanMapper.flashRepaymentsToFlashRepaymentDtos(list);
    }

    @Override
    public List<FlashRepaymentResponseDto> getAllRepayments() {
        List<FlashRepayment> repayments = StreamSupport.stream(flashRepaymentRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashRepaymentsToFlashRepaymentDtos(repayments);
    }


}
