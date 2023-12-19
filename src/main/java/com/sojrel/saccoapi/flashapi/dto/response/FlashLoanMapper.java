package com.sojrel.saccoapi.flashapi.dto.response;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlashLoanMapper {
    public static FlashLoanResponseDto flashLoanToDto(FlashLoan loan){
        FlashLoanResponseDto dto = new FlashLoanResponseDto();
        dto.setId(loan.getId());
        dto.setPrincipal(loan.getPrincipal());
        dto.setApplicationDate(loan.getApplicationDate());
        dto.setMemberId(loan.getMember().getId());
        dto.setRepayDate(loan.getRepayDate());
        dto.setLoanStatus(String.valueOf(loan.getLoanStatus()));
        dto.setAmount(loan.getAmount());
        dto.setRepayments(loan.getRepayments());
        return dto;
    }

    public static List<FlashLoanResponseDto> flashLoansToFlashLoanDtos(List<FlashLoan> loans){
        List<FlashLoanResponseDto> dtoList = new ArrayList<>();
        if(Objects.nonNull(loans)){
            for (FlashLoan loan:loans) {
                dtoList.add(flashLoanToDto(loan));
            }
        }
        return dtoList;
    }

    public static FlashRepaymentResponseDto flashRepaymentToFlashRepaymentResponseDto(FlashRepayment repayment){
        FlashRepaymentResponseDto dto = new FlashRepaymentResponseDto();
        dto.setId(repayment.getId());
        dto.setLoanId(repayment.getLoan().getId());
        dto.setTransactionDate(repayment.getTransactionDate());
        dto.setAmount(repayment.getAmount());
        return dto;
    }

    public static List<FlashRepaymentResponseDto> flashRepaymentsToFlashRepaymentDtos(List<FlashRepayment> repayments){
        List<FlashRepaymentResponseDto> dtoList = new ArrayList<>();
        if(Objects.nonNull(repayments)){
            for(FlashRepayment repayment: repayments){
                dtoList.add(flashRepaymentToFlashRepaymentResponseDto(repayment));
            }
        }
        return dtoList;
    }

}
