package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.UserFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface UserFilesRepository extends JpaRepository<UserFiles, Long> {
    List<UserFiles> findByMember(Member member);
    UserFiles findByMemberAndFileName(Member member, String fileName);
}
