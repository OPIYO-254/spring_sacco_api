package com.sojrel.saccoapi.flashapi.service;

import com.sojrel.saccoapi.flashapi.dto.request.FlashLoanRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanMapper;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanResponseDto;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.repository.FlashLoanRepository;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FlashServiceImpl implements FlashLoanService{
    @Autowired
    private FlashLoanRepository flashLoanRepository;
    @Autowired
    private MemberService memberService;

    @Override
    public FlashLoanResponseDto addFlashLoan(FlashLoanRequestDto dto) {
        FlashLoan loan = new FlashLoan();
        loan.setPrincipal(dto.getPrincipal());
        Member member = memberService.getMemberById(dto.getMemberId());
        if(Objects.nonNull(member)){
            loan.setMember(member);
        }
        loan.setAmount(dto.getAmount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getRepayDate(), formatter);
        loan.setRepayDate(dateTime);
        loan.setLoanStatus(FlashLoan.Status.valueOf(dto.getLoanStatus()));
        flashLoanRepository.save(loan);
        return FlashLoanMapper.flashLoanToDto(loan);
    }

    @Override
    public FlashLoan getFlashLoanById(Long id) {
        return flashLoanRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("loan with id "+id+" does not exist"));
    }

    @Override
    public FlashLoanResponseDto getFlashLoan(Long id) {
        FlashLoan loan = getFlashLoanById(id);
        return FlashLoanMapper.flashLoanToDto(loan);
    }

    @Override
    public List<FlashLoanResponseDto> getAllFlashLoans() {
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }

    @Override
    public List<FlashLoanResponseDto> getFlashLoanByMemberId(String memberId) {
        Member member = memberService.getMemberById(memberId);
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findByMember(member).spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }

    @Override
    public List<FlashLoanResponseDto> getFlashLoanByStatus(String loanStatus) {
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findByLoanStatus(FlashLoan.Status.valueOf(loanStatus)).spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }

    @Override
    public List<FlashLoanResponseDto> getFlashLoanByApplicationDate(LocalDateTime date) {
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findByApplicationDate(date).spliterator(),false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }

    @Override
    public List<FlashLoanResponseDto> getMemberLoansByLoanStatus(String memberId, FlashLoan.Status loanStatus) {
        Member member = memberService.getMemberById(memberId);
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findByMemberAndLoanStatus(member, loanStatus).spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }
}
