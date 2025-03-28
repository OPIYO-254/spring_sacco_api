package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.flashapi.model.StkPush_Entries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StkPushEntriesRepository extends JpaRepository<StkPush_Entries, String> {
    //  Find Record By MerchantRequestID or CheckoutRequestID ...
    StkPush_Entries findByMerchantRequestIDOrCheckoutRequestID(String merchantRequestID, String checkoutRequestID);

    // Find Transaction By TransactionId ...
    StkPush_Entries findByTransactionId(String transactionId);
}
