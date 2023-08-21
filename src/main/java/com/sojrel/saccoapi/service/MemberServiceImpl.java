package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.MemberResponseDto;
import com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto;
import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public MemberResponseDto addMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setFirstName(memberRequestDto.getFirstName());
        member.setMidName(memberRequestDto.getMidName());
        member.setLastName(memberRequestDto.getLastName());
        member.setEmail(memberRequestDto.getEmail());
        member.setPhone(memberRequestDto.getPhone());
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
        return Mapper.memberToMembersResponseDtos(members);
    }
    @Transactional
    @Override
    public MemberResponseDto editMember(String memberId, MemberRequestDto memberRequestDto) {
        Member member = getMemberById(memberId);
        member.setEmail(memberRequestDto.getEmail());
        member.setPhone(memberRequestDto.getPhone());
        member.setAddress(memberRequestDto.getAddress());
        member.setResidence(memberRequestDto.getResidence());
        member.setGender(Member.Gender.valueOf(memberRequestDto.getGender()));
//        if(!memberRequestDto.getContributionIds().isEmpty()){ //check if the list is not empty
//            List<Contribution> contributions = new ArrayList<>();
//            for(Long contributionId: memberRequestDto.getContributionIds()){
//                contributions.add(contributionService.getContributionById(contributionId));
//            }
//            member.setContributions(contributions);
//        }
//        if(!memberRequestDto.getLoansTakenIds().isEmpty()){
//            List<Loan> loansTaken = new ArrayList<>();
//            for(Long loanTakenId : memberRequestDto.getLoansTakenIds()){
//                loansTaken.add(loanService.getLoanById(loanTakenId));
//            }
//            member.setLoansTaken(loansTaken);
//        }
//        if(!memberRequestDto.getLoansGuaranteedIds().isEmpty()){
//            List<Loan> loansGuaranteed = new ArrayList<>();
//            for (Long loanTakenGuaranteed : memberRequestDto.getLoansGuaranteedIds()){
//                loansGuaranteed.add(loanService.getLoanById(loanTakenGuaranteed));
//            }
//            member.setLoansGuaranteed(loansGuaranteed);
//        }
        memberRepository.save(member);
        return Mapper.memberToMemberResponseDto(member);
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

    @Override
    public List<MemberTotalSavingsDto> findMemberSavings() {
        return memberRepository.findMemberSavings();
    }

    @Override
    public void saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setFirstName(memberRequestDto.getFirstName());
        member.setMidName(memberRequestDto.getMidName());
        member.setLastName(memberRequestDto.getLastName());
        member.setEmail(memberRequestDto.getEmail());
        member.setPhone(memberRequestDto.getPhone());
        member.setDob(memberRequestDto.getDob());
        member.setIdNo(memberRequestDto.getIdNo());
        member.setKraPin(memberRequestDto.getKraPin());
        member.setGender(Member.Gender.valueOf(memberRequestDto.getGender()));
        member.setAddress(memberRequestDto.getAddress());
        member.setResidence(memberRequestDto.getResidence());
        memberRepository.save(member);
    }


}
