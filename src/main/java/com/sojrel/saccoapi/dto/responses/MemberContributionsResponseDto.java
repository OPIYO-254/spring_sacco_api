package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class MemberContributionsResponseDto {
    private Long id;
    private String memberId;
    private String firstName;
    private String midName;
    private LocalDateTime contributionDate;
    private String contributionType;
    private int amount;

}
