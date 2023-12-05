package com.sojrel.saccoapi.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

@Service
public interface StorageService {

    void init();

    Map<String, String> store(MultipartFile file, String id);

    public Map<String, String> storeFile(MultipartFile file);
    Stream<Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename);
    Resource loadAsPublicResource(String filename);
    void deleteAll();

}