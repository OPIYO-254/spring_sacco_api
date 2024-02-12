package com.sojrel.saccoapi.mpesa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojrel.saccoapi.flashapi.model.B2C_C2B_Entries;
import com.sojrel.saccoapi.mpesa.dto.*;
import com.sojrel.saccoapi.mpesa.service.DarajaApi;
import com.sojrel.saccoapi.repository.B2CC2BEntriesRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("payments")
@CrossOrigin
public class MpesaController {
    private final AcknowledgeResponse acknowledgeResponse;

    @Autowired
    private final B2CC2BEntriesRepository b2CC2BEntriesRepository;
    @Autowired
    private final DarajaApi darajaApi;
    private final ObjectMapper objectMapper;
    public MpesaController(AcknowledgeResponse acknowledgeResponse, B2CC2BEntriesRepository b2CC2BEntriesRepository, DarajaApi darajaApi, ObjectMapper objectMapper) {
        this.acknowledgeResponse = acknowledgeResponse;
        this.b2CC2BEntriesRepository = b2CC2BEntriesRepository;
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
        B2C_C2B_Entries b2CC2BEntry = b2CC2BEntriesRepository.findByBillRefNumber(mpesaValidationResponse.getBillRefNumber());

//        b2CC2BEntry.setRawCallbackPayloadResponse(mpesaValidationResponse);
        b2CC2BEntry.setResultCode("0");
        b2CC2BEntry.setTransactionId(mpesaValidationResponse.getTransID());

        b2CC2BEntriesRepository.save(b2CC2BEntry);
        return ResponseEntity.ok(acknowledgeResponse);
    }

//    simulating c2b transaction
    @PostMapping(path = "/simulate-c2b", produces = "application/json")
    public ResponseEntity<SimulateTransactionResponse> simulateB2CTransaction(@RequestBody SimulateTransactionRequest simulateTransactionRequest) {
        SimulateTransactionResponse simulateTransactionResponse = darajaApi.simulateC2BTransaction(simulateTransactionRequest);

        B2C_C2B_Entries b2C_c2BEntry = new B2C_C2B_Entries();
        b2C_c2BEntry.setTransactionType("C2B");
        b2C_c2BEntry.setBillRefNumber(simulateTransactionRequest.getBillRefNumber());
        b2C_c2BEntry.setAmount(simulateTransactionRequest.getAmount());
        b2C_c2BEntry.setEntryDate(new Date());
        b2C_c2BEntry.setOriginatorConversationId(simulateTransactionResponse.getOriginatorCoversationID());
        b2C_c2BEntry.setConversationId(simulateTransactionResponse.getConversationID());
        b2C_c2BEntry.setMsisdn(simulateTransactionRequest.getMsisdn());

        b2CC2BEntriesRepository.save(b2C_c2BEntry);
        return ResponseEntity.ok(simulateTransactionResponse);
    }

    @PostMapping(path = "/transaction-result", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> b2cTransactionAsyncResults(@RequestBody B2CTransactionAsyncResponse b2CTransactionAsyncResponse)
            throws JsonProcessingException {
        log.info("============ Transaction Result =============");
        log.info(objectMapper.writeValueAsString(b2CTransactionAsyncResponse));
        Result b2cResult = b2CTransactionAsyncResponse.getResult();

        B2C_C2B_Entries b2cInternalRecord = b2CC2BEntriesRepository.findByConversationIdOrOriginatorConversationId(
                b2cResult.getConversationID(),
                b2cResult.getOriginatorConversationID());

//        b2cInternalRecord.setRawCallbackPayloadResponse(b2CTransactionAsyncResponse);
        b2cInternalRecord.setResultCode(String.valueOf(b2cResult.getResultCode()));
        b2cInternalRecord.setTransactionId(b2cResult.getTransactionID());

        b2CC2BEntriesRepository.save(b2cInternalRecord);
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/b2c-queue-timeout", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> queueTimeout(@RequestBody Object object) {
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/b2c-transaction", produces = "application/json")
    public ResponseEntity<CommonSyncResponse> performB2CTransaction(@RequestBody InternalB2CTransactionRequest internalB2CTransactionRequest) {
        CommonSyncResponse commonSyncResponse = darajaApi.performB2CTransaction(internalB2CTransactionRequest);

        B2C_C2B_Entries b2C_c2BEntry = new B2C_C2B_Entries();
        b2C_c2BEntry.setTransactionType("B2C");
        b2C_c2BEntry.setAmount(internalB2CTransactionRequest.getAmount());
        b2C_c2BEntry.setEntryDate(new Date());
        b2C_c2BEntry.setOriginatorConversationId(commonSyncResponse.getOriginatorConversationID());
        b2C_c2BEntry.setConversationId(commonSyncResponse.getConversationID());
        b2C_c2BEntry.setMsisdn(internalB2CTransactionRequest.getPartyB());

        b2CC2BEntriesRepository.save(b2C_c2BEntry);
        return ResponseEntity.ok(commonSyncResponse);
    }
    @PostMapping(path = "/simulate-transaction-result", produces = "application/json")
    public ResponseEntity<TransactionStatusSyncResponse> getTransactionStatusResult(@RequestBody InternalTransactionStatusRequest internalTransactionStatusRequest) {
        return ResponseEntity.ok(darajaApi.getTransactionResult(internalTransactionStatusRequest));
    }

    @GetMapping(path = "/check-account-balance", produces = "application/json")
    public ResponseEntity<CommonSyncResponse> checkAccountBalance() {
        return ResponseEntity.ok(darajaApi.checkAccountBalance());
    }

    //STK TRANSACTIONS
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

    @PostMapping(path = "/query-lnm-request", produces = "application/json")
    public ResponseEntity<LNMQueryResponse> getTransactionStatus(@RequestBody InternalLNMRequest internalLNMRequest) {
        return ResponseEntity.ok(darajaApi.getTransactionStatus(internalLNMRequest));
    }
}
