package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Entity
@Table(name="contribution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime contributionDate=LocalDateTime.now(ZoneId.of("Africa/Nairobi"));

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    @Column(nullable = false)
    private int amount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public enum ContributionType{
        SHARES, SAVINGS
    }


//    @PrePersist
//    public void prePersist(){
//        if(contributionDate == null){
//            contributionDate = ;
//        }
//    }
}
