package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.FileUploadResponseDto;
import com.sojrel.saccoapi.model.FileUploads;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FileUploadService {
    public List<FileUploadResponseDto> getAllFiles();

    public FileUploads getFileByName(String fileName);

    public FileUploadResponseDto uploadFile(String description, MultipartFile file);

}
