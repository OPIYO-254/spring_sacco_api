package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.model.LoanGuarantor;
import com.sojrel.saccoapi.model.LoanGuarantorId;
import com.sojrel.saccoapi.repository.LoanGuarantorRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LoanGuarantorServiceImp implements LoanGuarantorService{
    @Autowired
    private LoanGuarantorRepository loanGuarantorRepository;

}
