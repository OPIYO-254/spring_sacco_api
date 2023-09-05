package com.sojrel.saccoapi.controller.WebAppController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.service.ContributionService;
import com.sojrel.saccoapi.service.LoanService;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Long count = loanCount.getCount();
        ItemCountDto memberCount = memberService.getMemberCount();
        Long mCount = memberCount.getCount();
        ItemCountDto approvedCount = loanService.countApprovedLoans();
        Long approved = approvedCount.getCount();
        ItemCountDto rejectedCount = loanService.countRejectedLoans();
        Long rejected = rejectedCount.getCount();
        ItemCountDto completedCount = loanService.countCompletedLoans();
        Long completed = completedCount.getCount();
        modelAndView.addObject("loan_count", count);
        modelAndView.addObject("member_count", mCount);
        modelAndView.addObject("approved_count", approved);
        modelAndView.addObject("rejected_count", rejected);
        modelAndView.addObject("completed_count", completed);
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

    @GetMapping("/add-contributions")
    public String contributionForm(){
        return "add-contributions";
    }



    @GetMapping("/members")
    public ModelAndView listMembers(){
        ModelAndView modelAndView = new ModelAndView("members");
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        modelAndView.addObject("members", memberResponseDtos);
        return  modelAndView;
    }
    @GetMapping("/member-details")
    public ModelAndView memberDetails(@RequestParam String id){
        ModelAndView modelAndView = new ModelAndView("member-details");
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        modelAndView.addObject("member", memberResponseDto);
        return modelAndView;
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

//    @PostMapping("/save-contribution")
//    public String saveContribution(@ModelAttribute ContributionRequestDto contributionRequestDto){
//        contributionService.addContribution(contributionRequestDto);
//        return "redirect:/members-contributions";
//    }


    @PostMapping("/save-contributions")
    public ResponseEntity<?> addContribution(@RequestBody ContributionRequestDto contributionRequestDto){
        try {
            contributionService.addContribution(contributionRequestDto);
            // Return a JSON response with success message
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Contribution added successfully.\"}");
        } catch (Exception e) {
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error adding contribution.\"}");
        }
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
    @GetMapping("/rejected-loans")
    public ModelAndView listRejectedLoans(){
        ModelAndView modelAndView = new ModelAndView("rejected-loans");
        List<MemberLoansResponseDto> rejectedLoans = loanService.getRejectedLoans();
        modelAndView.addObject("rejectedLoans", rejectedLoans);
        return modelAndView;
    }
    @GetMapping("/approved-loans")
    public ModelAndView listApprovedLoans(){
        ModelAndView modelAndView = new ModelAndView("approved-loans");
        List<MemberLoansResponseDto> rejectedLoans = loanService.getApprovedLoans();
//        System.out.println(rejectedLoans);
        modelAndView.addObject("approvedLoans", rejectedLoans);
        return modelAndView;
    }

    @GetMapping("/loan-details")
    public ModelAndView loanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("loan", loanResponseDto);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        return modelAndView;
    }
    @GetMapping("/approved-loan-details")
    public ModelAndView approvedLoanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("approved-loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("loan", loanResponseDto);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @PostMapping("/approve-loan/{id}")
    public String approveLoan(@PathVariable Long id){
        loanService.approveLoan(id);
        return "redirect:/applied-loans";
    }

    @PostMapping("/reject-loan/{id}")
    public String rejectLoan(@PathVariable Long id){
        loanService.rejectLoan(id);
        return "redirect:/applied-loans";
    }
    @GetMapping("/rejected-loan-details")
    public ModelAndView rejectedLoanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("rejected-loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("loan", loanResponseDto);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        return modelAndView;
    }

}
