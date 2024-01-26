package com.sojrel.saccoapi.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResultParameters {
    @JsonProperty("ResultParameter")
    private List<ResultParameterItem> resultParameter;
}
