package com.sojrel.saccoapi.controller.WebAppController;

import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.service.ContributionService;
import com.sojrel.saccoapi.service.LoanService;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomepageController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ContributionService contributionService;
    @Autowired
    private LoanService loanService;
    @GetMapping("/dashboard")
    public ModelAndView membersSavings(){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        ItemCountDto loanCount = loanService.countAppliedLoans();
        int count = loanCount.getCount();
        ItemCountDto memberCount = memberService.getMemberCount();
        int mCount = memberCount.getCount();
        modelAndView.addObject("loan_count", count);
        modelAndView.addObject("member_count", mCount);
        List<MemberTotalSavingsDto> memberTotalSavingDtos = memberService.findMemberSavings();
        modelAndView.addObject("savings", memberTotalSavingDtos);
        return modelAndView;
    }
    @GetMapping("/register")
    public ModelAndView addMemberForm(){
        ModelAndView modelAndView = new ModelAndView("register");
        MemberRequestDto newMember = new MemberRequestDto();
        modelAndView.addObject("member", newMember);
        return  modelAndView;
    }

    @PostMapping("/save-member")
    public String saveMember(@ModelAttribute MemberRequestDto memberRequestDto){
        memberService.saveMember(memberRequestDto);
        return "redirect:/members";
    }

    @GetMapping("/members")
    public ModelAndView listMembers(){
        ModelAndView modelAndView = new ModelAndView("members");
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        modelAndView.addObject("members", memberResponseDtos);
        return  modelAndView;
    }
    @GetMapping("/add-contribution")
    public ModelAndView addContributionForm(){
        ModelAndView modelAndView = new ModelAndView("add-contribution");
        ContributionRequestDto contributionRequestDto = new ContributionRequestDto();
        modelAndView.addObject("contribution", contributionRequestDto);
        return modelAndView;
    }

    @GetMapping("members-contributions")
    public ModelAndView listContributions(){
        ModelAndView modelAndView = new ModelAndView("members-contributions");
        List<MemberContributionsResponseDto> memberContributionsResponseDtos = contributionService.getMemberContributions();
        modelAndView.addObject("contributions", memberContributionsResponseDtos);
        return modelAndView;
    }

    @PostMapping("/save-contribution")
    public String saveContribution(@ModelAttribute ContributionRequestDto contributionRequestDto){
        contributionService.addContribution(contributionRequestDto);
        return "redirect:/members-contributions";
    }

    @GetMapping("/apply-loan")
    public ModelAndView addLoanForm(){
        ModelAndView modelAndView = new ModelAndView("apply-loan");
        LoanRequestDto loanRequestDto = new LoanRequestDto();
        modelAndView.addObject("loan", loanRequestDto);
        return modelAndView;
    }

    @PostMapping("/save-loan")
    public String saveLoan(@ModelAttribute LoanRequestDto loanRequestDto){
        loanService.addLoan(loanRequestDto);
        return "redirect:/applied-loans";
    }

    @GetMapping("/applied-loans")
    public ModelAndView listAppliedLoans(){
        ModelAndView modelAndView = new ModelAndView("applied-loans");
        List<MemberLoansResponseDto> loanResponseDtos = loanService.getAppliedLoans();
        modelAndView.addObject("loans", loanResponseDtos);

        return modelAndView;
    }

}
