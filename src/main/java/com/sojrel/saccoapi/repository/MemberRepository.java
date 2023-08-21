package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto;
import com.sojrel.saccoapi.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
//    @Query(value = "SELECT member.id, member.first_name, member.mid_name, member.id_no, member.email, member.phone, \n" +
//            "sum(contribution.amount) as total_contribution \n" +
//            "from member inner join contribution on member.id = contribution.member_id where contribution_type= :type group by member.id;", nativeQuery = true)
//    List<MemberTotalSavingsDto> findBySavings(String type);

    @Query("SELECT new com.sojrel.saccoapi.dto.responses.MemberTotalSavingsDto(m.id, m.firstName, m.midName,m.idNo, m.email, m.phone, SUM(c.amount)) FROM Contribution c JOIN c.member m WHERE c.contributionType = 'SAVINGS' GROUP BY m.id")
    List<MemberTotalSavingsDto> findMemberSavings();
}
