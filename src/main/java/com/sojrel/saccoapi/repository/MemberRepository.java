package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.*;
import com.sojrel.saccoapi.model.Contribution;
import com.sojrel.saccoapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByEmail(String email);
    @Query("SELECT new com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto(m.id, m.firstName, m.midName,m.lastName,m.idNo, m.email, " +
            "m.phone, m.residence, SUM(c.amount), m.isActive) FROM Contribution c JOIN c.member m WHERE c.contributionType =:type AND m.isActive=true GROUP BY m.id")
    List<MemberTotalSavingsDto> findMemberSavings(@Param("type")Contribution.ContributionType type);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemCountDto(COUNT(*)) FROM Member m where m.isActive = true")
    ItemCountDto findMemberCount();



}

