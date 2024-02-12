package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Contribution;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ContributionService {
    public ContributionResponseDto addContribution(ContributionRequestDto contributionRequestDto);
    public Contribution getContributionById(Long contributionId);
    public ContributionResponseDto getContribution(Long contributionId);
    public List<ContributionResponseDto> getContributions();
    ContributionResponseDto editContribution(Long contributionId, ContributionRequestDto contributionRequestDto);
    public ContributionResponseDto deleteContribution(Long contributionId);
    public List<MemberContributionsResponseDto> getMemberContributions();
//    public ContributionResponseDto removeMemberFromContribution(Long contributionId, String memberId);
    public ItemTotalDto getTotalContributions();
    public ItemTotalDto getTotalShares();
    public ItemTotalDto getTotalSavings();
    public ItemTotalDto getMemberTotalShares(String memberId);
    public ItemTotalDto getMemberTotalSavings(String memberId);

    public List<MemberMonthlySavingsDto> getMemberMonthlySavings(String memberId);

}
