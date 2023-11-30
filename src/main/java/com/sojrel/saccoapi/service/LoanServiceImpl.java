package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.requests.RepaymentRequestDto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.sojrel.saccoapi.utils.DateConverter.convertDateFormat;

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
//        System.out.println(loanRequestDto.getLoanType());
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
    public List<MemberLoansResponseDto> getCompletedLoans() {
        List<MemberLoansResponseDto> loans = loanRepository.findCompletedLoans(Loan.LoanStatus.COMPLETED);
        return loans;
    }

    @Override
    public List<MemberLoansResponseDto> findRepayingLoans() {
        List<MemberLoansResponseDto> loans = loanRepository.findRepayingLoans(Loan.LoanStatus.APPROVED);
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
    public ItemCountDto countRepayingLoans() {
        ItemCountDto loanCount = loanRepository.countRepayingLoans(Loan.LoanStatus.APPROVED);
        return loanCount;
    }

    @Override
    public ItemCountDto countCompletedLoans() {
        ItemCountDto loanCount = loanRepository.countCompletedLoans(Loan.LoanStatus.COMPLETED);
        return loanCount;
    }

    public LoanGuarantor findByMemberIdAndLoanId(String memberId, Long loanId){
        LoanGuarantor loanGuarantor = loanGuarantorRepository.findByMemberIdAndLoanId(memberId, loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan guarantor not found"));
        return loanGuarantor;
    }
    @Override
    public void updateGuaranteedAmount(String memberId, Long loanId, Double amount) {
        LoanGuarantor loanGuarantor = findByMemberIdAndLoanId(memberId, loanId);
        loanGuarantor.setAmount(amount);
        loanGuarantorRepository.save(loanGuarantor);

    }

    @Override
    public LoanGuarantor findLoanGuarantor(String memberId, Long loanId) {
        return null;
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

    /*
    * This method is to be used in loan repayment function to ensure that fully repaid loans are marked as completed
    * */
    @Override
    public void completeLoan(Long loanId) {
        Loan loan = getLoanById(loanId);
        loan.setLoanStatus(Loan.LoanStatus.COMPLETED);
        loanRepository.save(loan);
    }

    @Override
    public RepaymentResponseDto addLoanRepayment(RepaymentRequestDto repaymentRequestDto) {
        RepaymentResponseDto repaymentResponseDto = repaymentService.addRepayment(repaymentRequestDto);
        return repaymentResponseDto;
    }


    @Override
    public List<RepaymentResponseDto> getLoanRepayments(Long loanId) {
        List<RepaymentResponseDto> repayments = loanRepository.getLoanRepayments(loanId);
        return repayments;
    }

    @Override
    public TotalDoubleItem getTotalRepaid(Long loanId) {
        TotalDoubleItem totalRepaid = loanRepository.getTotalRepaid(loanId);
        return totalRepaid;
    }

    @Override
    public ItemTotalDto getMonthlyDisbursement(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = now.format(formatter);
        Long monthlyTotal = loanRepository.getTotalDisbursement(formattedDate+'%');
//        System.out.println(formattedDate);
        ItemTotalDto total = new ItemTotalDto();
        total.setTotal(monthlyTotal);
//        System.out.println(total);
        return  total;
    }

    @Override
    public ItemTotalDto getAnnualDisbursement(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedDate = now.format(formatter);
        Long monthlyTotal = loanRepository.getTotalDisbursement(formattedDate+'%');
        ItemTotalDto total = new ItemTotalDto();
        total.setTotal(monthlyTotal);
        return  total;
    }

    @Override
    public List<LoanResponseDto> findByMember(String memberId){
        Member member = memberService.getMemberById(memberId);
        List<Loan>loans = loanRepository.findByBorrower(member);
        return Mapper.loanToLoanResponseDtos(loans);
    }

    @Override
    public List<KeyValueDto> totalMonthlyDisbursements(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusYears(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Object[]> monthlyDisbursements = loanRepository.getTotalMonthlyDisbursements(start.format(dateFormatter), now.format(dateFormatter));
        List<KeyValueDto> keyValueDtos = new ArrayList<>();
//        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        for (Object[] row : monthlyDisbursements) {
            String formattedDate = convertDateFormat((String)row[0]);
            String key = formattedDate;
            Double value = (Double) row[1];
            KeyValueDto keyValueDto = new KeyValueDto(key, value);
            keyValueDtos.add(keyValueDto);
        }

//        System.out.println(keyValueDtos);
        return keyValueDtos;
    }
    @Override
    public List<KeyValueDto> totalPerLoanCategory(){
        List<Object[]> totalLoans = loanRepository.getTotalPerLoanCategory();
        List<KeyValueDto> keyValueDtos = new ArrayList<>();
        for(Object[] row: totalLoans){
            String key = (String) row[0];
            Double value = (Double) row[1];
            KeyValueDto dto = new KeyValueDto(key, value);
            keyValueDtos.add(dto);
        }
//        System.out.println(keyValueDtos);
        return keyValueDtos;
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


