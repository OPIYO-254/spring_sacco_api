package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.FileUploads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploads, Long> {
    FileUploads findByFileName(String fileName);
}
