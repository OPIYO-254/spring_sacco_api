package com.sojrel.saccoapi.dto.responses;

import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.Repayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mapper {
    public static MemberResponseDto memberToMemberResponseDto(Member member){
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(member.getId());
        memberResponseDto.setFirstName(member.getFirstName());
        memberResponseDto.setMidName(member.getMidName());
        memberResponseDto.setLastName(member.getLastName());
        memberResponseDto.setRegDate(member.getRegDate());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setIdNo(member.getIdNo());
        memberResponseDto.setPhone(member.getPhone());
        memberResponseDto.setDob(member.getDob());
        memberResponseDto.setKraPin(member.getKraPin());
        memberResponseDto.setGender(String.valueOf(member.getGender()));
        memberResponseDto.setAddress(member.getAddress());
        memberResponseDto.setResidence(member.getResidence());
        memberResponseDto.setContributions(member.getContributions());
        memberResponseDto.setLoansTaken(member.getLoansTaken());
        memberResponseDto.setLoansGuaranteed(member.getLoansGuaranteed());
        return memberResponseDto;
    }

    public static List<MemberResponseDto> memberToMembersResponseDtos(List<Member> members){
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
        for(Member member : members){
            memberResponseDtos.add(memberToMemberResponseDto(member));
        }
        return memberResponseDtos;
    }

    public static ContributionResponseDto contributionToContributionResponseDto(Contribution contribution){
        ContributionResponseDto contributionResponseDto = new ContributionResponseDto();
        contributionResponseDto.setId(contribution.getId());
        contributionResponseDto.setContributionDate(contribution.getContributionDate());
        contributionResponseDto.setContributionType(contribution.getContributionType().name());
        contributionResponseDto.setMemberId(contribution.getMember().getId());
        contributionResponseDto.setAmount(contribution.getAmount());
        return contributionResponseDto;
    }

    public static List<ContributionResponseDto> contributionToContributionResponseDtos(List<Contribution> contributions){
        List<ContributionResponseDto> contributionResponseDtos = new ArrayList<>();
        for(Contribution contribution : contributions){
            contributionResponseDtos.add(contributionToContributionResponseDto(contribution));
        }
        return contributionResponseDtos;
    }
     public static LoanResponseDto loanToLoanResponseDto(Loan loan){
        LoanResponseDto loanResponseDto = new LoanResponseDto();
        loanResponseDto.setId(loan.getId());
        loanResponseDto.setLoanType(loan.getLoanType().name());
        loanResponseDto.setApplicationDate(loan.getApplicationDate());
        loanResponseDto.setMemberId(loan.getBorrower().getId());
        loanResponseDto.setPrincipal(loan.getPrincipal());
        loanResponseDto.setInstalments(loan.getInstalments());
        loanResponseDto.setInterest(loan.getInterest());
        loanResponseDto.setLoanStatus(loan.getLoanStatus().name());
        loanResponseDto.setAmount(loan.calculatedAmount());
        loanResponseDto.setGuarantors(loan.getGuarantors());
        loanResponseDto.setRepayments(loan.getRepayments());
        if(!Objects.nonNull(loanResponseDto)){
            return null;
        }
         return loanResponseDto;


    }

    public static List<LoanResponseDto> loanToLoanResponseDtos(List<Loan> loans){
        List<LoanResponseDto> loanResponseDtos = new ArrayList<>();
        for(Loan loan : loans){
            loanResponseDtos.add(loanToLoanResponseDto(loan));
        }
        return loanResponseDtos;
    }

    public static RepaymentResponseDto repaymentToRepaymentResponseDto(Repayment repayment){
        RepaymentResponseDto repaymentResponseDto = new RepaymentResponseDto();
        repaymentResponseDto.setId(repayment.getId());
        repaymentResponseDto.setRepaymentDate(repayment.getRepaymentDate());
        repaymentResponseDto.setLoanId(repayment.getLoan().getId());
        repaymentResponseDto.setAmount(repayment.getAmount());
        return repaymentResponseDto;
    }

    public static List<RepaymentResponseDto> repaymentToRepaymentResponseDtos(List<Repayment> repayments){
        List<RepaymentResponseDto> repaymentResponseDtos = new ArrayList<>();
        for(Repayment repayment : repayments){
            repaymentResponseDtos.add(repaymentToRepaymentResponseDto(repayment));
        }
        return repaymentResponseDtos;
    }
}
