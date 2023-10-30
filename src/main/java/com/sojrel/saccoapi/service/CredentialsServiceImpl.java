package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.CredentialsRequestDto;
import com.sojrel.saccoapi.dto.responses.CredentialsResponseDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.model.Credentials;
import com.sojrel.saccoapi.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class CredentialsServiceImpl implements CredentialsService{
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private MemberService memberService;
    @Override
    public CredentialsResponseDto addCredentials(CredentialsResponseDto credentialsResponseDto) {
        Credentials credentials = new Credentials();
        credentials.setIdBackPath(credentialsResponseDto.getIdBackPath());
        credentials.setIdFrontPath(credentialsResponseDto.getIdFrontPath());
        credentials.setKraCertPath(credentialsResponseDto.getKraCertPath());
        credentials.setPassportPath(credentialsResponseDto.getPassportPath());
        credentialsRepository.save(credentials);
        return Mapper.credentialToCredentialResponseDto(credentials);
    }

    @Override
    public Credentials getCredentialById(Long id) {
        return credentialsRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("Credentials with id" + id+ " not found"));
    }

    @Override
    public List<CredentialsResponseDto> getAllCredentials() {
        List<Credentials> credentialsList = StreamSupport.stream(credentialsRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return Mapper.credentialsToCredentialResponseDtoList(credentialsList);
    }

    @Override
    public CredentialsResponseDto editCredentials(Long id, CredentialsRequestDto credentialsRequestDto) {
        Credentials credentials = getCredentialById(id);
        credentials.setPassportPath(credentialsRequestDto.getPassportPath());
        credentials.setKraCertPath(credentialsRequestDto.getKraCertPath());
        credentials.setIdFrontPath(credentialsRequestDto.getIdFrontPath());
        credentials.setIdBackPath(credentialsRequestDto.getIdBackPath());
        return Mapper.credentialToCredentialResponseDto(credentials);
    }

    @Override
    public CredentialsResponseDto deleteCredentials(Long id) {
        Credentials credentials = getCredentialById(id);
        credentialsRepository.delete(credentials);
        return Mapper.credentialToCredentialResponseDto(credentials);
    }

    @Override
    public CredentialsResponseDto getMemberCredentials(String memberId) {
        CredentialsResponseDto dto = credentialsRepository.getMemberCredentials(memberId);
        return dto;
    }
}
