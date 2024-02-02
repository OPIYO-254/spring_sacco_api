package com.sojrel.saccoapi.controller.WebAppController;

import com.sojrel.saccoapi.dto.requests.*;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.exceptions.UserNotFoundException;
import com.sojrel.saccoapi.flashapi.dto.request.FlashRepaymentRequestDto;
import com.sojrel.saccoapi.flashapi.dto.response.*;
import com.sojrel.saccoapi.flashapi.model.FlashLoan;
import com.sojrel.saccoapi.flashapi.model.FlashRepayment;
import com.sojrel.saccoapi.flashapi.service.FlashLoanService;
import com.sojrel.saccoapi.flashapi.service.FlashRepaymentService;
import com.sojrel.saccoapi.model.*;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import com.sojrel.saccoapi.repository.MemberRepository;
import com.sojrel.saccoapi.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@CrossOrigin
@Controller
public class HomepageController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ContributionService contributionService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private RepaymentService repaymentService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserFilesService userFilesService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private FlashLoanService flashLoanService;

    @Autowired
    private RegistrationFeeService registrationFeeService;

    @Autowired
    private FlashRepaymentService flashRepaymentService;
    DecimalFormat df = new DecimalFormat("0.00");

    @GetMapping("/home")
    public ModelAndView adminMain(){
        ModelAndView modelAndView = new ModelAndView("home");
        List<MemberTotalSavingsDto> memberTotalSavingDtos = memberService.findMemberSavings();
        List<MemberTotalSavingsDto> activeMembers = new ArrayList<>();
        for(MemberTotalSavingsDto dto : memberTotalSavingDtos){
            if(dto.getIsActive()){
                activeMembers.add(dto);
            }
        }
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
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getEmail());

