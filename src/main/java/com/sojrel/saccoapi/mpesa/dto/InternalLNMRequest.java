package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InternalLNMRequest {
    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestID;
}
