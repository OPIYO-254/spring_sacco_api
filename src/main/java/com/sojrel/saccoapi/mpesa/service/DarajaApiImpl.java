package com.sojrel.saccoapi.mpesa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sojrel.saccoapi.mpesa.dto.*;
import com.sojrel.saccoapi.mpesa.config.MpesaConfiguration;
import com.sojrel.saccoapi.mpesa.utils.Constants;
import com.sojrel.saccoapi.mpesa.utils.HelperUtility;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Objects;

import static com.sojrel.saccoapi.mpesa.utils.Constants.*;

@Slf4j
@Service
public class DarajaApiImpl implements DarajaApi{
    private final MpesaConfiguration mpesaConfiguration;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public DarajaApiImpl(MpesaConfiguration mpesaConfiguration, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfiguration = mpesaConfiguration;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccessTokenResponse getAccessTokenResponse() {
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getOauthEndPoint())
                .method("GET", null)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();
        try{
            Response response = okHttpClient.newCall(request).execute();
//            System.out.println(response.code());
            assert response.body()!=null;
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
        }
        catch (IOException e){
            log.error(String.format("Could not get access token. -> %s", e.getLocalizedMessage()));
            return null;
        }
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        AccessTokenResponse accessTokenResponse = getAccessTokenResponse();
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setConfirmationURL(mpesaConfiguration.getConfirmationUrl());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setValidationURL(mpesaConfiguration.getValidationUrl());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));
        Request request = new Request.Builder()
                .url(mpesaConfiguration.getRegisterUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING,accessTokenResponse.getAccessToken()))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() !=null;
            return objectMapper.readValue(response.body().string(), RegisterUrlResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not register url ->%s", e.getLocalizedMessage()));
            return null;
        }
    }

    @Override
    public SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest) {
        AccessTokenResponse accessTokenResponse = getAccessTokenResponse();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(simulateTransactionRequest)));
        Request request = new Request.Builder()
                .url(mpesaConfiguration.getSimulateTransactionUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), SimulateTransactionResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not simulate C2B transaction -> %s", e.getLocalizedMessage()));
            return null;
        }
    }

    @Override
    public B2CTransactionSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest) {
        AccessTokenResponse accessTokenResponse = getAccessTokenResponse();
        //log.info(String.format("Access Token: %s", accessTokenResponse.getAccessToken()));

        B2CTransactionRequest b2CTransactionRequest = new B2CTransactionRequest();

        b2CTransactionRequest.setCommandID(internalB2CTransactionRequest.getCommandID());
        b2CTransactionRequest.setAmount(internalB2CTransactionRequest.getAmount());
        b2CTransactionRequest.setPartyB(internalB2CTransactionRequest.getPartyB());
        b2CTransactionRequest.setRemarks(internalB2CTransactionRequest.getRemarks());
        b2CTransactionRequest.setOccassion(internalB2CTransactionRequest.getOccassion());

        // get the security credentials ...
        b2CTransactionRequest.setSecurityCredential(HelperUtility.getSecurityCredentials(mpesaConfiguration.getB2cInitiatorPassword()));

        //log.info(String.format("Security Creds: %s", b2CTransactionRequest.getSecurityCredential()));

        // set the result url ...
        b2CTransactionRequest.setResultURL(mpesaConfiguration.getB2cResultUrl());
        b2CTransactionRequest.setQueueTimeOutURL(mpesaConfiguration.getB2cQueueTimeoutUrl());
        b2CTransactionRequest.setInitiatorName(mpesaConfiguration.getB2cInitiatorName());
        b2CTransactionRequest.setPartyA(mpesaConfiguration.getShortCode());

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(b2CTransactionRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getB2cTransactionEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;
            log.info(b2CTransactionRequest.toString());
            return objectMapper.readValue(response.body().string(), B2CTransactionSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not perform B2C transaction ->%s", e.getLocalizedMessage()));
            return null;
        }

    }


    @Override
    public StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest) {
        ExternalStkPushRequest externalStkPushRequest = new ExternalStkPushRequest();
        externalStkPushRequest.setBusinessShortCode(mpesaConfiguration.getPushShortCode());
        String transactionTimestamp = HelperUtility.getTransactionTimestamp();
        String stkPushPassword = HelperUtility.getStkPushPassword(mpesaConfiguration.getPushShortCode(),
                mpesaConfiguration.getStkPassKey(), transactionTimestamp);

        externalStkPushRequest.setPassword(stkPushPassword);
        externalStkPushRequest.setTimestamp(transactionTimestamp);
        externalStkPushRequest.setTransactionType(Constants.CUSTOMER_PAYBILL_ONLINE);
        externalStkPushRequest.setAmount(internalStkPushRequest.getAmount());
        externalStkPushRequest.setPartyA(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setPartyB(mpesaConfiguration.getPushShortCode());
        externalStkPushRequest.setPhoneNumber(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setCallBackURL(mpesaConfiguration.getStkPushRequestCallback());
        externalStkPushRequest.setAccountReference(Constants.ACCOUNT_REFERENCE_STRING);
        externalStkPushRequest.setTransactionDesc(String.format("%s Transaction", internalStkPushRequest.getPhoneNumber()));
        AccessTokenResponse accessTokenResponse = getAccessTokenResponse();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(externalStkPushRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getStkPushUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();


        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
//            System.out.println(externalStkPushRequest);
            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), StkPushSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not perform the STK push request -> %s", e.getLocalizedMessage()));
            return null;
        }
    }
}

