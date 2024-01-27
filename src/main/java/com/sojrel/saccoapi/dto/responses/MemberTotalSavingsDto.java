package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class MemberTotalSavingsDto {
    private String id;
    private String firstName;
    private String midName;
    private Long idNo;
    private String email;
    private String phone;
    private String residence;
    private Long totalSavings;
    private Boolean isActive;
}
