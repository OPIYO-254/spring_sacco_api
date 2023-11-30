package com.sojrel.saccoapi.controller.WebAppController;

import com.sojrel.saccoapi.dto.requests.*;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.exceptions.UserNotFoundException;
import com.sojrel.saccoapi.model.*;
import com.sojrel.saccoapi.repository.CredentialsRepository;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import com.sojrel.saccoapi.repository.MemberRepository;
import com.sojrel.saccoapi.service.*;
import com.sojrel.saccoapi.utils.EmailUtility;
import com.sojrel.saccoapi.utils.FileUploader;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CredentialsService credentialsService;
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
    private StorageService storageService;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;
    DecimalFormat df = new DecimalFormat("0.00");

    @GetMapping("/home")
    public ModelAndView adminMain(){
        ModelAndView modelAndView = new ModelAndView("home");
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
        modelAndView.addObject("savings", memberTotalSavingDtos);
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
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error registering member.\"}");
        }

    }
    @GetMapping("/credentials")
    public ModelAndView credentials(){
        ModelAndView modelAndView = new ModelAndView();
        List<NewMemberResponseDto> memberDetails = memberService.findNewMembers();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("memberDetails", memberDetails);
        modelAndView.addObject("unread", unread);
        return modelAndView;
    }

    @GetMapping("/add-credentials")
    public ModelAndView addCredetials(@RequestParam String id){
        ModelAndView modelAndView = new ModelAndView("add-credentials");
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        modelAndView.addObject("member", memberResponseDto);
        return  modelAndView;
    }

    @GetMapping("/downloads/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {

        Resource resource = FileUploader.downloadFile(fileName);

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
        CredentialsResponseDto credsDto = credentialsService.getMemberCredentials(id);
        try{
            String idFrontName = credsDto.getIdFrontName();
            String idFrontPath = credsDto.getIdFrontPath();
            String idBackName = credsDto.getIdBackName();
            String idBackPath = credsDto.getIdBackPath();
            String kraCertName = credsDto.getKraCertName();
            String kraCertPath = credsDto.getKraCertPath();
            String passportName = credsDto.getPassportName();
            String passportPath = credsDto.getPassportPath();

            modelAndView.addObject("idFrontName", idFrontName);
            modelAndView.addObject("idFrontPath", idFrontPath);
            modelAndView.addObject("idBackName", idBackName);
            modelAndView.addObject("idBackPath", idBackPath);
            modelAndView.addObject("kraCertName", kraCertName);
            modelAndView.addObject("kraCertPath", kraCertPath);
            modelAndView.addObject("passportName", passportName);
            modelAndView.addObject("passportPath", passportPath);
        }
        catch (NullPointerException e){
            e.printStackTrace();
//            throw e;
        }
        Long totalSavings = savings.getTotal();
        ItemTotalDto shares = contributionService.getMemberTotalShares(id);
        Long totalShares = shares.getTotal();
        Long totalContribs = null;
        if(totalSavings == null && totalShares==null){totalContribs=null;}
        else if(totalSavings!=null && totalShares==null){totalContribs=totalSavings;}
        else if(totalSavings == null && totalShares!=null){totalContribs=totalShares;}
        else{totalContribs = totalShares + totalSavings;}
        modelAndView.addObject("member", memberResponseDto);
        modelAndView.addObject("savings", totalSavings);
        modelAndView.addObject("shares", totalShares);
        modelAndView.addObject("total", totalContribs);
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
        List<MemberLoansResponseDto> rejectedLoans = loanService.getApprovedLoans();
        ItemCountDto countUnread = contactService.countUnreadMessages();
        Long unread = countUnread.getCount();
        modelAndView.addObject("approvedLoans", rejectedLoans);
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
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        LocalDateTime startMonth = endDate.minusMonths(1);
        ModelAndView modelAndView = new ModelAndView("/revenue");
        List<LoanResponseDto> loanResponseDtos = loanService.getLoans();
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
        try {
            modelAndView.addObject("totalDisbursed", df.format(tDisbursed));
            modelAndView.addObject("totalAmount", df.format(expectedAmount));
            modelAndView.addObject("repaid", df.format(tRepaid));
            modelAndView.addObject("completed", df.format(collectedAmount));
            modelAndView.addObject("annualDisbursement", df.format(annualDisburse));
            modelAndView.addObject("monthlyDisburse", df.format(monthlyDisburse));
            modelAndView.addObject("totalMonthly", keyValueDtoList);
            modelAndView.addObject("totalPerCategiry", totalPerCategory);

        }
        catch (IllegalArgumentException e){
            new IllegalArgumentException("null values");
        }
        return modelAndView;
    }

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
        List<UploadedFilesDto> dtos = fileUploadService.getAllFiles();
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

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("memberId") String memberId,
                                              @RequestParam("idFrontPath") MultipartFile idFrontPath,
                                              @RequestParam("idBackPath") MultipartFile idBackPath,
                                              @RequestParam("kraCertPath") MultipartFile kraCertPath,
                                              @RequestParam("passportPath") MultipartFile passportPath) {

        Map<String, String> frontDetails = storageService.store(idFrontPath, memberId);
        Map<String, String> backDetails = storageService.store(idBackPath, memberId);
        Map<String, String> certDetails = storageService.store(kraCertPath, memberId);
        Map<String, String> passDetails = storageService.store(passportPath, memberId);

        Credentials credentials = new Credentials();
        credentials.setIdFrontName(frontDetails.get("fileName"));
        credentials.setIdFrontPath(frontDetails.get("url"));
        credentials.setIdBackName(backDetails.get("fileName"));
        credentials.setIdBackPath(backDetails.get("url"));
        credentials.setKraCertName(certDetails.get("fileName"));
        credentials.setKraCertPath(certDetails.get("url"));
        credentials.setPassportName(passDetails.get("fileName"));
        credentials.setPassportPath(passDetails.get("url"));
        Credentials savedCredential = credentialsRepository.save(credentials);
        Member member = memberService.getMemberById(memberId);
        member.setCredentials(savedCredential);
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/file-upload")
    public String fileUploads(@RequestHeader Map<String, String> headers){
        return "file-upload";}

    @PostMapping("/upload-file")
    public ResponseEntity<?> handleFileUpload(@RequestParam("fileDescription") String fileDescription, @RequestParam("file") MultipartFile file){
        Map<String, String> fileDetails = storageService.storeFile(file);
        FileUploads fileUploads = new FileUploads();
        fileUploads.setFileDescription(fileDescription);
        fileUploads.setFileName(fileDetails.get("fileName"));
        fileUploads.setFilePath(fileDetails.get("url"));
        fileUploadRepository.save(fileUploads);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/files/show/{fileName}")
    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request) {

        Resource resource = storageService.loadAsResource(fileName);

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
            userService.addUser(registrationRequestDto,siteURl);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Registered Successfully. \nPlease check your email to verify your account.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error creating user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
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
}
