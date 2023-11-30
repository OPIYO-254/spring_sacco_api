package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTotalContributionsDto {
    private MemberResponseDto member;
    private Long totalSavings;
    private Long totalShares;
    private Long totalContributions;
    private String passportUrl;
}
