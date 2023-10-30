package com.sojrel.saccoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "credentials", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;

    @Column(nullable = false)
    private String idFrontName;

    @Column(nullable = false)
    private String idFrontPath;

    @Column(nullable = false)
    private String idBackName;

    @Column(nullable = false)
    private String idBackPath;

    @Column(nullable = false)
    private String kraCertName;

    @Column(nullable = false)
    private String kraCertPath;

    @Column(nullable = false)
    private String passportName;

    @Column(nullable = false)
    private String passportPath;

}

//fileName, contentType, url