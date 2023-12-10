package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.UserFilesResponseDto;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.model.UserFiles;
import com.sojrel.saccoapi.repository.UserFilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserFilesServiceImpl implements UserFilesService{

    @Autowired
    private MemberService memberService;
    @Autowired
    private UserFilesRepository userFilesRepository;
    @Override
    public UserFilesResponseDto storeFile(String memberId, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileNotFoundException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(file.getSize()>10485760){
                log.info("File exceeds maximum allowed size of 10MB");
            }
            UserFiles userFiles = new UserFiles();
            userFiles.setFileName(fileName);
            userFiles.setMember(memberService.getMemberById(memberId));
            userFiles.setFileType(file.getContentType());
            userFiles.setFile(file.getBytes());
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/%s/".formatted(memberId))
                    .path(userFiles.getFileName())
                    .toUriString();
            userFiles.setFileUrl(fileDownloadUri);
            userFilesRepository.save(userFiles);
            return Mapper.userFileToUserFileResponseDto(userFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<UserFiles> getMemberFileByDescription(String memberId, String fileDescription) {
//        Member member = memberService.getMemberById(memberId);
//        return userFilesRepository.findByMemberAndFileDescription(member, fileDescription);
//    }

    @Override
    public List<UserFiles> getUserFilesByMember(String memberId) {
        Member member = memberService.getMemberById(memberId);
        return userFilesRepository.findByMember(member);
    }

    @Override
    public UserFiles getMemberFileByName(String memberId, String fileName) {
        Member member = memberService.getMemberById(memberId);
        UserFiles file = userFilesRepository.findByMemberAndFileName(member, fileName);
        return file;
    }


}
