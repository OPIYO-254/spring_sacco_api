package com.sojrel.saccoapi.model;

//import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "member")
//@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GenericGenerator(name = "id", strategy = "com.sojrel.saccoapi.model.MemberIdGenerator")
    @GeneratedValue(generator = "id")
    private String id;

    @Column(nullable = false, updatable = false)
    private String firstName;

    @Column(updatable = false)
    private String midName;

    @Column(nullable = false, updatable = false)
    private String lastName;

    @Column(updatable = false)
    private LocalDateTime regDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private Long idNo;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(nullable = false, unique = true)
    private String phone;

    private String alternativePhone;

    @Column(nullable = false,unique = true, updatable = false)
    private String kraPin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String residence;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contribution> contributions;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Loan> loansTaken;

    @ManyToMany(mappedBy = "guarantors", fetch = FetchType.LAZY)
    private List<Loan> loansGuaranteed;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FlashLoan> flashLoans;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserFiles> userFiles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate.atZone(ZoneId.of("Africa/Nairobi")).toLocalDateTime();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdNo() {
        return idNo;
    }

    public void setIdNo(Long idNo) {
        this.idNo = idNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    public String getKraPin() {
        return kraPin;
    }

    public void setKraPin(String kraPin) {
        this.kraPin = kraPin;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Contribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public List<Loan> getLoansTaken() {
        return loansTaken;
    }

    public void setLoansTaken(List<Loan> loansTaken) {
        this.loansTaken = loansTaken;
    }

    public List<Loan> getLoansGuaranteed() {
        return loansGuaranteed;
    }

    public void setLoansGuaranteed(List<Loan> loansGuaranteed) {
        this.loansGuaranteed = loansGuaranteed;
    }

    public List<FlashLoan> getFlashLoans() {
        return flashLoans;
    }

    public void setFlashLoans(List<FlashLoan> flashLoans) {
        this.flashLoans = flashLoans;
    }

    public List<UserFiles> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<UserFiles> userFiles) {
        this.userFiles = userFiles;
    }

    public enum Gender{
        MALE,FEMALE
    }
    public void addContribution(Contribution contribution){
        contributions.add(contribution);
    }

    public void removeContribution(Contribution contribution){
        contributions.remove(contribution);
    }

    public void addLoanTaken(Loan loan){
        loansTaken.add(loan);
    }

    public void removeLoanTaken(Loan loan){
        loansTaken.remove(loan);
    }

    public void addLoanGuaranteed(Loan loan){
        loansGuaranteed.add(loan);
    }

    public void removeLoanGuaranteed(Loan loan){
        loansGuaranteed.remove(loan);
    }


    @PrePersist
    public void prePersist(){
        if(regDate==null){
            regDate = LocalDateTime.now();
        }
    }
}
