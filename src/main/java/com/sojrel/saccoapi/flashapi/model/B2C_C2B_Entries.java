package com.sojrel.saccoapi.flashapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class B2C_C2B_Entries {
    @Id
    private String internalId;

    private String transactionType;

    @Column(unique = true)
    private String transactionId;

    private String billRefNumber;

    private String msisdn;

    private String amount;

    @Column(unique = true)
    private String conversationId;

    @Column(unique = true)
    private String originatorConversationId;

    private Date EntryDate;

    private String resultCode;

//    private Object rawCallbackPayloadResponse;
}
