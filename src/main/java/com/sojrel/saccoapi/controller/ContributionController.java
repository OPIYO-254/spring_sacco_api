package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.requests.ContributionRequestDto;
import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.service.ContributionService;
import com.sojrel.saccoapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contribution")
public class ContributionController {
    @Autowired
    private ContributionService contributionService;
    @Autowired
    private MemberService memberService;
    @PostMapping("/add")
    public ResponseEntity<ContributionResponseDto> addContribution(@RequestBody ContributionRequestDto contributionRequestDto){
        ContributionResponseDto contributionResponseDto = contributionService.addContribution(contributionRequestDto);
        return new ResponseEntity<>(contributionResponseDto, HttpStatus.CREATED);
    }
    @GetMapping("/get-one/{id}")
    public ResponseEntity<ContributionResponseDto> getContribution(@PathVariable Long id){
        ContributionResponseDto contributionResponseDto = contributionService.getContribution(id);
        return new ResponseEntity<>(contributionResponseDto, HttpStatus.FOUND);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ContributionResponseDto>> getContributions(){
        List<ContributionResponseDto> contributionResponseDtos = contributionService.getContributions();
        return new ResponseEntity<>(contributionResponseDtos, HttpStatus.FOUND);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<ContributionResponseDto> editContribution(@PathVariable Long id, @RequestBody ContributionRequestDto contributionRequestDto){
        ContributionResponseDto contributionResponseDto = contributionService.editContribution(id, contributionRequestDto);
        return new ResponseEntity<>(contributionResponseDto, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<ContributionResponseDto> deleteContribution(@RequestParam Long id){
        ContributionResponseDto contributionResponseDto = contributionService.deleteContribution(id);
        return new ResponseEntity<>(contributionResponseDto, HttpStatus.NOT_FOUND);
    }

//    @PostMapping("/add-member/{contributionId}/{memberId}")
//    public ResponseEntity<ContributionResponseDto> addMember(@PathVariable Long contributionId, @PathVariable String memberId){
//        ContributionResponseDto contributionResponseDto = contributionService.addMemberToContribution(contributionId, memberId);
//        return new ResponseEntity<>(contributionResponseDto, HttpStatus.OK);
//    }
//
//    @PostMapping("/remove-member/{contributionId}/{memberId}")
//    public ResponseEntity<ContributionResponseDto> removeMember(@PathVariable Long contributionId, @PathVariable String memberId){
//        ContributionResponseDto contributionResponseDto = contributionService.removeMemberFromContribution(contributionId, memberId);
//        return new ResponseEntity<>(contributionResponseDto, HttpStatus.OK);
//    }
    @GetMapping("/total-savings")
    public ResponseEntity<List<MemberTotalSavingsDto>> membersTotalSavings(){
        List<MemberTotalSavingsDto> memberTotalSavingDtos = memberService.findMemberSavings();
        return new ResponseEntity<>(memberTotalSavingDtos, HttpStatus.OK);
    }




}
