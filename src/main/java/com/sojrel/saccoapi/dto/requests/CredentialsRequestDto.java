package com.sojrel.saccoapi.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsRequestDto {
    private String idFrontName;
    private String idFrontPath;
    private String idBackName;
    private String idBackPath;
    private String kraCertName;
    private String kraCertPath;
    private String passportName;
    private String passportPath;
}
