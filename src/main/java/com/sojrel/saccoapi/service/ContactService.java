package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.ContactRequestDto;
import com.sojrel.saccoapi.dto.responses.ContactResponseDto;
import com.sojrel.saccoapi.dto.responses.ItemCountDto;
import com.sojrel.saccoapi.model.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {
    public ContactResponseDto addContact(ContactRequestDto contactRequestDto);
    public List<ContactResponseDto> getUnreadMessages();
    public List<ContactResponseDto> getAllReadMessages();

    public Contact getContactById(Long id);

    public ContactResponseDto getContact(Long id);

    public ContactResponseDto updateToRead(Long id);

    public ItemCountDto countUnreadMessages();
}
