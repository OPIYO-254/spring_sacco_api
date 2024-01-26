package com.sojrel.saccoapi.mpesa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @PostMapping(path = "/simulate-c2b", produces = "application/json")
    public ResponseEntity<SimulateTransactionResponse> simulateB2CTransaction(@RequestBody SimulateTransactionRequest simulateTransactionRequest) {
        return ResponseEntity.ok(darajaApi.simulateC2BTransaction(simulateTransactionRequest));
    }

    @PostMapping(path = "/b2c-transaction-result", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> b2cTransactionAsyncResults(@RequestBody B2CTransactionAsyncResponse b2CTransactionAsyncResponse)
            throws JsonProcessingException {
        log.info("============ B2C Transaction Response =============");
        log.info(objectMapper.writeValueAsString(b2CTransactionAsyncResponse));
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/b2c-queue-timeout", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> queueTimeout(@RequestBody Object object) {
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/b2c-transaction", produces = "application/json")
    public ResponseEntity<B2CTransactionSyncResponse> performB2CTransaction(@RequestBody InternalB2CTransactionRequest internalB2CTransactionRequest) {

        return ResponseEntity.ok(darajaApi.performB2CTransaction(internalB2CTransactionRequest));
    }

    @PostMapping(path="/stk-transaction-request", produces = "application/json")
    public ResponseEntity<StkPushSyncResponse> stkPushTransaction(@RequestBody InternalStkPushRequest internalStkPushRequest){
        return ResponseEntity.ok(darajaApi.performStkPushTransaction(internalStkPushRequest));
    }

    @SneakyThrows
    @PostMapping(path="/stk-transaction-result", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> acknowledgeStkPushResponse(@RequestBody StkPushAsyncResonse stkPushAsyncResonse){
        log.info("======= STK Push Async Response =====");
        log.info(objectMapper.writeValueAsString(stkPushAsyncResonse));
        return ResponseEntity.ok(acknowledgeResponse);
    }
}
