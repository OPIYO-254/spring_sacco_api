package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.UserFiles;
import com.sojrel.saccoapi.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ContributionService contributionService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private UserFilesService userFilesService;
    @Autowired
    private MemberService memberService;
    @Override
    public MemberResponseDto addMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setFirstName(memberRequestDto.getFirstName());
        member.setMidName(memberRequestDto.getMidName());
        member.setLastName(memberRequestDto.getLastName());
        member.setEmail(memberRequestDto.getEmail());
        member.setPhone(memberRequestDto.getPhone());
        member.setAlternativePhone(memberRequestDto.getAlternativePhone());
        member.setDob(memberRequestDto.getDob());
        member.setIdNo(memberRequestDto.getIdNo());
        member.setKraPin(memberRequestDto.getKraPin());
        member.setGender(Member.Gender.valueOf(memberRequestDto.getGender()));
        member.setAddress(memberRequestDto.getAddress());
        member.setResidence(memberRequestDto.getResidence());
        memberRepository.save(member);
        return Mapper.memberToMemberResponseDto(member);
    }

    @Override
    public MemberResponseDto getMember(String memberId) {
        return Mapper.memberToMemberResponseDto(getMemberById(memberId));
    }

    @Override
    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(()->
                new IllegalArgumentException("Member with id "+memberId+" not found"));
    }
    @Override
    public List<MemberResponseDto> getAllMembers() {
        List<Member> members = StreamSupport.stream(memberRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<MemberResponseDto> activeMembersDto = new ArrayList<>();
        for(Member member : members){
            if(member.getIsActive()){
                activeMembersDto.add(Mapper.memberToMemberResponseDto(member));
            }
        }
        return activeMembersDto;
    }
    @Transactional
    @Override
    public MemberResponseDto editMember(String memberId, MemberRequestDto memberRequestDto) {
        Member member = getMemberById(memberId);
        member.setFirstName(memberRequestDto.getFirstName());
        member.setMidName(memberRequestDto.getMidName());
        member.setLastName(memberRequestDto.getLastName());
        member.setEmail(memberRequestDto.getEmail());
        member.setPhone(memberRequestDto.getPhone());
        member.setAlternativePhone(memberRequestDto.getAlternativePhone());
        member.setDob(memberRequestDto.getDob());
        member.setIdNo(memberRequestDto.getIdNo());
        member.setKraPin(memberRequestDto.getKraPin());
        member.setGender(Member.Gender.valueOf(memberRequestDto.getGender()));
        member.setAddress(memberRequestDto.getAddress());
        member.setResidence(memberRequestDto.getResidence());
        memberRepository.save(member);
        return Mapper.memberToMemberResponseDto(member);
    }

    @Override
    public void deactivateMember(String memberId) {
        Member member = getMemberById(memberId);
        member.setIsActive(false);
        memberRepository.save(member);
    }

    @Override
    public MemberResponseDto deleteMember(String memberId) {
        Member member = getMemberById(memberId);
        memberRepository.delete(member);
        return Mapper.memberToMemberResponseDto(member);
    }
    @Transactional
    @Override
    public MemberResponseDto addContributionToMember(String memberId, Long contributionId) {
        Member member = getMemberById(memberId);
        Contribution contribution = contributionService.getContributionById(contributionId);
        if(member.getContributions().contains(contribution)){
            throw new IllegalArgumentException("Member already has this contribution");
        }
        member.addContribution(contribution);
        contribution.setMember(member);
        return Mapper.memberToMemberResponseDto(member);
    }
    @Transactional
    @Override
    public MemberResponseDto removeContributionFromMember(String memberId, Long contributionId) {

        Member member = getMemberById(memberId);
        Contribution contribution = contributionService.getContributionById(contributionId);
        if(!member.getContributions().contains(contribution)){
            throw new IllegalArgumentException("Member does not have this contribution");
        }
        member.removeContribution(contribution);
        contribution.setMember(null);
        return Mapper.memberToMemberResponseDto(member);
    }
    @Transactional
    @Override
    public MemberResponseDto addLoanTakenToMember(String memberId, Long loanId) {
        Member member = getMemberById(memberId);
        Loan loan = loanService.getLoanById(loanId);
        if(member.getLoansTaken().contains(loan)){
            throw new IllegalArgumentException("Member already has this loan");
        }
        member.addLoanTaken(loan);
        loan.setBorrower(member);
        return Mapper.memberToMemberResponseDto(member);
    }
    @Transactional
    @Override
    public MemberResponseDto removeLoanTakenFromMember(String memberId, Long loanId) {
        Member member = getMemberById(memberId);
        Loan loan = loanService.getLoanById(loanId);
        if(!member.getLoansTaken().contains(loan)){
            throw new IllegalArgumentException("Member does not have this loan");
        }
        member.removeLoanTaken(loan);
        loan.setBorrower(null);
        return Mapper.memberToMemberResponseDto(member);
    }
    @Transactional
    @Override
    public MemberResponseDto addLoanGuaranteedToMember(String memberId, Long loanId) {
        Member member = getMemberById(memberId);
        Loan loan = loanService.getLoanById(loanId);
        if(member.getLoansTaken().contains(loan)){
            throw new IllegalArgumentException("Member already has this guarantee");
        }
        member.addLoanGuaranteed(loan);
        loan.addGuarantor(member);
        return Mapper.memberToMemberResponseDto(member);
    }

    @Transactional
    @Override
    public MemberResponseDto removeLoanGuaranteedFromMember(String memberId, Long loanId) {
        Member member = getMemberById(memberId);
        Loan loan = loanService.getLoanById(loanId);
        if(!member.getLoansTaken().contains(loan)){
            throw new IllegalArgumentException("Member does not have this guarantee");
        }
        member.removeLoanGuaranteed(loan);
        loan.removeGuarantor(member);
        return Mapper.memberToMemberResponseDto(member);
    }

    @Override
    public List<Member> listMembers() {
        List<Member> members = memberRepository.findAll();
        return members;

    }

    /**
     * The function fetches members and their savings
     * @return list of member details and their savings
     */
    @Override
    public List<MemberTotalSavingsDto> findMemberSavings() {
        return memberRepository.findMemberSavings(Contribution.ContributionType.SAVINGS);
    }

    /**
     * This function is aimed at fetching all new members whose documents have not been
     * uploaded in the server. If the member has documents, the record is not added in
     * the list.
     * @return a list of member details for newly registered members
     */
    @Override
    public List<MemberResponseDto> findNewMembers() {
        List<MemberResponseDto> members = getAllMembers();
        List<MemberResponseDto> newMembers = new ArrayList<>();
        for(MemberResponseDto dto: members){
            if(dto.getUserFiles().isEmpty() && dto.getIsActive()){
                newMembers.add(dto);
            }
        }
        return newMembers;
    }


//    @Override
//    public void saveMember(MemberRequestDto memberRequestDto) {
//        Member member = new Member();
//        member.setFirstName(memberRequestDto.getFirstName());
//        member.setMidName(memberRequestDto.getMidName());
//        member.setLastName(memberRequestDto.getLastName());
//        member.setEmail(memberRequestDto.getEmail());
//        member.setPhone(memberRequestDto.getPhone());
//        member.setDob(memberRequestDto.getDob());
//        member.setIdNo(memberRequestDto.getIdNo());
//        member.setKraPin(memberRequestDto.getKraPin());
//        member.setGender(Member.Gender.valueOf(memberRequestDto.getGender()));
//        member.setAddress(memberRequestDto.getAddress());
//        member.setResidence(memberRequestDto.getResidence());
//        memberRepository.save(member);
//    }

    @Override
    public List<MemberTotalSavingsDto> membersTotalShares() {
        return null;
    }

    /**
     * The function counts the number of registered members
     * @return ItemCountDto
     */
    @Override
    public ItemCountDto getMemberCount(){
        ItemCountDto itemCountDto = memberRepository.findMemberCount();
        return itemCountDto;
    }

    /**
     * the function takes member emails and returns the member details
     * @param email
     * @return Member
     */
    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

//    @Override
//    public MemberResponseDto updateCredentials(String memberId, Long credentialsId) {
//        MemberResponseDto memberResponseDto = memberRepository.updateCredentials(credentialsId, memberId);
//        return memberResponseDto;
//    }


}
