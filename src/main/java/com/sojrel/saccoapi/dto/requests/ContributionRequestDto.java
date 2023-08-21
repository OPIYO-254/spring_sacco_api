package com.sojrel.saccoapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionRequestDto {
    private String contributionType;
    private String memberId;
    private int amount;

}
