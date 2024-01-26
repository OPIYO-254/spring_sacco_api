package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReferenceData {
    @JsonProperty("ReferenceItem")
    private ReferenceItem referenceItem;
}
