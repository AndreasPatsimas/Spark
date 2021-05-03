package org.patsimas.spark_project.controllers;

import org.patsimas.spark_project.domain.UploadFileResponse;
import org.patsimas.spark_project.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/file-storage")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        return fileStorageService.uploadFile(file);
    }

    @GetMapping
    public List<String> getFileNames() {

        return fileStorageService.fetchFileNames();
    }
}
