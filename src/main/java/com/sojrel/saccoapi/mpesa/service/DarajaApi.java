package com.sojrel.saccoapi.mpesa.service;

import com.sojrel.saccoapi.mpesa.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface DarajaApi {

    AccessTokenResponse getAccessTokenResponse();

    RegisterUrlResponse registerUrl();
    SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest);

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);
}
