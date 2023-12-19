package com.sojrel.saccoapi.flashapi.service;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.TotalDoubleItem;
import com.sojrel.saccoapi.flashapi.dto.request.FlashLoanRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.*;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.repository.FlashLoanRepository;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class FlashLoanServiceImpl implements FlashLoanService{
    @Autowired
    private FlashLoanRepository flashLoanRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FlashRepaymentService flashRepaymentService;

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
    public FlashLoanResponseDto approveFlashLoan(Long id) {
        FlashLoan loan = getFlashLoanById(id);
        if(Objects.nonNull(loan)){
            loan.setLoanStatus(FlashLoan.Status.APPROVED);
            flashLoanRepository.save(loan);
        }
        return FlashLoanMapper.flashLoanToDto(loan);
    }

    @Override
    public FlashLoanResponseDto rejectFlashLoan(Long id) {
        FlashLoan loan = getFlashLoanById(id);
        if(Objects.nonNull(loan)){
            loan.setLoanStatus(FlashLoan.Status.REJECTED);
            flashLoanRepository.save(loan);
        }
        return FlashLoanMapper.flashLoanToDto(loan);
    }

    @Override
    public FlashLoanResponseDto completeFlashLoan(Long id) {
        FlashLoan loan = getFlashLoanById(id);
        if(Objects.nonNull(loan)){
            loan.setLoanStatus(FlashLoan.Status.PAID);
            flashLoanRepository.save(loan);
        }
        return FlashLoanMapper.flashLoanToDto(loan);
    }
    @Override
    public FlashLoanResponseDto writeOffFlashLoan(Long id) {
        FlashLoan loan = getFlashLoanById(id);
        if(Objects.nonNull(loan)){
            loan.setLoanStatus(FlashLoan.Status.WRITTEN_OFF);
            flashLoanRepository.save(loan);
        }
        return FlashLoanMapper.flashLoanToDto(loan);
    }

    @Override
    public List<FlashLoanResponseDto> getAllFlashLoans() {
        List<FlashLoan> loans = StreamSupport.stream(flashLoanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return FlashLoanMapper.flashLoansToFlashLoanDtos(loans);
    }

    @Override
    public List<FlashLoanResponseDto> getNewFlashLoans() {
        List<FlashLoanResponseDto> loans = getAllFlashLoans();
        List<FlashLoanResponseDto> newLoans = new ArrayList<>();
        for(FlashLoanResponseDto dto:loans){
            if(dto.getLoanStatus() == "REVIEWING"){
                newLoans.add(dto);
            }
        }
        return newLoans;
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
    public FlashRepaymentAndTotalRepaid getLoanAndRepayment(Long id) {
        FlashRepaymentAndTotalRepaid flashRepaymentAndTotalRepaid = new FlashRepaymentAndTotalRepaid();
        try {
            FlashLoan loan = getFlashLoanById(id);
            TotalDoubleItem item = flashRepaymentService.getTotalLoanRepaid(id);

            flashRepaymentAndTotalRepaid.setLoan(loan);
            flashRepaymentAndTotalRepaid.setTotalRepaid(item.getTotal());
        }
        catch (Exception e){
            log.error("error "+ e.getLocalizedMessage());
        }
        return flashRepaymentAndTotalRepaid;

    }

    public List<FlashRepaymentAndTotalRepaid> getLoansAndRepayments(){
        List<FlashRepaymentAndTotalRepaid> list = new ArrayList<>();
        List<FlashLoanResponseDto> loans = getAllFlashLoans();
        List<FlashLoanRepaidAmountDto> repaymentResponseDtos = flashRepaymentService.getLoansAndRepaidAmount();
        for(FlashLoanResponseDto dto:loans){
            for(FlashLoanRepaidAmountDto flashLoanRepaidAmountDto:repaymentResponseDtos){
                if(dto.getId()==flashLoanRepaidAmountDto.getLoanId()){
                    FlashRepaymentAndTotalRepaid repaid = new FlashRepaymentAndTotalRepaid(getFlashLoanById(flashLoanRepaidAmountDto.getLoanId()), flashLoanRepaidAmountDto.getAmount());
                    list.add(repaid);
                }
            }
        }
        System.out.println(list);
        return list;
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
