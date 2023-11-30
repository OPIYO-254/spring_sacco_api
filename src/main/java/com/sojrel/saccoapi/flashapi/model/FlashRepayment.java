package com.sojrel.saccoapi.flashapi.model;

import com.sojrel.saccoapi.flashapi.dto.request.LipaNaMpesaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "flash_load_id")
    private FlashLoan loan;
    @Column(nullable = false)
    private double amount;
    private String mpesaReceiptNumber;
    private LocalDateTime transactionDate;
    private Long phoneNumber;


}
