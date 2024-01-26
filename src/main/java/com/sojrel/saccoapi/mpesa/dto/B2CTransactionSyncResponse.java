package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class B2CTransactionSyncResponse {
    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ResponseDescription")
    private String responseDescription;
}
