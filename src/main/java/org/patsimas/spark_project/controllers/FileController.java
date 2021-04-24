package org.patsimas.spark_project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.patsimas.spark_project.domain.UploadFileResponse;
import org.patsimas.spark_project.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/file-storage")
@Slf4j
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        log.info("Upload file {}", file.getName());

        return fileStorageService.uploadFile(file);
    }

    @GetMapping
    public List<String> getFileNames() {

        log.info("Fetch file names");

        return fileStorageService.fetchFileNames();
    }
}
