package com.sojrel.saccoapi.flashapi.dto.response;

import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashRepaymentAndTotalRepaid {
   private FlashLoan loan;
   double totalRepaid;
}
