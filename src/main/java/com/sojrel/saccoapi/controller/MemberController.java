package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.MemberResponseDto;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<MemberResponseDto> addMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.addMember(memberRequestDto);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }
    @PostMapping("/save-member")
    public ResponseEntity<?> saveMember(@RequestBody MemberRequestDto memberRequestDto){
        try {
            memberService.saveMember(memberRequestDto);
            // Return a JSON response with success message
            return ResponseEntity.ok("{\"status\": \"success\", \"message\": \"Member added successfully.\"}");
        } catch (Exception e) {
            // Return a JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\": \"error\", \"message\": \"Error adding member.\"}");
        }

    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable String id) {
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.FOUND);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        return new ResponseEntity<>(memberResponseDtos, HttpStatus.FOUND);
    }



    @PostMapping("/edit/{id}")
    public ResponseEntity<MemberResponseDto> editMember(@PathVariable String id, @RequestBody MemberRequestDto memberRequestDto) {
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