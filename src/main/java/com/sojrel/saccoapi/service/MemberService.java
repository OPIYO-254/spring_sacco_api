package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.ItemCountDto;
import com.sojrel.saccoapi.dto.responses.MemberResponseDto;
import com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto;
import com.sojrel.saccoapi.dto.responses.NewMemberResponseDto;
import com.sojrel.saccoapi.model.Member;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
@Service
public interface MemberService {
    public MemberResponseDto addMember(MemberRequestDto memberRequestDto);
    public MemberResponseDto getMember(String memberId);
    public Member getMemberById(String memberId);
    public List<MemberResponseDto> getAllMembers();
    public MemberResponseDto editMember(String memberId, MemberRequestDto memberRequestDto);
    public void deactivateMember(String memberId);
    public MemberResponseDto deleteMember(String memberId);
    public MemberResponseDto addContributionToMember(String memberId, Long contributionId);
    public MemberResponseDto removeContributionFromMember(String memberId, Long contributionId);
    public MemberResponseDto addLoanTakenToMember(String memberId, Long loanId);
    public MemberResponseDto removeLoanTakenFromMember(String memberId, Long loanId);
    public MemberResponseDto addLoanGuaranteedToMember(String memberId, Long loanId);
    public MemberResponseDto removeLoanGuaranteedFromMember(String memberId, Long loanId);
    public List<Member> listMembers();
    List<MemberTotalSavingsDto> findMemberSavings();
    List<NewMemberResponseDto> findNewMembers();
//    public void saveMember(MemberRequestDto member);
    public List<MemberTotalSavingsDto> membersTotalShares();
    public ItemCountDto getMemberCount();

    Member getMemberByEmail(String email);
//    public MemberResponseDto updateCredentials(String memberId, Long credentialsId);
}
