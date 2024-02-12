package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.flashapi.model.B2C_C2B_Entries;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface B2CC2BEntriesRepository extends JpaRepository<B2C_C2B_Entries,String> {

    B2C_C2B_Entries findByBillRefNumber(String billRefNumber);

    B2C_C2B_Entries findByConversationIdOrOriginatorConversationId(String conversationID, String originatorConversationID);
}
