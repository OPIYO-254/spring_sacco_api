package com.sojrel.saccoapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LoanGuarantorId implements Serializable {

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "member_id")
    private String memberId;

    // Constructors, getters, setters, equals, and hashCode methods
}
