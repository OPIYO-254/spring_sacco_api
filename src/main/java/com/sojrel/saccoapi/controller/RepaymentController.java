package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.RepaymentRequestDto;
import com.sojrel.saccoapi.dto.responses.RepaymentResponseDto;
import com.sojrel.saccoapi.service.MemberService;
import com.sojrel.saccoapi.service.RepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repayment")
public class RepaymentController {
    @Autowired
    private RepaymentService repaymentService;
    @Autowired
    private MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<RepaymentResponseDto> addRepayment(@RequestBody RepaymentRequestDto repaymentRequestDto){
        RepaymentResponseDto repaymentResponseDto = repaymentService.addRepayment(repaymentRequestDto);
        return new ResponseEntity<>(repaymentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<RepaymentResponseDto> getRepayment(@PathVariable Long id){
        RepaymentResponseDto repaymentResponseDto = repaymentService.getRepayment(id);
        return new ResponseEntity<>(repaymentResponseDto, HttpStatus.FOUND);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<RepaymentResponseDto>> getRepayments(){
        List<RepaymentResponseDto> repaymentResponseDtos = repaymentService.getRepayments();
        return new ResponseEntity<>(repaymentResponseDtos, HttpStatus.FOUND);
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<RepaymentResponseDto> editRepayment(@PathVariable Long id, @RequestBody RepaymentRequestDto repaymentRequestDto){
        RepaymentResponseDto repaymentResponseDto = repaymentService.editRepayment(id, repaymentRequestDto);
        return new ResponseEntity<>(repaymentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/add-loan/{repaymentId}/{loanId}")
    public ResponseEntity<RepaymentResponseDto> addLoan(@PathVariable Long repaymentId, @PathVariable Long loanId){
        RepaymentResponseDto repaymentResponseDto = repaymentService.addLoanToRepayment(repaymentId, loanId);
        return new ResponseEntity<>(repaymentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove-loan/{repaymentId}/{loanId}")
    public ResponseEntity<RepaymentResponseDto> removeLoan(@PathVariable Long repaymentId, @PathVariable Long loanId){
        RepaymentResponseDto repaymentResponseDto = repaymentService.addLoanToRepayment(repaymentId, loanId);
        return new ResponseEntity<>(repaymentResponseDto, HttpStatus.OK);
    }
}

/**
 *
 */
