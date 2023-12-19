package com.sojrel.saccoapi.flashapi.controller;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.TotalDoubleItem;
import com.sojrel.saccoapi.flashapi.dto.request.FlashRepaymentRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.FlashRepaymentResponseDto;
import com.sojrel.saccoapi.flashapi.service.FlashRepaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/flash-repayment/")
public class FlashRepaymentController {
    @Autowired
    private FlashRepaymentService flashRepaymentService;

    /**
     *This functions sends a flash loan repayment request and
     * returns a response with message and status
     * @param dto
     * @return
     */
    @PostMapping("/repay")
    public ResponseEntity<?> makeRepayment(@RequestBody FlashRepaymentRequestDto dto){
        try{
            flashRepaymentService.makeRepayment(dto);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\":\"Repayment successful\"}");
        }
        catch (Exception e){
            log.error("error: "+e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":\"error\", \"message\":\"Server error\"}");
        }
    }

    /**
     * The function for getting all items from flash_loan_repayment table
     * @return
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<FlashRepaymentResponseDto>> getLoanRepayments(){
        List<FlashRepaymentResponseDto> dtoList = flashRepaymentService.getAllRepayments();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    /**
     * The function for fetching a list of repayments for a particular loan
     * @param loanId
     * @return
     */
    @GetMapping("/get/{loanId}")
    public ResponseEntity<?> getLoanRepayments(@PathVariable Long loanId){
        try{
            List<FlashRepaymentResponseDto> dtoList = flashRepaymentService.getLoanRepayments(loanId);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }
        catch (Exception e){
            log.error("error "+e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Server error\"}");
        }
    }

    @GetMapping("total-repaid/{loanId}")
    public ResponseEntity<TotalDoubleItem> getTotalRepaidAmount(@PathVariable Long loanId){
        TotalDoubleItem dto = flashRepaymentService.getTotalLoanRepaid(loanId);
//        Long total = dto.getTotal();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
