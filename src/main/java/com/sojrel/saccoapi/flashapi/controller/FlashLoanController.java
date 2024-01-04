package com.sojrel.saccoapi.flashapi.controller;

import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanMapper;
import com.sojrel.saccoapi.flashapi.dto.response.FlashRepaymentAndTotalRepaid;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.dto.request.FlashLoanRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashLoanResponseDto;
import com.sojrel.saccoapi.flashapi.repository.FlashLoanRepository;
import com.sojrel.saccoapi.flashapi.service.FlashLoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@CrossOrigin(origins={"http://10.0.2.2:8080"})
@RestController
@RequestMapping("/api/v1/flash/")
public class FlashLoanController {
    @Autowired
    private FlashLoanService flashLoanService;
    @Autowired
    private FlashLoanRepository flashLoanRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addFlashLoan(@RequestBody FlashLoanRequestDto requestDto){
        List<FlashLoanResponseDto> loanResponseDtos = flashLoanService.getFlashLoanByMemberId(requestDto.getMemberId());
        List<FlashLoanResponseDto> unpaidLoans = new ArrayList<>();
        if(Objects.nonNull(loanResponseDtos)){
            for (FlashLoanResponseDto loanDto: loanResponseDtos) {
                if(loanDto.getLoanStatus() == "REVIEWING" || loanDto.getLoanStatus()=="APPROVED"){
                    unpaidLoans.add(loanDto);
                }
            }
        }
        if(unpaidLoans.isEmpty()) {
            try {

                FlashLoanResponseDto dto = flashLoanService.addFlashLoan(requestDto);
                return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Loan application successful\"}");

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"status\": \"error\", \"message\": \"Error in sending request\"}");
            }
        }
        else {
            return ResponseEntity.ok("{\"status\": \"error\", \"message\": \"You have unpaid loan. Please repay first and try again.\"}");
        }

    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<FlashLoanResponseDto>  editFlashLoan(@PathVariable Long id, @RequestBody FlashLoanRequestDto dto){
        FlashLoan loan = flashLoanService.getFlashLoanById(id);
        loan.setLoanStatus(FlashLoan.Status.valueOf(dto.getLoanStatus()));
        loan.setPrincipal(dto.getPrincipal());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(dto.getRepayDate(), formatter);
        loan.setRepayDate(dateTime);
        flashLoanRepository.save(loan);
        FlashLoanResponseDto flashLoanResponseDto= FlashLoanMapper.flashLoanToDto(loan);
        return new ResponseEntity<>(flashLoanResponseDto, HttpStatus.OK);
    }


    @GetMapping("/get-one/{id}")
    public ResponseEntity<FlashLoanResponseDto> getFlashLoan(@PathVariable Long id){
        FlashLoanResponseDto dto = flashLoanService.getFlashLoan(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<FlashLoanResponseDto>> getAllLoans(){
        List<FlashLoanResponseDto> dtoList = flashLoanService.getAllFlashLoans();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/get/{memberId}")
    public ResponseEntity<List<FlashLoanResponseDto>> getMemberLoans(@PathVariable String memberId){
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByMemberId(memberId);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/get/status")
    public ResponseEntity<List<FlashLoanResponseDto>> getLoansByStatus(@PathVariable String status){
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByStatus(status);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
    @GetMapping("/get/date")
    public ResponseEntity<List<FlashLoanResponseDto>> getLoansByDateApplied(@PathVariable LocalDateTime date){
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByApplicationDate(date);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/loan-repayment-amount/{id}")
    public ResponseEntity<FlashRepaymentAndTotalRepaid> getLoanAndRepayment(@PathVariable Long id){
        FlashRepaymentAndTotalRepaid flashRepaymentAndTotalRepaid = flashLoanService.getLoanAndRepayment(id);
        return new ResponseEntity<>(flashRepaymentAndTotalRepaid, HttpStatus.OK);
    }

    @GetMapping("/get-limit/{memberId}")
    public ResponseEntity<?> getLoanLimit(@PathVariable String memberId){
        try {
            double limit = flashLoanService.determineLoanLimit(memberId);
            return new ResponseEntity<>(limit, HttpStatus.OK);
        }
        catch (Exception e){
            log.error("error in getting limit "+e.getLocalizedMessage());
            return null;
        }

    }

}
