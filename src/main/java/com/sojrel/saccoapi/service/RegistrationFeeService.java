package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.RegistrationFeeRequestDto;
import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.RegistrationFeeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegistrationFeeService {
    RegistrationFeeResponseDto addRegistrationFee(RegistrationFeeRequestDto registrationFeeRequestDto);
    List<RegistrationFeeResponseDto> getMemberRegistrationFees(String memberId);

    ItemTotalDto getTotalFee(String memberId);
}
