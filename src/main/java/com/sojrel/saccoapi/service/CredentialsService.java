package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.CredentialsRequestDto;
import com.sojrel.saccoapi.dto.responses.CredentialsResponseDto;
import com.sojrel.saccoapi.model.Credentials;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CredentialsService {
    public CredentialsResponseDto addCredentials(CredentialsResponseDto credentialsResponseDto);
    public Credentials getCredentialById(Long id);
    public List<CredentialsResponseDto> getAllCredentials();
    public CredentialsResponseDto editCredentials(Long id, CredentialsRequestDto credentialsRequestDto);
    public CredentialsResponseDto deleteCredentials(Long id);

    public CredentialsResponseDto getMemberCredentials(String memberId);
}
