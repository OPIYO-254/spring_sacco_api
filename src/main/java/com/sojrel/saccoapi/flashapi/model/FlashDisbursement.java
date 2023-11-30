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
/**
"TransactionType": "Pay Bill",
"TransID":"RKTQDM7W6S",
"TransTime":"20191122063845",
"TransAmount":"10"
"BusinessShortCode": "600638",
"BillRefNumber":"invoice008",
"InvoiceNumber":"",
"OrgAccountBalance":""
"ThirdPartyTransID": "",
"MSISDN":"25470****149",
"FirstName":"John",
"MiddleName":""
"LastName":"Doe"
 */
