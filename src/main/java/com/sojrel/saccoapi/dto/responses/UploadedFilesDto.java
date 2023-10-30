package com.sojrel.saccoapi.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFilesDto {
    private Long id;
    private String fileDescription;
    private String fileName;
    private String filePath;
}
