package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.responses.ContributionResponseDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.repository.ContributionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class ContributionServiceImpl implements ContributionService{
    @Autowired
    private ContributionRepository contributionRepository;
    @Autowired
    private MemberService memberService;
    @Transactional
    @Override
    public ContributionResponseDto addContribution(ContributionRequestDto contributionRequestDto) {
        Contribution contribution = new Contribution();
        Member member = memberService.getMemberById(contributionRequestDto.getMemberId());
        if(Objects.nonNull(member)){
            contribution.setMember(member);
            contribution.setContributionType(Contribution.ContributionType.valueOf(contributionRequestDto.getContributionType()));
            contribution.setAmount(contributionRequestDto.getAmount());
        }
        else{System.out.print("Member is null");}
        contributionRepository.save(contribution);
        return Mapper.contributionToContributionResponseDto(contribution);
    }

    @Override
    public Contribution getContributionById(Long contributionId) {
        return contributionRepository.findById(contributionId).orElseThrow(()->
                new IllegalArgumentException("Contribution with id "+contributionId+" not found."));
    }

    @Override
    public ContributionResponseDto getContribution(Long contributionId) {
        Contribution contribution = getContributionById(contributionId);
        return Mapper.contributionToContributionResponseDto(contribution);
    }

    @Override
    public List<ContributionResponseDto> getContributions() {
        List<Contribution> contributions = StreamSupport.stream(contributionRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return Mapper.contributionToContributionResponseDtos(contributions);
    }
    @Transactional
    @Override
    public ContributionResponseDto editContribution(Long contributionId, ContributionRequestDto contributionRequestDto) {
        Contribution contribution = getContributionById(contributionId);
        contribution.setContributionType(Contribution.ContributionType.valueOf(contributionRequestDto.getContributionType()));
        contribution.setAmount(contributionRequestDto.getAmount());
        if (contributionRequestDto.getMemberId() != null) {
            contribution.setMember(memberService.getMemberById(contributionRequestDto.getMemberId()));
        }
        contributionRepository.save(contribution);
        return Mapper.contributionToContributionResponseDto(contribution);
    }

    @Override
    public ContributionResponseDto deleteContribution(Long contributionId) {
        Contribution contribution = getContributionById(contributionId);
        contributionRepository.delete(contribution);
        return Mapper.contributionToContributionResponseDto(contribution);
    }


//    @Transactional
//    @Override
//    public ContributionResponseDto addMemberToContribution(Long contributionId, String memberId) {
//        Contribution contribution = getContributionById(contributionId);
//        Member member = memberService.getMemberById(memberId);
//        if(Objects.nonNull(contribution.getMember())){
//            throw new IllegalArgumentException("Contribution already has a member");
//        }
//        contribution.setMember(member);
//        member.addContribution(contribution);
//        return Mapper.contributionToContributionResponseDto(contribution);
//    }
//    @Transactional
//    @Override
//    public ContributionResponseDto removeMemberFromContribution(Long contributionId, String memberId) {
//        Contribution contribution = getContributionById(contributionId);
//        Member member = memberService.getMemberById(memberId);
//        if(!Objects.nonNull(contribution.getMember())){
//            throw new IllegalArgumentException("Contribution has no member");
//        }
//        contribution.setMember(null);
//        member.addContribution(contribution);
//        return Mapper.contributionToContributionResponseDto(contribution);
//    }

}
