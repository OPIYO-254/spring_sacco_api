package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.LoanGuarantorRequestDto;
import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.LoanDetailsResponseDto;
import com.sojrel.saccoapi.dto.responses.LoanGuarantorResponseDto;
import com.sojrel.saccoapi.dto.responses.LoanResponseDto;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@CrossOrigin(origins={"http://10.0.2.2:8080"})
@RestController
@RequestMapping("api/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<?> addLoan(@RequestBody LoanRequestDto loanRequestDto){
        try{
            loanService.addLoan(loanRequestDto);
            return ResponseEntity.ok("{\"status\":\"success\", \"message\":\"Application submitted successfully for review.\"}");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":\"error\", \"message\":\"You still have another loan under review.\"}");
        }

    }
    @GetMapping("/get-one/{id}")
    public ResponseEntity<LoanResponseDto> getLoan(@PathVariable Long id){
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<LoanResponseDto>> getLoans(){
        List<LoanResponseDto> loanResponseDtos = loanService.getLoans();
        return new ResponseEntity<>(loanResponseDtos, HttpStatus.OK);
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<LoanResponseDto> editLoan(@PathVariable Long id, @RequestBody LoanRequestDto loanRequestDto){
        LoanResponseDto loanResponseDto = loanService.editLoan(id, loanRequestDto);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }

    @GetMapping("/get-loans/{memberId}")
    public ResponseEntity<List<LoanResponseDto>> getMembersLoans(@PathVariable String memberId){
        List<LoanResponseDto> loans = loanService.findByMember(memberId);
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }
    @GetMapping("/loan-details/{id}")
    public ResponseEntity<LoanDetailsResponseDto> loanDetails(@PathVariable Long id){
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        Long total = item.getTotal();
        LoanDetailsResponseDto dto = new LoanDetailsResponseDto();
        dto.setDto(loanResponseDto);
        dto.setTotalGuaranteed(total);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/get-guarantors/{loanId}")
    public ResponseEntity<List<LoanGuarantorResponseDto>> getLoanGuarantors(@PathVariable Long loanId){
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(loanId);
        return new ResponseEntity<>(loanGuarantorResponseDtos, HttpStatus.OK);
    }
    @PatchMapping("edit/{id}")
    public ResponseEntity<LoanResponseDto> updateLoanStatus(@PathVariable Long id, @RequestBody Map<String, String> fields){
        LoanResponseDto loanResponseDto = loanService.updateLoan(id, fields);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.OK);
    }


    @PostMapping("/delete")
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
        LoanResponseDto loanResponseDto = loanService.addGuarantorToLoan(guarantorId, loanId);
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

    @PutMapping("/update-amount")
    public ResponseEntity<Map<String, Object>> addGuaranteeAmount(
            @RequestParam String memberId,
            @RequestParam Long loanId,
            @RequestParam double amount){
        Map<String, Object> res = new HashMap<>();
        try {
            LoanGuarantor loanGuarantor= loanService.findByMemberIdAndLoanId(memberId, loanId);

            if(loanGuarantor.getAmount() == null || loanGuarantor.getAmount()==0.0){
                loanService.updateGuaranteedAmount(memberId, loanId, amount);
                res.put("status", "success");
                res.put("message", "Loan guaranteed successfully.");
                return ResponseEntity.ok(res);
            }
            else {
                res.put("message", "You have already guaranteed this loan");
                return ResponseEntity.ok(res);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "error");
            res.put("message", "Failed to guarantee loan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);
        }
    }

    @GetMapping("/guarantee-total")
    public ResponseEntity<?> getOutstandingGuaranteeAmount(@RequestParam String memberId){
        String oustanding = loanService.getGuaranteeBalance(memberId);
        return new ResponseEntity<>(oustanding, HttpStatus.OK);
    }





}

