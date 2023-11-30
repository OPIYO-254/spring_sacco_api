package com.sojrel.saccoapi.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sojrel.saccoapi.exceptions.StorageFileNotFoundException;
import com.sojrel.saccoapi.model.Credentials;
import com.sojrel.saccoapi.model.FileUploads;
import com.sojrel.saccoapi.model.Member;
import com.sojrel.saccoapi.repository.CredentialsRepository;
import com.sojrel.saccoapi.repository.FileUploadRepository;
import com.sojrel.saccoapi.repository.MemberRepository;
import com.sojrel.saccoapi.service.MemberService;
import com.sojrel.saccoapi.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


@RestController
@RequestMapping("api/files")
public class FileUploadController {
    @Autowired
    private final StorageService storageService;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("files/show/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {

        Resource resource = storageService.loadAsResource(fileName);

//        MediaType contentType = MediaType.APPLICATION_PDF;

        String mimeType;

        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("memberId") String memberId,
                                                                   @RequestParam("idFrontPath") MultipartFile idFrontPath,
                                                                   @RequestParam("idBackPath") MultipartFile idBackPath,
                                                                   @RequestParam("kraCertPath") MultipartFile kraCertPath,
                                                                   @RequestParam("passportPath") MultipartFile passportPath) {

        Map<String, String> frontDetails = storageService.store(idFrontPath, memberId);
        Map<String, String> backDetails = storageService.store(idBackPath, memberId);
        Map<String, String> certDetails = storageService.store(kraCertPath, memberId);
        Map<String, String> passDetails = storageService.store(passportPath, memberId);

        Credentials credentials = new Credentials();
        credentials.setIdFrontName(frontDetails.get("fileName"));
        credentials.setIdFrontPath(frontDetails.get("url"));
        credentials.setIdBackName(backDetails.get("fileName"));
        credentials.setIdBackPath(backDetails.get("url"));
        credentials.setKraCertName(certDetails.get("fileName"));
        credentials.setKraCertPath(certDetails.get("url"));
        credentials.setPassportName(passDetails.get("fileName"));
        credentials.setPassportPath(passDetails.get("url"));
        Credentials savedCredential = credentialsRepository.save(credentials);
        Member member = memberService.getMemberById(memberId);
        member.setCredentials(savedCredential);
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/upload-file")
    public ResponseEntity<?> handleFileUpload(@RequestParam("fileDescription") String fileDescription, @RequestParam("file") MultipartFile file){
        Map<String, String> fileDetails = storageService.storeFile(file);
        FileUploads fileUploads = new FileUploads();
        fileUploads.setFileDescription(fileDescription);
        fileUploads.setFileName(fileDetails.get("fileName"));
        fileUploads.setFilePath(fileDetails.get("url"));
        fileUploadRepository.save(fileUploads);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
