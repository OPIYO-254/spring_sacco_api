package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.MemberResponseDto;
import com.sojrel.saccoapi.dto.responses.MemberTotalContributionsDto;
import com.sojrel.saccoapi.service.ContributionService;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
@CrossOrigin(origins={"http://10.0.2.2:8080"})
@RestController
@RequestMapping("api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ContributionService contributionService;
    @PostMapping("/add")
    public ResponseEntity<MemberResponseDto> addMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.addMember(memberRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }


    @GetMapping("/get-one/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable String id) {
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<MemberTotalContributionsDto> memberDetails(@PathVariable String id){
        MemberTotalContributionsDto dto = new MemberTotalContributionsDto();
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        dto.setMember(memberResponseDto);
        ItemTotalDto savings = contributionService.getMemberTotalSavings(id);
        try{
//
            Long totalSavings = savings.getTotal();
            ItemTotalDto shares = contributionService.getMemberTotalShares(id);
            Long totalShares = shares.getTotal();
            Long totalContribs = null;
            if(totalSavings == null && totalShares==null){
                totalContribs=null;
            }
            else if(totalSavings!=null && totalShares==null){
                totalContribs=totalSavings;
            }
            else if(totalSavings == null && totalShares!=null){
                totalContribs=totalShares;
            }
            else{
                totalContribs=totalSavings + totalShares;
            }
            dto.setTotalSavings(totalSavings);
            dto.setTotalShares(totalShares);
            dto.setTotalContributions(totalContribs);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        return new ResponseEntity<>(memberResponseDtos, HttpStatus.OK);
    }



    @PostMapping("/edit/{id}")
    public ResponseEntity<MemberResponseDto> editMember(@PathVariable String id, @RequestBody MemberRequestDto memberRequestDto) throws ParseException {
        MemberResponseDto memberResponseDto = memberService.editMember(id, memberRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<MemberResponseDto> deleteMember(@RequestParam String id) {
        MemberResponseDto memberResponseDto = memberService.deleteMember(id);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-contribution/{memberId}/{contributionId}")
    public ResponseEntity<MemberResponseDto> addContribution(@PathVariable String memberId, @PathVariable Long contributionId) {
        MemberResponseDto memberResponseDto = memberService.addContributionToMember(memberId, contributionId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove-contribution/{memberId}/{contributionId}")
    public ResponseEntity<MemberResponseDto> removeContribution(@PathVariable String memberId, @PathVariable Long contributionId) {
        MemberResponseDto memberResponseDto = memberService.removeContributionFromMember(memberId, contributionId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/add-loanTaken/{memberId}/{loanId}")
    public ResponseEntity<MemberResponseDto> addLoanTaken(@PathVariable String memberId, @PathVariable Long loanId) {
        MemberResponseDto memberResponseDto = memberService.addLoanTakenToMember(memberId, loanId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove-loanTaken/{memberId}/{loanId}")
    public ResponseEntity<MemberResponseDto> removeLoanTaken(@PathVariable String memberId, @PathVariable Long loanId) {
        MemberResponseDto memberResponseDto = memberService.removeLoanTakenFromMember(memberId, loanId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/add-loanGuaranteed/{memberId}/{loanId}")
    public ResponseEntity<MemberResponseDto> addLoanGuaranteed(@PathVariable String memberId, @PathVariable Long loanId) {
        MemberResponseDto memberResponseDto = memberService.addLoanGuaranteedToMember(memberId, loanId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/remove-loanGuaranteed/{memberId}/{loanId}")
    public ResponseEntity<MemberResponseDto> removeLoanGuaranteed(@PathVariable String memberId, @PathVariable Long loanId) {
        MemberResponseDto memberResponseDto = memberService.removeLoanGuaranteedFromMember(memberId, loanId);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

}