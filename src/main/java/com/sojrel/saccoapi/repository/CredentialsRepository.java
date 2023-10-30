package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.CredentialsResponseDto;
import com.sojrel.saccoapi.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.CredentialsResponseDto(c.id, c.idFrontName, c.idFrontPath," +
            "c.idBackName, c.idBackPath, c.kraCertName, c.kraCertPath, c.passportName, c.passportPath) " +
            "FROM Credentials c JOIN c.member WHERE c.member.id=:memberId")
    public CredentialsResponseDto getMemberCredentials(@Param("memberId") String memberId);
}
