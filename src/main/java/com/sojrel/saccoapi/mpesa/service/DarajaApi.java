package com.sojrel.saccoapi.mpesa.service;

import com.sojrel.saccoapi.mpesa.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface DarajaApi {

    AccessTokenResponse getAccessTokenResponse();

    RegisterUrlResponse registerUrl();
    SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest);
    CommonSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest);

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);

    TransactionStatusSyncResponse getTransactionResult(InternalTransactionStatusRequest internalTransactionStatusRequest);

    CommonSyncResponse checkAccountBalance();

    LNMQueryResponse getTransactionStatus(InternalLNMRequest internalLNMRequest);
}
