package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class B2CTransactionAsyncResponse {
    @JsonProperty("Result")
    private Result result;
}
