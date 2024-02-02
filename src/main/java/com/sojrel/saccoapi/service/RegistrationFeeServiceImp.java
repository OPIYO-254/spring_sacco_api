package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.RegistrationFeeRequestDto;
import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.RegistrationFeeResponseDto;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.RegistrationFee;
import com.sojrel.saccoapi.repository.RegistrationFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RegistrationFeeServiceImp implements RegistrationFeeService{
    @Autowired
    private RegistrationFeeRepository repository;

    @Autowired
    private MemberService memberService;
    @Override
    public RegistrationFeeResponseDto addRegistrationFee(RegistrationFeeRequestDto registrationFeeRequestDto) {
        RegistrationFee registrationFee = new RegistrationFee();
        registrationFee.setPayDate(registrationFeeRequestDto.getPayDate());
        Member member = memberService.getMemberById(registrationFeeRequestDto.getMemberId());
        if(Objects.nonNull(member)){
            registrationFee.setMember(member);
        }
        registrationFee.setAmount(registrationFeeRequestDto.getAmount());
        repository.save(registrationFee);
        return Mapper.registrationFeeToRegistrationFeeResponseDto(registrationFee);
    }

    @Override
    public List<RegistrationFeeResponseDto> getMemberRegistrationFees(String memberId) {
//        List<RegistrationFee> fees= new ArrayList<>();
//        List<RegistrationFee> registrationFees = repository.findAll();
//        for(RegistrationFee registrationFee : registrationFees){
//            if(registrationFee.getMember().getId().equals(memberId)){
//                fees.add(registrationFee);
//            }
//        }
        List<RegistrationFee> fees = repository.findByMember(memberService.getMemberById(memberId));
        return Mapper.registrationFeesToRegistrationFeeResponseDtos(fees);
    }

    @Override
    public ItemTotalDto getTotalFee(String memberId) {
        ItemTotalDto dto = repository.getTotalMemberFee(memberId);
        return dto;
    }
}
