package com.sojrel.saccoapi.mpesa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojrel.saccoapi.mpesa.dto.*;
import com.sojrel.saccoapi.mpesa.service.DarajaApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("payments")
public class MpesaController {
    private final AcknowledgeResponse acknowledgeResponse;
    @Autowired
    private final DarajaApi darajaApi;
    private final ObjectMapper objectMapper;
    public MpesaController(AcknowledgeResponse acknowledgeResponse, DarajaApi darajaApi, ObjectMapper objectMapper) {
        this.acknowledgeResponse = acknowledgeResponse;
        this.darajaApi = darajaApi;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/token")
    public ResponseEntity<AccessTokenResponse> getAccessToken(){
        return ResponseEntity.ok(darajaApi.getAccessTokenResponse());
    }

    @PostMapping("/register-url")
    public ResponseEntity<RegisterUrlResponse> registerUrl(){
        return ResponseEntity.ok(darajaApi.registerUrl());
    }

//    simulating validation transaction
    @PostMapping("/validation")
    public ResponseEntity<AcknowledgeResponse> mpesaValidation(@RequestBody MpesaValidationResponse mpesaValidationResponse) {
        return ResponseEntity.ok(acknowledgeResponse);
    }

//    similating c2b transaction
    @PostMapping("/simulate-c2b")
    public ResponseEntity<SimulateTransactionResponse> simulateB2CTransaction(@RequestBody SimulateTransactionRequest simulateTransactionRequest) {
        return ResponseEntity.ok(darajaApi.simulateC2BTransaction(simulateTransactionRequest));
    }

    @PostMapping("/stk-transaction-request")
    public ResponseEntity<StkPushSyncResponse> stkPushTransaction(@RequestBody InternalStkPushRequest internalStkPushRequest){
        return ResponseEntity.ok(darajaApi.performStkPushTransaction(internalStkPushRequest));
    }

    @SneakyThrows
    @PostMapping("/stk-transaction-result")
    public ResponseEntity<AcknowledgeResponse> acknowledgeStkPushResponse(@RequestBody StkPushAsyncResonse stkPushAsyncResonse){
        log.info("======= STK Push Async Response =====");
        log.info(objectMapper.writeValueAsString(stkPushAsyncResonse));
        return ResponseEntity.ok(acknowledgeResponse);
    }
}
