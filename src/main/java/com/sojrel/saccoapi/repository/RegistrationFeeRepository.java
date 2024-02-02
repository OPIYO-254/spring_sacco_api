package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.ItemTotalDto;
import com.sojrel.saccoapi.dto.responses.RegistrationFeeResponseDto;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.RegistrationFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationFeeRepository extends JpaRepository<RegistrationFee, Long> {

    List<RegistrationFee> findByMember(Member member);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.ItemTotalDto(SUM(rf.amount)) FROM RegistrationFee rf WHERE rf.member.id=:memberId")
    ItemTotalDto getTotalMemberFee(@Param("memberId") String memberId);

}
