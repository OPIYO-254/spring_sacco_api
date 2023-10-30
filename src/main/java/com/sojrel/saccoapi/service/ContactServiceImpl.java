package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.requests.ContactRequestDto;
import com.sojrel.saccoapi.dto.responses.ContactResponseDto;
import com.sojrel.saccoapi.dto.responses.ItemCountDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.model.Contact;
import com.sojrel.saccoapi.model.Loan;
import com.sojrel.saccoapi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactRepository contactRepository;
    @Override
    public ContactResponseDto addContact(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        contact.setFirstName(contactRequestDto.getFirstName());
        contact.setLastName(contactRequestDto.getLastName());
        contact.setEmail(contactRequestDto.getEmail());
        contact.setPhone(contactRequestDto.getPhone());
        contact.setMessage(contactRequestDto.getMessage());
        contactRepository.save(contact);
        return Mapper.contactToContactResponseDto(contact);
    }

    @Override
    public List<ContactResponseDto> getAllContacts() {
        List<Contact> contacts = StreamSupport.stream(contactRepository.getUnreadMessages().spliterator(), false).collect(Collectors.toList());
        return Mapper.contactsToContactResponseDtoList(contacts);

    }

    @Override
    public Contact getContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Contact with id"+id+"not found"));
        return contact;
    }

    @Override
    public ContactResponseDto getContact(Long id) {
        Contact contact = getContactById(id);
        return Mapper.contactToContactResponseDto(contact);
    }

    @Override
    public ContactResponseDto updateToRead(Long id) {
        Contact contact = getContactById(id);
        if(Objects.nonNull(contact)){
            contactRepository.updateToRead(id);
        }
        return Mapper.contactToContactResponseDto(contact);

    }

    @Override
    public ItemCountDto countUnreadMessages() {
        ItemCountDto itemCountDto = contactRepository.countUnreadMessages();
        return itemCountDto;
    }
}
