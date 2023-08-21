package com.sojrel.saccoapi.flashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sojrel.saccoapi.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name="flash")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private double principal;
    @CreationTimestamp
    private LocalDateTime applicationDate;
    @UpdateTimestamp
    private LocalDateTime repayDate;
    @Column(nullable = false)
    private Status loanStatus;
    @Column(nullable = false)
    private double amount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    public enum Status{REVIEWING,APPROVED,REJECTED,PAID, WRITTEN_OFF}

}
