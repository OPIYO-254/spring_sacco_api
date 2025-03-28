package com.sojrel.saccoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileDescription;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String fileUrl;
    @Column(nullable = false)
    private String fileType;
    @JsonIgnore
    @Lob
    @Column(nullable = false, length = 10485760)
    private byte[] file;
}
