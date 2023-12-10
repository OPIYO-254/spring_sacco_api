package com.sojrel.saccoapi.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilesResponseDto {
    private Long id;
    private String memberId;
    private String fileName;
    private String fileType;
    private String fileUrl;
}
