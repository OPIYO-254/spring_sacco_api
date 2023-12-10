package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InternalStkPushRequest {
    @JsonProperty("PhoneNumber")
    private String phoneNumber;
    @JsonProperty("Amount")
    private String amount;
}
