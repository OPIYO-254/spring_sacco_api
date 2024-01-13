package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "repayment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Repayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime repaymentDate;

    @Column(nullable = false)
    private double amount;

//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @PrePersist
    public void prePersist(){
        if(repaymentDate==null){
            repaymentDate = LocalDateTime.now(ZoneId.of("Africa/Nairobi"));
        }
    }
}
