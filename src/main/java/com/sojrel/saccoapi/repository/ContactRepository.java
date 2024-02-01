package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.ContactResponseDto;
import com.sojrel.saccoapi.dto.responses.ItemCountDto;
import com.sojrel.saccoapi.model.Contact;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT new com.sojrel.saccoapi.model.Contact(c.id, c.contactDate, c.firstName, " +
            "c.lastName, c.email, c.phone, c.message, c.isRead) FROM Contact c WHERE c.isRead = :isRead ORDER BY c.contactDate DESC")
    List<Contact> getAllMessages(@Param("isRead") boolean isRead);

    List<Contact> findByIsRead(boolean isRead);
    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Contact c WHERE c.isRead = FALSE")
    ItemCountDto countUnreadMessages();

    @Transactional
    @Modifying
    @Query("UPDATE Contact c SET c.isRead = TRUE WHERE c.id = :id")
    int updateToRead(@Param("id") Long id);
}
