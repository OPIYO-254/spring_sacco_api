package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sojrel.saccoapi.flashapi.dto.request.LipaNaMpesaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "flash_loan_id")
    private FlashLoan loan;
    @Column(nullable = false)
    private double amount;
    private LocalDateTime transactionDate;
    private String mpesaReceiptNumber;
    private Long phoneNumber;
    @PrePersist
    public void prePersist(){
        if(transactionDate == null){
            transactionDate = LocalDateTime.now(ZoneId.of("Africa/Nairobi"));
        }
    }

    /**
     * {
     *     "Body": {
     *         "stkCallback": {
     *             "MerchantRequestID": "9794-55071722-2",
     *             "CheckoutRequestID": "ws_CO_17012024140651809719479842",
     *             "ResultCode": 0,
     *             "ResultDesc": "The service request is processed successfully.",
     *             "CallbackMetadata": {
     *                 "Item": [
     *                     {
     *                         "Name": "Amount",
     *                         "Value": 1
     *                     },
     *                     {
     *                         "Name": "MpesaReceiptNumber",
     *                         "Value": "SAH7AFQEHP"
     *                     },
     *                     {
     *                         "Name": "Balance"
     *                     },
     *                     {
     *                         "Name": "TransactionDate",
     *                         "Value": 20240117140706
     *                     },
     *                     {
     *                         "Name": "PhoneNumber",
     *                         "Value": 254719479842
     *                     }
     *                 ]
     *             }
     *         }
     *     }
     * }
     */

}
