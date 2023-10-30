package com.sojrel.saccoapi.service;

import com.sojrel.saccoapi.dto.responses.CredentialsResponseDto;
import com.sojrel.saccoapi.dto.responses.Mapper;
import com.sojrel.saccoapi.dto.responses.UploadedFilesDto;
import com.sojrel.saccoapi.model.Credentials;
import com.sojrel.saccoapi.model.FileUploads;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public interface FileUploadService {
    public List<UploadedFilesDto> getAllFiles();
}
