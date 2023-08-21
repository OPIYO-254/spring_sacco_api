package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.dto.responses.MemberContributionsResponseDto;
import com.sojrel.saccoapi.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
//    @Query("SELECT new com.sojrel.saccoapi.dto.responses.MemberContributionsResponseDto(m.id, m.firstName, m.lastName, c.contributionDate, c.contributionType, c.amount) FROM Contribution c JOIN c.member m")
    @Query(value = "SELECT contribution.id, member.id, member.first_name, member.mid_name,contribution.contribution_date, contribution.contribution_type, contribution.amount\n" +
            "from contribution inner join member on contribution.member_id = member.id;", nativeQuery = true)
    public List<Object[]> findMemberContributions();

}

