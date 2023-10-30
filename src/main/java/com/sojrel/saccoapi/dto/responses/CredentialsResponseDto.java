package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsResponseDto {
    private Long id;
    private String idFrontName;
    private String idFrontPath;
    private String idBackName;
    private String idBackPath;
    private String kraCertName;
    private String kraCertPath;
    private String passportName;
    private String passportPath;
}
