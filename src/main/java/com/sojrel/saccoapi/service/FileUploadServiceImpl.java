package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.UploadedFilesDto;
import com.sojrel.saccoapi.model.FileUploads;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    @Autowired
    private FileUploadRepository fileUploadsRepository;
    @Override
    public List<UploadedFilesDto> getAllFiles() {
        List<FileUploads> files = StreamSupport.stream(fileUploadsRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return Mapper.filesToUPloadedFilesDtos(files);
    }
}
