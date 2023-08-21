package com.sojrel.saccoapi.controller.WebAppController;

import com.sojrel.saccoapi.dto.requests.MemberRequestDto;
import com.sojrel.saccoapi.dto.responses.MemberResponseDto;
import com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.service.ContributionService;
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
    MemberService memberService;
    @Autowired
    ContributionService contributionService;
    @GetMapping("/")
    public ModelAndView membersSavings(){
        ModelAndView modelAndView = new ModelAndView("index");
//        List<Member> members = memberService.listMembers();
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
        return "redirect:/";
    }

    @GetMapping("/members")
    public ModelAndView listMembers(){
        ModelAndView modelAndView = new ModelAndView("members");
        List<MemberResponseDto> memberResponseDtos = memberService.getAllMembers();
        modelAndView.addObject("members", memberResponseDtos);
        return  modelAndView;
    }
}
