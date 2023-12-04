package com.sojrel.saccoapi.flashapi.dto.response;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;

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
}
