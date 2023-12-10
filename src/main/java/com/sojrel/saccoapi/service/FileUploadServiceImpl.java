package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.FileUploadResponseDto;
import com.sojrel.saccoapi.model.FileUploads;
import com.sojrel.saccoapi.model.UserFiles;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService{

    @Autowired
    private FileUploadRepository fileUploadsRepository;
    @Override
    public List<FileUploadResponseDto> getAllFiles() {
        List<FileUploads> files = StreamSupport.stream(fileUploadsRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
        return Mapper.filesToUploadedFilesDtos(files);
    }

    @Override
    public FileUploads getFileByName(String fileName) {
        FileUploads fileUploads = fileUploadsRepository.findByFileName(fileName);
        return fileUploads;
    }

    @Override
    public FileUploadResponseDto uploadFile(String description, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileNotFoundException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(file.getSize()>10485760){
                log.info("File exceeds maximum allowed size of 10MB");
            }
            FileUploads fileUploads = new FileUploads();
            fileUploads.setFileDescription(description);
            fileUploads.setFileName(fileName);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(fileUploads.getFileName())
                    .toUriString();
            fileUploads.setFileUrl(fileDownloadUri);
            fileUploads.setFileType(file.getContentType());
            fileUploads.setFile(file.getBytes());
            fileUploadsRepository.save(fileUploads);
            return Mapper.fileToUploadedFileDto(fileUploads);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
