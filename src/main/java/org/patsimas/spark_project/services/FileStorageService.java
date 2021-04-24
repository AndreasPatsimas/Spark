package org.patsimas.spark_project.services;

import org.patsimas.spark_project.domain.UploadFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public interface FileStorageService {

    UploadFileResponse uploadFile(MultipartFile file);

    List<String> fetchFileNames();
}
