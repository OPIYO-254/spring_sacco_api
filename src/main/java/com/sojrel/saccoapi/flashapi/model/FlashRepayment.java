package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @PrePersist
    public void prePersist(){
        if(transactionDate == null){
            transactionDate = LocalDateTime.now(ZoneId.of("Africa/Nairobi"));
        }
    }

}
