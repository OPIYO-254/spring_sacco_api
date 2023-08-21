package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.responses.LoanResponseDto;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<LoanResponseDto> addLoan(@RequestBody LoanRequestDto loanRequestDto){
        LoanResponseDto loanResponseDto = loanService.addLoan(loanRequestDto);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.CREATED);
    }
    @GetMapping("/get-one/{id}")
    public ResponseEntity<LoanResponseDto> getLoan(@PathVariable Long id){
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.FOUND);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<LoanResponseDto>> getLoans(){
        List<LoanResponseDto> loanResponseDtos = loanService.getLoans();
        return new ResponseEntity<>(loanResponseDtos, HttpStatus.FOUND);
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<LoanResponseDto> editLoan(@PathVariable Long id, @RequestBody LoanRequestDto loanRequestDto){
        LoanResponseDto loanResponseDto = loanService.editLoan(id, loanRequestDto);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @PatchMapping("edit/{id}")
    public ResponseEntity<LoanResponseDto> updateLoanStatus(@PathVariable Long id, @RequestBody Map<String, String> fields){
        LoanResponseDto loanResponseDto = loanService.updateLoan(id, fields);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<LoanResponseDto> deleteLoan(@RequestParam Long id){
        LoanResponseDto loanResponseDto = loanService.deleteLoan(id);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/add_repayment/{loanId}/{repaymentId}")
    public ResponseEntity<LoanResponseDto> addRepayment(@PathVariable Long loan_id, @PathVariable Long repaymentId){
        LoanResponseDto loanResponseDto = loanService.addRepaymentToLoan(loan_id, repaymentId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove_repayment/{loanId}/{repaymentId}")
    public ResponseEntity<LoanResponseDto> removeRepayment(@PathVariable Long loan_id, @PathVariable Long repaymentId){
        LoanResponseDto loanResponseDto = loanService.removeRepaymentFromLoan(loan_id, repaymentId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @PostMapping("/add-guarantor/{loanId}/{guarantorId}")
    public ResponseEntity<LoanResponseDto> addGuarantor(@PathVariable Long loanId, @PathVariable String guarantorId){
        LoanResponseDto loanResponseDto = loanService.addGuarantorToLoan(loanId, guarantorId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove-guarantor/{loanId}/{guarantorId}")
    public ResponseEntity<LoanResponseDto> removeGuarantor(@PathVariable Long loanId, @PathVariable String guarantorId){
        LoanResponseDto loanResponseDto = loanService.removeGuarantorFromLoan(loanId, guarantorId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @PostMapping("add-borrower/{loanId}/{borrowerId}")
    public ResponseEntity<LoanResponseDto> addBorrower(@PathVariable Long loanId, @PathVariable String borrowerId){
        LoanResponseDto loanResponseDto = loanService.addBorrowerToLoan(loanId, borrowerId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @PostMapping("remove-borrower/{loanId}/{borrowerId}")
    public ResponseEntity<LoanResponseDto> removeBorrower(@PathVariable Long loanId, @PathVariable String borrowerId){
        LoanResponseDto loanResponseDto = loanService.removeBorrowerFromLoan(loanId, borrowerId);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }

}

