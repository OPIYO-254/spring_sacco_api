package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_guarantors", uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "loan_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanGuarantor {
    @EmbeddedId
    private LoanGuarantorId id = new LoanGuarantorId();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id",insertable = false, updatable = false)
    private Member member;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loan_id",insertable = false, updatable = false)
    private Loan loan;
    @Column(nullable = true)

    private Double amount;
}
