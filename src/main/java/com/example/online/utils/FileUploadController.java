package com.example.online.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("catalog/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,@RequestParam("folder") String folder) throws IOException {

        UploadResult uploadResult = fileUploadService.uploadFile(file,folder);

        return ResponseEntity.ok(uploadResult);
    }
}
