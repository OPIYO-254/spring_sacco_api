package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailsResponseDto {
    private LoanResponseDto dto;
    Long totalGuaranteed;
}
