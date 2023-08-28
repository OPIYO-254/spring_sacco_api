package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.*;
import com.sojrel.saccoapi.repository.LoanGuarantorRepository;
import com.sojrel.saccoapi.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class LoanServiceImpl implements LoanService{
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private MemberService memberService;

    private RepaymentService repaymentService;
    @Autowired
    private LoanGuarantorRepository loanGuarantorRepository;
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
    public LoanResponseDto addGuarantorToLoan(String guarantorId,Long loanId) {
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

    @Override
    public List<MemberLoansResponseDto> getAppliedLoans() {
        List<MemberLoansResponseDto> loans = loanRepository.findAppliedLoans(Loan.LoanStatus.REVIEW);
        return loans;
    }

    public List<MemberLoansResponseDto> getRejectedLoans(){
        List<MemberLoansResponseDto> loans = loanRepository.findAppliedLoans(Loan.LoanStatus.REJECTED);
        return loans;
    }

    public List<MemberLoansResponseDto> getApprovedLoans(){
        List<MemberLoansResponseDto> loans = loanRepository.findAppliedLoans(Loan.LoanStatus.APPROVED);
        return loans;
    }

    @Override
    public ItemCountDto countAppliedLoans() {
        ItemCountDto loanCount = loanRepository.countAppliedLoans(Loan.LoanStatus.REVIEW);
        return loanCount;
    }

    @Override
    public ItemCountDto countRejectedLoans() {
        ItemCountDto loanCount = loanRepository.countAppliedLoans(Loan.LoanStatus.REJECTED);
        return loanCount;
    }

    @Override
    public ItemCountDto countApprovedLoans() {
        ItemCountDto loanCount = loanRepository.countAppliedLoans(Loan.LoanStatus.APPROVED);
        return loanCount;
    }

    @Override
    public ItemCountDto countCompletedLoans() {
        ItemCountDto loanCount = loanRepository.countAppliedLoans(Loan.LoanStatus.COMPLETED);
        return loanCount;
    }


    @Override
    public void updateGuaranteedAmount(String memberId, Long loanId, Double amount) {
        LoanGuarantor loanGuarantor = loanGuarantorRepository.findByMemberIdAndLoanId(memberId, loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan guarantor not found"));
        loanGuarantor.setAmount(amount);
        loanGuarantorRepository.save(loanGuarantor);
    }

    @Override
    public List<LoanGuarantorResponseDto> getLoanGuarantors(Long loanId) {
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanGuarantorRepository.findLoanGuarantorById(loanId);
        return loanGuarantorResponseDtos;
    }

    @Override
    public ItemTotalDto getTotalGuaranteed(Long loanId) {
        Long totalGuaranteed = loanGuarantorRepository.getTotalGuaranteed(loanId);
        ItemTotalDto itemTotalDto = new ItemTotalDto();
        itemTotalDto.setTotal(totalGuaranteed);
        return itemTotalDto;
    }

    @Override
    public void approveLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        loan.setLoanStatus(Loan.LoanStatus.APPROVED);
        loanRepository.save(loan);
    }

    @Override
    public void rejectLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        loan.setLoanStatus(Loan.LoanStatus.REJECTED);
        loanRepository.save(loan);
    }


//    @Override
//    public LoanGuarantorResponseDto updateGuaranteedAmount(String memberId, Long loanId, Double amount) {
//        LoanGuarantorResponseDto loanGuarantorResponseDto =loanRepository.addGuaranteedAmount(memberId,loanId, amount);
//        return loanGuarantorResponseDto;
//    }

//    @Override
//    public LoanResponseDto updateGuaranteedAmount(Long loanId, Map<String, Double> fields) {
//        Loan loan = getLoanById(loanId);
//        List<Member> guarantors = loan.getGuarantors();
//
//        for (Member m:guarantors) {
//            if(m.getId()==)
//        }
//        if(Objects.nonNull(loan)) {
//            fields.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(Loan.class, key);
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, loan, Loan.LoanStatus.valueOf(value));
//            });
//            loanRepository.save(loan);
//            return Mapper.loanToLoanResponseDto(loan);
//        }
//        return null;
//        return null;
//    }
}


