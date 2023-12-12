package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.UserFilesResponseDto;
import com.sojrel.saccoapi.model.UserFiles;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserFilesService {
    UserFilesResponseDto storeFile(String memberId, MultipartFile file);
//    public List<UserFiles> getMemberFileByDescription(String memberid, String fileDescription);
    public List<UserFiles> getUserFilesByMember(String memberId);

    public UserFiles getMemberFileByName(String memberId, String fileName);
    public List<UserFilesResponseDto> getUsersFiles();
}
