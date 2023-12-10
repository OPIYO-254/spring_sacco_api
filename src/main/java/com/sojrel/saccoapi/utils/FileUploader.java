package com.sojrel.saccoapi.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;


public class FileUploader {
    private static Path fileStoragePath;
    private static String fileStorageLocation;

    public FileUploader(@Value("${file.storage.location:temp}") String fileStorageLocation) {

        this.fileStorageLocation = fileStorageLocation;
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }



    public static String saveFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloads/")
                .path(fileName)
                .toUriString();
        return url;
    }
    public static Resource downloadFile(String fileName) {
        try {
            Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

            Resource resource;

            resource = new UrlResource(path.toUri());


            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("the file doesn't exist or not readable");
            }
        }
        catch (MalformedURLException e) {
                throw new RuntimeException("Issue in reading the file", e);
            }
    }
}
