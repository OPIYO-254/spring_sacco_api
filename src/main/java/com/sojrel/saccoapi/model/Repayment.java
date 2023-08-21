package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "repayment")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
            repaymentDate = LocalDateTime.now();
        }
    }
}
