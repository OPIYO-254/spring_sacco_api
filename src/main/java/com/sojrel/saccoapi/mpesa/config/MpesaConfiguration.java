package com.sojrel.saccoapi.mpesa.config;

import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
public class MpesaConfiguration {
    private String consumerKey;
    private String consumerSecret;
    private String grantType;
    private String oauthEndPoint;
    private String shortCode;
    private String responseType;
    private String confirmationUrl;
    private String validationUrl;
    private String registerUrl;
    private String simulateTransactionUrl;
    private String stkPassKey;
    private String pushShortCode;
    private String stkPushUrl;
    private String stkPushRequestCallback;
    private String b2cTransactionEndpoint;
    private String b2cResultUrl;
    private String b2cQueueTimeoutUrl;
    private String b2cInitiatorName;
    private String b2cInitiatorPassword;
    private String transactionResultUrl;
    private String checkAccountBalanceUrl;
    private String lnmQueryRequestUrl;


    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
    @Override
    public String toString() {
        return "MpesaConfiguration{" +
                "consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                ", oauthEndPoint='" + oauthEndPoint + '\'' +
                '}';
    }
}
