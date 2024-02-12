package com.sojrel.saccoapi.flashapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StkPush_Entries {
    @Id
    private String internalId;

    @Column(unique = true)
    private String transactionId;

    private String transactionType;

    private String msisdn;

    private Long amount;

    @Column(unique = true)
    private String merchantRequestID;

    @Column(unique = true)
    private String checkoutRequestID;

    private Date entryDate;

    private String resultCode;

    private String rawCallbackPayloadResponse;
}