//        modelAndView.addObject("user", userDetails);
        modelAndView.addObject("loan_count", count);
        modelAndView.addObject("member_count", mCount);
        modelAndView.addObject("approved_count", approved);
        modelAndView.addObject("repaying", repaying);
        modelAndView.addObject("rejected_count", rejected);
        modelAndView.addObject("completed_count", completed);
        modelAndView.addObject("savings", activeMembers);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView addMemberForm(){
        ModelAndView modelAndView = new ModelAndView("register");
//        MemberRequestDto newMember = new MemberRequestDto();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
//        modelAndView.addObject("member", newMember);
        modelAndView.addObject("unread", unread);
        return  modelAndView;
    }

    @PostMapping("/register-member")
    public ResponseEntity<?> saveMember(@RequestBody MemberRequestDto memberRequestDto){
        try {
            memberService.addMember(memberRequestDto);
            // Return a JSON response with success message
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Member registered successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error registering member.\"}");
        }

    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editMember(@PathVariable String id, @RequestBody MemberRequestDto memberRequestDto) {
        try {
            memberService.editMember(id, memberRequestDto);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Member details updated successfully.\"}");
        }
        catch (Exception e){
//            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error updating member details.\"}");
        }
    }

    @PostMapping("/deactivate")
    public String deactivateMember(@RequestParam String id){
        memberService.deactivateMember(id);
        return "redirect:/members";
    }

    @GetMapping("/documents")
    public ModelAndView credentials(){
        ModelAndView modelAndView = new ModelAndView("documents");
        List<MemberResponseDto> memberDetails = memberService.findNewMembers();
//        System.out.println(memberDetails);
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("memberDetails", memberDetails);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @GetMapping("/add-documents")
    public ModelAndView addCredetials(@RequestParam String id){
        ModelAndView modelAndView = new ModelAndView("add-documents");
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        modelAndView.addObject("member", memberResponseDto);
        return  modelAndView;
    }

//    @GetMapping("/downloads/{fileName}")
//    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
//
//        Resource resource = FileUploader.downloadFile(fileName);
//
////        MediaType contentType = MediaType.APPLICATION_PDF;
//
//        String mimeType;
//
//        try {
//            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException e) {
//            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//        }
//        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(mimeType))
////                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
//                .body(resource);
//    }

    @GetMapping("/add-contributions")
    public String contributionForm(){
        return "add-contributions";
    }



    @GetMapping("/members")
    public ModelAndView listMembers(){
        ModelAndView modelAndView = new ModelAndView("members");
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("members", memberResponseDtos);
        modelAndView.addObject("unread", unread);
        return  modelAndView;
    }
    @GetMapping("/member-details")
    public ModelAndView memberDetails(@RequestParam String id){
        ModelAndView modelAndView = new ModelAndView("member-details");
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        ItemTotalDto savings = contributionService.getMemberTotalSavings(id);
        try {
            List<UserFiles> files = userFilesService.getUserFilesByMember(id);
            List<String > urls = new ArrayList<>();
            List<String> names = new ArrayList<>();
            for (UserFiles file : files) {
                urls.add(file.getFileUrl());
                names.add(file.getFileName());

            }
            modelAndView.addObject("idFrontName", names.get(0));
            modelAndView.addObject("idFrontPath", urls.get(0));
            modelAndView.addObject("idBackName", names.get(1));
            modelAndView.addObject("idBackPath", urls.get(1));
            modelAndView.addObject("kraCertName", names.get(2));
            modelAndView.addObject("kraCertPath", urls.get(2));
            modelAndView.addObject("passportName", names.get(3));
            modelAndView.addObject("passportPath", urls.get(3));
        }
        catch (NullPointerException e){
            log.info("No files found for this member");
//            throw e;
        }
        catch (IndexOutOfBoundsException ex){
            log.info("Files not found for this member");
        }
        Long totalSavings = savings.getTotal();
        ItemTotalDto shares = contributionService.getMemberTotalShares(id);
        Long totalShares = shares.getTotal();
        Long totalContribs;


        String totalGuaranteed = loanService.getGuaranteeBalance(id);

        if(totalSavings == null && totalShares==null){totalContribs=null;}
        else if(totalSavings!=null && totalShares==null){totalContribs=totalSavings;}
        else if(totalSavings == null && totalShares!=null){totalContribs=totalShares;}
        else{totalContribs = totalShares + totalSavings;}
        double availableGuarantee = 0.0;
        if(totalSavings==null){
            availableGuarantee = 0.0;
        }
        else{
            availableGuarantee = Double.valueOf(totalSavings) - Double.valueOf(loanService.getGuaranteeBalance(id));
        }
        List<RegistrationFeeResponseDto> registrationFeeRequestDtos = registrationFeeService.getMemberRegistrationFees(id);
        ItemTotalDto totalRegistrationFee = registrationFeeService.getTotalFee(id);
        Long totalFee = totalRegistrationFee.getTotal();
//        System.out.println(totalFee);
        modelAndView.addObject("registrationFees", registrationFeeRequestDtos);
        modelAndView.addObject("totalFee", totalFee);
        modelAndView.addObject("member", memberResponseDto);
        modelAndView.addObject("savings", totalSavings);
        modelAndView.addObject("shares", totalShares);
        modelAndView.addObject("guaranteed", totalGuaranteed);
        modelAndView.addObject("guaranteeLimit", String.format("%.2f",availableGuarantee));
        modelAndView.addObject("total", totalContribs);

        return modelAndView;
    }

    @PostMapping("/add-fee")
    public ResponseEntity<?> addFee(@RequestBody RegistrationFeeRequestDto registrationFeeRequestDto){
        try {
            registrationFeeService.addRegistrationFee(registrationFeeRequestDto);
            // Return a JSON response with success message
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Fee added successfully.\"}");
        } catch (Exception e) {
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error adding fee.\"}");
        }
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
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("loans", loanResponseDtos);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }
    @GetMapping("/rejected-loans")
    public ModelAndView listRejectedLoans(){
        ModelAndView modelAndView = new ModelAndView("rejected-loans");
        List<MemberLoansResponseDto> rejectedLoans = loanService.getRejectedLoans();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("rejectedLoans", rejectedLoans);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }
    @GetMapping("/approved-loans")
    public ModelAndView listApprovedLoans(){
        ModelAndView modelAndView = new ModelAndView("approved-loans");
        List<MemberLoansResponseDto> approvedLoans = loanService.getApprovedLoans();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("approvedLoans", approvedLoans);
        modelAndView.addObject("unread", unread);
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
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("repayingLoans", repayingLoans);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @PostMapping("/repay-loan")
    public ResponseEntity<?> addRepayment(@RequestBody RepaymentRequestDto repaymentRequestDto){
        try {
            RepaymentResponseDto repaymentResponseDto = repaymentService.addRepayment(repaymentRequestDto);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Loan repayment successful.\"}");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error adding repayment.\"}");
        }
    }

    @GetMapping("/repaying-loan-details")
    public ModelAndView repayingLoanDetails(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("repaying-loan-details");
        LoanResponseDto loanResponseDto = loanService.getLoan(id);
        TotalDoubleItem repaidTotal = loanService.getTotalRepaid(id);
        double totalRepaid = repaidTotal.getTotal();
        if(loanResponseDto.getAmount()==totalRepaid || loanResponseDto.getAmount() < totalRepaid){
            loanService.completeLoan(id);
        }
        modelAndView.addObject("loan", loanResponseDto);
        List<LoanGuarantorResponseDto> loanGuarantorResponseDtos = loanService.getLoanGuarantors(id);
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        modelAndView.addObject("guarantors", loanGuarantorResponseDtos);
        Long total = item.getTotal();
        modelAndView.addObject("total", total);
        List<RepaymentResponseDto> repayments = loanService.getLoanRepayments(id);
        modelAndView.addObject("repayments", repayments);
        modelAndView.addObject("repaid", totalRepaid);
        return modelAndView;

    }

    @PostMapping("/approve-loan/{id}")
    public ResponseEntity<String> approveLoan(@PathVariable Long id) {
        ItemTotalDto item = loanService.getTotalGuaranteed(id);
        Long totalGuaranteed = item.getTotal();
        Loan loan = loanService.getLoanById(id);
        if(loan.getLoanType().equals(Loan.LoanType.EMERGENCY)){
            loanService.approveLoan(id);
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Loan approved successfully.\"}");
        }
        else if(totalGuaranteed != null) {
            if (loan.getPrincipal() == totalGuaranteed || loan.getPrincipal() < totalGuaranteed) {
                loanService.approveLoan(id);
                return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Loan approved successfully.\"}");
            }
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
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("completedLoans", completedLoans);
        modelAndView.addObject("unread", unread);
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
        modelAndView.addObject("repayments", repayments);
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
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("total_contributions", totalContributions);
        modelAndView.addObject("contributions", memberContributionsResponseDtos);
        modelAndView.addObject("shares", total_shares);
        modelAndView.addObject("savings", total_savings);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @GetMapping("/revenue")
    public ModelAndView getRevenue(){
//        LocalDateTime endDate = LocalDateTime.now();
//        LocalDateTime startDate = endDate.minusYears(1);
//        LocalDateTime startMonth = endDate.minusMonths(1);
        ModelAndView modelAndView = new ModelAndView("revenue");
        List<LoanResponseDto> loanResponseDtos = loanService.getLoans();
        try {
            double tDisbursed = loanResponseDtos.stream()
                    .filter(loan -> "COMPLETED".equals(loan.getLoanStatus()) || "APPROVED".equals(loan.getLoanStatus()))
                    .mapToDouble(LoanResponseDto::getPrincipal)
                    .sum();
            double expectedAmount = loanResponseDtos.stream()
                    .filter(loan -> "COMPLETED".equals(loan.getLoanStatus()) || "APPROVED".equals(loan.getLoanStatus()))
                    .mapToDouble(LoanResponseDto::getAmount)
                    .sum();
            double collectedAmount = loanResponseDtos.stream()
                    .filter(loan -> "COMPLETED".equals(loan.getLoanStatus()))
                    .mapToDouble(LoanResponseDto::getAmount)
                    .sum();
            ItemTotalDto monthlyTotal = loanService.getMonthlyDisbursement();
            ItemTotalDto annualTotal = loanService.getAnnualDisbursement();
            Long monthlyDisburse = monthlyTotal.getTotal();
            Long annualDisburse = annualTotal.getTotal();
            List<KeyValueDto> keyValueDtoList = loanService.totalMonthlyDisbursements();
            List<RepaymentResponseDto> repaymentResponseDtos = repaymentService.getRepayments();
            double tRepaid = repaymentResponseDtos.stream().mapToDouble(RepaymentResponseDto::getAmount).sum();//sum of all repaid amount
            List<KeyValueDto> totalPerCategory = loanService.totalPerLoanCategory();
            ItemCountDto countUnread = contactService.countUnreadMessages();
            Long unread = countUnread.getCount();
            modelAndView.addObject("unread", unread);
            modelAndView.addObject("totalDisbursed", df.format(tDisbursed));
            modelAndView.addObject("totalAmount", df.format(expectedAmount));
            modelAndView.addObject("repaid", df.format(tRepaid));
            modelAndView.addObject("completed", df.format(collectedAmount));
//            modelAndView.addObject("annualDisbursement", df.format(annualDisburse));
            if(monthlyDisburse!=null){
                modelAndView.addObject("monthlyDisburse", df.format(monthlyDisburse));
            }
            if(!keyValueDtoList.isEmpty()){
                modelAndView.addObject("totalMonthly", keyValueDtoList);
//                System.out.println(keyValueDtoList);
            }
            if(!totalPerCategory.isEmpty()){
                modelAndView.addObject("totalPerCategory", totalPerCategory);
//                System.out.println(totalPerCategory);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/flash")
    public ModelAndView flashLoans(){
        ModelAndView modelAndView = new ModelAndView("flash-loans");
        List<FlashLoanResponseDto> dtoList = flashLoanService.getNewFlashLoans();
        List<FlashLoanResponseDto> approvedList = flashLoanService.getFlashLoanByStatus("APPROVED");
        List<FlashLoanResponseDto> paidList = flashLoanService.getFlashLoanByStatus("PAID");
        List<FlashLoanResponseDto> rejectedList = flashLoanService.getFlashLoanByStatus("REJECTED");
        List<FlashLoanResponseDto> writtenList = flashLoanService.getFlashLoanByStatus("WRITEOFF");
        int countApproved = approvedList.size();
        int paid = paidList.size();
        int rejected = rejectedList.size();
        int writtenOff = writtenList.size();
        modelAndView.addObject("flashLoans", dtoList);
        modelAndView.addObject("approvedCount", countApproved);
        modelAndView.addObject("paid", paid);
        modelAndView.addObject("rejected", rejected);
        modelAndView.addObject("writtenOff", writtenOff);
        return modelAndView;
    }

    @PostMapping("/approve-flash")
    public String approveFlashLoan(@RequestParam Long id){
        flashLoanService.approveFlashLoan(id);
        return "redirect:/flash";
    }

    @PostMapping("/reject-flash")
    public String rejectFlashLoan(@RequestParam Long id){
        flashLoanService.rejectFlashLoan(id);
        return "redirect:/flash";
    }

    @PostMapping("/flash-write-off")
    public String writeOffFlashLoan(@RequestParam Long id){
        flashLoanService.writeOffFlashLoan(id);
        return "redirect:/flash-approved";
    }
    @GetMapping("/flash-approved")
    public ModelAndView approvedFlashLoans() {
        ModelAndView modelAndView = new ModelAndView("flash-loan-approved");
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByStatus("APPROVED");
        modelAndView.addObject("flashLoans", dtoList);
        return  modelAndView;

    }

    @GetMapping("/flash-approved-details")
    public ModelAndView getApprovedFlashLoanDetails(@RequestParam("id") Long id){
        ModelAndView modelAndView = new ModelAndView("flash-approved-details");
        FlashLoanResponseDto dto = FlashLoanMapper.flashLoanToDto(flashLoanService.getFlashLoanById(id));
        List<FlashRepayment> repayments = dto.getRepayments();
        double totalRepaid = repayments.stream().mapToDouble(FlashRepayment::getAmount).sum();
        if(dto.getAmount()+dto.getLoanPenalty()-totalRepaid <= 0.0){
            flashLoanService.completeFlashLoan(dto.getId());
        }
        modelAndView.addObject("flashLoan", dto);
        modelAndView.addObject("totalRepaid", totalRepaid);
        return  modelAndView;
    }

    @PostMapping("/flash-repay")
    public String flashRepayment(FlashRepaymentRequestDto dto){
        flashRepaymentService.makeRepayment(dto);
        return "redirect:/flash-approved-details?id="+dto.getLoanId();
    }


    @GetMapping("/flash-rejected")
    public ModelAndView rejectedFlashLoans(){
        ModelAndView modelAndView = new ModelAndView("flash-loan-rejected");
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByStatus("REJECTED");
        modelAndView.addObject("flashLoans", dtoList);
        return  modelAndView;
    }
    @GetMapping("/flash-repaid")
    public ModelAndView repaidFlashLoans(){
        ModelAndView modelAndView = new ModelAndView("flash-loan-repaid");
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByStatus("PAID");
        modelAndView.addObject("flashLoans", dtoList);
        return  modelAndView;
    }
    @GetMapping("/flash-written-off")
    public ModelAndView writtenOffFlashLoans(){
        ModelAndView modelAndView = new ModelAndView("flash-loan-written-off");
        List<FlashLoanResponseDto> dtoList = flashLoanService.getFlashLoanByStatus("WRITEOFF");
        modelAndView.addObject("flashLoans", dtoList);
        return  modelAndView;
    }

    @GetMapping("/")
    public String index(){return "index";}
    @GetMapping("/about-us")
    public String aboutUs(){return "about-us";}

    @GetMapping("/membership")
    public String membership(){return "membership";}

    @GetMapping("/services")
    public String loanProducts(){return "services";}

    @GetMapping("/contact-us")
    public String contactUs(){return "contact-us";}

    @GetMapping("/blogs")
    public String blogs(){return "blogs";}

    @GetMapping("/downloads")
    public ModelAndView downloads(){
        ModelAndView modelAndView = new ModelAndView("downloads");
        List<FileUploadResponseDto> dtos = fileUploadService.getAllFiles();

        modelAndView.addObject("downloads", dtos);
        return modelAndView;
    }
    @PostMapping("/send-message")
    public ResponseEntity<?> addContact(@RequestBody ContactRequestDto contactRequestDto){
        try {
            contactService.addContact(contactRequestDto);
            // Return a JSON response with success message
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Message sent successfully.\"}");
        } catch (Exception e) {
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error sending message.\"}");
        }
    }

    @GetMapping("/messages")
    public ModelAndView getContactMessages(){
        ModelAndView modelAndView = new ModelAndView("messages");
        List<ContactResponseDto> contactResponseDtos = contactService.getUnreadMessages();
        List<ContactResponseDto> read = contactService.getAllReadMessages();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("messages", contactResponseDtos);
        modelAndView.addObject("read", read);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @GetMapping("/read_message")
    public ModelAndView readMessage(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("read_message");
        ContactResponseDto contactResponseDto = contactService.getContact(id);
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("message", contactResponseDto);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @PostMapping("/mark-read-message/{id}")
    public String markRead(@PathVariable Long id){
        contactService.updateToRead(id);
        return "redirect:/messages";
    }

//    @PostMapping("/filupload")
//    public ResponseEntity<?> handleFileUpload(@RequestParam("memberId") String memberId,
//                                              @RequestParam("idFrontPath") MultipartFile idFrontPath,
//                                              @RequestParam("idBackPath") MultipartFile idBackPath,
//                                              @RequestParam("kraCertPath") MultipartFile kraCertPath,
//                                              @RequestParam("passportPath") MultipartFile passportPath) {
//
//        Map<String, String> frontDetails = storageService.store(idFrontPath, memberId);
//        Map<String, String> backDetails = storageService.store(idBackPath, memberId);
//        Map<String, String> certDetails = storageService.store(kraCertPath, memberId);
//        Map<String, String> passDetails = storageService.store(passportPath, memberId);
//
//        Credentials credentials = new Credentials();
//        credentials.setIdFrontName(frontDetails.get("fileName"));
//        credentials.setIdFrontPath(frontDetails.get("url"));
//        credentials.setIdBackName(backDetails.get("fileName"));
//        credentials.setIdBackPath(backDetails.get("url"));
//        credentials.setKraCertName(certDetails.get("fileName"));
//        credentials.setKraCertPath(certDetails.get("url"));
//        credentials.setPassportName(passDetails.get("fileName"));
//        credentials.setPassportPath(passDetails.get("url"));
//        Credentials savedCredential = credentialsRepository.save(credentials);
//        Member member = memberService.getMemberById(memberId);
//        member.setCredentials(savedCredential);
//        memberRepository.save(member);
//        return new ResponseEntity<>(HttpStatus.OK);
//
//    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("memberId") String memberId,
                                        @RequestParam("idFrontPath") MultipartFile idFrontPath,
                                        @RequestParam("idBackPath") MultipartFile idBackPath,
                                        @RequestParam("kraCertPath") MultipartFile kraCertPath,
                                        @RequestParam("passportPath") MultipartFile passportPath) {
        List<MultipartFile> files = new ArrayList<>();
        files.add(idFrontPath);
        files.add(idBackPath);
        files.add(kraCertPath);
        files.add(passportPath);

        Iterator<MultipartFile> iterator = files.iterator();
        while (iterator.hasNext()) {
            MultipartFile item = iterator.next();
            userFilesService.storeFile(memberId, item);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{memberId}/{fileName:.+}")
    public ResponseEntity <ByteArrayResource> downloadFile(@PathVariable String fileName, @PathVariable String memberId, HttpServletRequest request) {
        // Load file as Resource
        UserFiles file = userFilesService.getMemberFileByName(memberId, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity <ByteArrayResource> showFile(@PathVariable String fileName,HttpServletRequest request) {
        // Load file as Resource
        FileUploads fileUploads = fileUploadService.getFileByName(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUploads.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUploads.getFileName() + "\"")
                .body(new ByteArrayResource(fileUploads.getFile()));
    }

    @GetMapping("/file-upload")
    public String fileUploads(@RequestHeader Map<String, String> headers){
        return "file-upload";}

    @PostMapping("/upload-file")
    public ResponseEntity<?> handleFileUpload(@RequestParam("fileDescription") String fileDescription, @RequestParam("file") MultipartFile file){
        FileUploadResponseDto fileUploadResponseDto = fileUploadService.uploadFile(fileDescription, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file/{fileName}")
    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = storageService.loadAsPublicResource(fileName);
//        MediaType contentType = MediaType.APPLICATION_PDF;
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

    @GetMapping("/create-account")
    public String createAccount(){return "create-account";}

    @PostMapping("/create-account")
    public ResponseEntity<?> signup(@RequestBody RegistrationRequestDto registrationRequestDto, HttpServletRequest request){
        String siteURl = getSiteURL(request);
        try {
            userService.addUser(registrationRequestDto, siteURl);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Registered Successfully. \nPlease check your email to verify your account.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error creating account");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
//        Member member = memberService.getMemberByEmail(registrationRequestDto.getEmail());
//        if(member!=null) {
//
//        }
//        else{
//            Map<String, String> response = new HashMap<>();
//            response.put("status", "error");
//            response.put("message", "You are not authorized to signup at the moment. Contact admin.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(response);
//        }
    }


    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        if (userService.verifyUser(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    @GetMapping("/login")
    public String loginForm(){return "login";}


    @GetMapping("/forgot-password")
    public String forgotPasswordForm() { return "forgot-password"; }


    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, Model model) throws UserNotFoundException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.setResetPasswordToken(email, token);
            String resetPasswordLink = getSiteURL(request) + "/reset-password?token=" + token;
            userService.sendResetEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        }catch (UserNotFoundException e){
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getUserByToken(token);
        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getUserByToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
//			return "message";
        } else {
            userService.resetPassword(user, password);
            model.addAttribute("message", "Password reset successful");
        }

        return "reset-password";
    }

    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/error")
    public String getError(){
        return "error";
    }
}
