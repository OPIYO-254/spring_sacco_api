package com.sojrel.saccoapi.controller;

import com.sojrel.saccoapi.dto.responses.ContactResponseDto;
import com.sojrel.saccoapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins={"http://10.0.2.2:8080"})
@RestController
@RequestMapping("api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @GetMapping("/get-messages")
    public List<ContactResponseDto> getMessages(){
//        ModelAndView modelAndView = new ModelAndView("messages");
        List<ContactResponseDto> contactResponseDtos = contactService.getUnreadMessages();
//        modelAndView.addObject("messages", contactResponseDtos);
        return contactResponseDtos;
    }
}
