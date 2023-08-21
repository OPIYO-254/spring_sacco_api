package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.responses.LoanResponseDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.Repayment;
import com.sojrel.saccoapi.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class LoanServiceImpl implements LoanService{
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private MemberService memberService;

    private RepaymentService repaymentService;
    @Transactional
    @Override
    public LoanResponseDto addLoan(LoanRequestDto loanRequestDto) {
        Loan loan = new Loan();
        System.out.println(loanRequestDto.getLoanType());
        loan.setLoanType(Loan.LoanType.valueOf(loanRequestDto.getLoanType()));
        Member member = memberService.getMemberById(loanRequestDto.getMemberId());
        if(Objects.nonNull(member)){
            loan.setBorrower(member);
        }
        loan.setPrincipal(loanRequestDto.getPrincipal());
        loan.setInstalments(loanRequestDto.getInstalments());
        loan.setInterest(loanRequestDto.getInterest());
        loan.setLoanStatus(Loan.LoanStatus.valueOf(loanRequestDto.getLoanStatus()));
        loanRepository.save(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }

    @Override
    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId).orElseThrow(()->
                new IllegalArgumentException("Loan with id" + loanId+ " not found"));
    }

    @Override
    public LoanResponseDto getLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        return Mapper.loanToLoanResponseDto(loan);
    }

    @Override
    public List<LoanResponseDto> getLoans() {
        List<Loan> loans= StreamSupport.stream(loanRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return Mapper.loanToLoanResponseDtos(loans);
    }
    @Transactional
    @Override
    public LoanResponseDto editLoan(Long loanId, LoanRequestDto loanRequestDto) {
        Loan loan = getLoanById(loanId);
        loan.setLoanType(Loan.LoanType.valueOf(loanRequestDto.getLoanType()));
        loan.setLoanStatus(Loan.LoanStatus.valueOf(loanRequestDto.getLoanStatus()));
        loan.setPrincipal(loanRequestDto.getPrincipal());
        loan.setInstalments(loanRequestDto.getInstalments());
        if(loanRequestDto.getMemberId()!=null){
            loan.setBorrower(memberService.getMemberById(loanRequestDto.getMemberId()));
        }
        loanRepository.save(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }

    @Override
    public LoanResponseDto updateLoan(Long id, Map<String, String> fields) {
        Loan loan = getLoanById(id);
        if(Objects.nonNull(loan)) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Loan.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, loan, Loan.LoanStatus.valueOf(value));
            });
            loanRepository.save(loan);
            return Mapper.loanToLoanResponseDto(loan);
        }
        return null;
    }

    @Override
    public LoanResponseDto deleteLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        loanRepository.delete(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }
    @Transactional
    @Override
    public LoanResponseDto addBorrowerToLoan(Long loanId, String borrowerId) {
        Loan loan = getLoanById(loanId);
        if (Objects.nonNull(loan.getBorrower())) {
            throw new IllegalArgumentException("The loan already has a borrower");
        }
        Member borrower = memberService.getMemberById(borrowerId);
        loan.setBorrower(borrower);
        borrower.addLoanTaken(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }
    @Transactional
    @Override
    public LoanResponseDto removeBorrowerFromLoan(Long loanId, String borrowerId){
        Loan loan = getLoanById(loanId);
        if (!Objects.nonNull(loan.getBorrower())) {
            throw new IllegalArgumentException("The loan already has no borrower");
        }
        Member borrower = memberService.getMemberById(borrowerId);
        loan.setBorrower(null);
        borrower.addLoanTaken(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }


    @Transactional
    @Override
    public LoanResponseDto addRepaymentToLoan(Long loanId, Long repaymentId) {
        Loan loan = getLoanById(loanId);
        Repayment repayment = repaymentService.getRepaymentById(repaymentId);
        if(loan.getRepayments().contains(repayment)){
            throw new IllegalArgumentException("Loan alreay has this repayment");
        }
        loan.addRepayment(repayment);
        repayment.setLoan(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }

    @Transactional
    @Override
    public LoanResponseDto removeRepaymentFromLoan(Long loanId, Long repaymentId) {
        Loan loan = getLoanById(loanId);
        Repayment repayment = repaymentService.getRepaymentById(repaymentId);
        if(!loan.getRepayments().contains(repayment)){
            throw new IllegalArgumentException("The loan does not have this repayment");
        }
        loan.removeRepayment(repayment);
        repayment.setLoan(null);
        return Mapper.loanToLoanResponseDto(loan);
    }
    @Transactional
    @Override
    public LoanResponseDto addGuarantorToLoan(Long loanId, String guarantorId) {
        Loan loan = getLoanById(loanId);
        Member guarantor = memberService.getMemberById(guarantorId);
        if(loan.getGuarantors().contains(guarantor)){
            throw new IllegalArgumentException("Loan already has this guarantor");
        }
        loan.addGuarantor(guarantor);
        guarantor.addLoanGuaranteed(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }
    @Transactional
    @Override
    public LoanResponseDto removeGuarantorFromLoan(Long loanId, String guarantorId) {
        Loan loan = getLoanById(loanId);
        Member guarantor = memberService.getMemberById(guarantorId);
        if(!loan.getGuarantors().contains(guarantor)){
            throw new IllegalArgumentException("The loan does not have this guarantor");
        }
        loan.removeGuarantor(guarantor);
        guarantor.removeLoanGuaranteed(loan);
        return Mapper.loanToLoanResponseDto(loan);
    }
}
