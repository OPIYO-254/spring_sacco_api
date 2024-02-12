package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SimulateTransactionResponse {
    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("OriginatorCoversationID")
    private String originatorCoversationID;

    @JsonProperty("ConversationID")
    private String conversationID;


}
