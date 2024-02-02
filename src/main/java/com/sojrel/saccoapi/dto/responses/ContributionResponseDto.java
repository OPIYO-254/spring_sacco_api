package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionResponseDto {
    private Long id;
    private Date contributionDate;
    private String contributionType;
    private int amount;
    private String memberId;
}
