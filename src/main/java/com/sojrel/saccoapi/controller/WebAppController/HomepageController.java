package com.sojrel.saccoapi.controller.WebAppController;

import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.requests.LoanRequestDto;
import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.requests.RepaymentRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.service.ContributionService;
import com.sojrel.saccoapi.service.LoanService;
import com.sojrel.saccoapi.service.MemberService;
import com.sojrel.saccoapi.service.RepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomepageController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ContributionService contributionService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private RepaymentService repaymentService;
    @GetMapping("/dashboard")
    public ModelAndView membersSavings(){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        List<MemberTotalSavingsDto> memberTotalSavingDtos = memberService.findMemberSavings();
        ItemCountDto loanCount = loanService.countAppliedLoans();
        Long count = loanCount.getCount();
        ItemCountDto memberCount = memberService.getMemberCount();
        Long mCount = memberCount.getCount();
        ItemCountDto approvedCount = loanService.countApprovedLoans();
        Long approved = approvedCount.getCount();
        ItemCountDto repayingCount = loanService.countRepayingLoans();
        Long repaying = repayingCount.getCount();
        ItemCountDto rejectedCount = loanService.countRejectedLoans();
        Long rejected = rejectedCount.getCount();
        ItemCountDto completedCount = loanService.countCompletedLoans();
        Long completed = completedCount.getCount();

        modelAndView.addObject("loan_count", count);
        modelAndView.addObject("member_count", mCount);
        modelAndView.addObject("approved_count", approved);
        modelAndView.addObject("repaying", repaying);
        modelAndView.addObject("rejected_count", rejected);
        modelAndView.addObject("completed_count", completed);
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
        ItemTotalDto savings = contributionService.getMemberTotalSavings(id);
        Long totalSavings = savings.getTotal();
        System.out.print(totalSavings);
        ItemTotalDto shares = contributionService.getMemberTotalShares(id);
        Long totalShares = shares.getTotal();
        modelAndView.addObject("member", memberResponseDto);
        modelAndView.addObject("savings", totalSavings);
        modelAndView.addObject("shares", totalShares);
        return modelAndView;
    }

    @GetMapping("/add-contribution")
    public ModelAndView addContributionForm(){
        ModelAndView modelAndView = new ModelAndView("add-contribution");
        ContributionRequestDto contributionRequestDto = new ContributionRequestDto();
        modelAndView.addObject("contribution", contributionRequestDto);
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
    @GetMapping("/repaying-loans")
    public ModelAndView listRepayingLoans(){
        ModelAndView modelAndView = new ModelAndView("repaying-loans");
        List<MemberLoansResponseDto> repayingLoans = loanService.findRepayingLoans();
        modelAndView.addObject("repayingLoans", repayingLoans);
        return modelAndView;
    }
    @GetMapping("/repaying-loan-details")
    public ModelAndView repayingLoanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("repaying-loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        TotalDoubleItem repaidTotal = loanService.getTotalRepaid(id);
        double totalRepaid = repaidTotal.getTotal();
        if(loanResponseDto.getAmount()==totalRepaid || loanResponseDto.getAmount()<totalRepaid){
            loanService.completeLoan(id);
        }
        modelAndView.addObject("loan", loanResponseDto);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        List<RepaymentResponseDto> repayments = loanService.getLoanRepayments(id);
        modelAndView.addObject("repayments", repayments);;
        modelAndView.addObject("repaid", totalRepaid);
        return modelAndView;
    }

    @PostMapping("/approve-loan/{id}")
    public ResponseEntity<String> approveLoan(@PathVariable Long id) {
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        Long totalGuaranteed = item.getTotal();
        Loan loan = loanService.getLoanById(id);
        if (totalGuaranteed != null) {
            if (loan.getPrincipal() == totalGuaranteed || loan.getPrincipal() < totalGuaranteed) {
                loanService.approveLoan(id);
                return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Loan approved successfully.\"}");
            }
            return ResponseEntity.ok("{\"status\": \"error\", \"message\": \"Loan not fully guaranteed\"}");
        }
        return ResponseEntity.ok("{\"status\": \"error\", \"message\": \"Loan not fully guaranteed\"}");
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

    @GetMapping("/completed-loans")
    public ModelAndView listCompletedLoans(){
        ModelAndView modelAndView = new ModelAndView("completed-loans");
        List<MemberLoansResponseDto> completedLoans = loanService.getCompletedLoans();
        modelAndView.addObject("completedLoans", completedLoans);
        return modelAndView;
    }

    @GetMapping("/completed-loan-details")
    public ModelAndView completedLoanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("completed-loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        TotalDoubleItem repaidTotal = loanService.getTotalRepaid(id);
        double totalRepaid = repaidTotal.getTotal();
        if(loanResponseDto.getAmount()==totalRepaid || loanResponseDto.getAmount()<totalRepaid){
            loanService.completeLoan(id);
        }
        modelAndView.addObject("loan", loanResponseDto);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        List<RepaymentResponseDto> repayments = loanService.getLoanRepayments(id);
        modelAndView.addObject("repayments", repayments);;
        modelAndView.addObject("repaid", totalRepaid);
        return modelAndView;
    }
    @GetMapping("/contributions")
    public ModelAndView listContributions(){
        ModelAndView modelAndView = new ModelAndView("contributions");
        List<MemberContributionsResponseDto> memberContributionsResponseDtos = contributionService.getMemberContributions();
        ItemTotalDto itemTotalDto = contributionService.getTotalContributions();
        Long totalContributions = itemTotalDto.getTotal();
        ItemTotalDto shares = contributionService.getTotalShares();
        Long total_shares = shares.getTotal();
        ItemTotalDto savings = contributionService.getTotalSavings();
        Long total_savings = savings.getTotal();
        modelAndView.addObject("total_contributions", totalContributions);
        modelAndView.addObject("contributions", memberContributionsResponseDtos);
        modelAndView.addObject("shares", total_shares);
        modelAndView.addObject("savings", total_savings);
        return modelAndView;
    }

    @GetMapping("/revenue")
    public ModelAndView getRevenue(){
        ModelAndView modelAndView = new ModelAndView("/revenue");
        List<LoanResponseDto> loanResponseDtos = loanService.getLoans();
        double tDisbursed = loanResponseDtos.stream()
                .filter(loan -> "COMPLETE".equals(loan.getLoanStatus()) || "APPROVED".equals(loan.getLoanStatus()))
                .mapToDouble(LoanResponseDto::getPrincipal)
                .sum();
        double expectedAmount = loanResponseDtos.stream()
                .filter(loan -> "COMPLETE".equals(loan.getLoanStatus()) || "APPROVED".equals(loan.getLoanStatus()))
                .mapToDouble(LoanResponseDto::getAmount)
                .sum();
        List<RepaymentResponseDto> repaymentResponseDtos = repaymentService.getRepayments();
        double tRepaid = repaymentResponseDtos.stream().mapToDouble(RepaymentResponseDto::getAmount).sum();
        modelAndView.addObject("totalDisbursed", tDisbursed);
        modelAndView.addObject("totalAmount", expectedAmount);
        modelAndView.addObject("repaid", tRepaid);
        return modelAndView;
    }


}
