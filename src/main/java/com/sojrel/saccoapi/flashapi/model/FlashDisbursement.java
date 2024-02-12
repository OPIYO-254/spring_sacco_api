package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashDisbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name="flash_loan_id")
    private FlashLoan loan;
    private LocalDateTime disburseDate;
    private double amount;

}

//    c2b response
/**
 * {
 *     "Result": {
 *         "ResultType": 0,
 *         "ResultCode": 0,
 *         "ResultDesc": "The service request is processed successfully.",
 *         "OriginatorConversationID": "53e3-4aa8-9fe0-8fb5e4092cdd303900",
 *         "ConversationID": "AG_20240205_20103c9a4a96805ae9e6",
 *         "TransactionID": "SB592HK4IN",
 *         "ResultParameters": {
 *             "ResultParameter": [
 *                 {
 *                     "Key": "TransactionAmount",
 *                     "Value": 10
 *                 },
 *                 {
 *                     "Key": "TransactionReceipt",
 *                     "Value": "SB592HK4IN"
 *                 },
 *                 {
 *                     "Key": "ReceiverPartyPublicName",
 *                     "Value": "254708374149 - John Doe"
 *                 },
 *                 {
 *                     "Key": "TransactionCompletedDateTime",
 *                     "Value": "05.02.2024 11:46:15"
 *                 },
 *                 {
 *                     "Key": "B2CUtilityAccountAvailableFunds",
 *                     "Value": 5590293.22
 *                 },
 *                 {
 *                     "Key": "B2CWorkingAccountAvailableFunds",
 *                     "Value": 0
 *                 },
 *                 {
 *                     "Key": "B2CRecipientIsRegisteredCustomer",
 *                     "Value": "Y"
 *                 },
 *                 {
 *                     "Key": "B2CChargesPaidAccountAvailableFunds",
 *                     "Value": -1925
 *                 }
 *             ]
 *         },
 *         "ReferenceData": {
 *             "ReferenceItem": {
 *                 "Key": "QueueTimeoutURL",
 *                 "Value": "https://internalsandbox.safaricom.co.ke/mpesa/b2cresults/v1/submit"
 *             }
 *         }
 *     }
 * }
 */




