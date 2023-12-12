package com.sojrel.saccoapi.flashapi.controller;

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

    @PostMapping("/repay")
    public ResponseEntity<?> makeRepayment(@RequestBody FlashRepaymentRequestDto dto){
        try{
            flashRepaymentService.addRepayment(dto);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\":\"Repayment successful\"}");
        }
        catch (Exception e){
            log.error("error: "+e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":\"error\", \"message\":\"Server error\"}");
        }
    }

//    @GetMapping("/get-one")
//    public ResponseEntity<FlashRepaymentResponseDto> getLoanRepayment(){
//        return null;
//    }
    @GetMapping("/get-all")
    public ResponseEntity<List<FlashRepaymentResponseDto>> getLoanRepayments(){
        List<FlashRepaymentResponseDto> dtoList = flashRepaymentService.getAllRepayments();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
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
}
