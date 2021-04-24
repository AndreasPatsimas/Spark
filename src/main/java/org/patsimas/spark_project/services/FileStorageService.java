package org.patsimas.spark_project.services;

import org.patsimas.spark_project.domain.UploadFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface FileStorageService {

    UploadFileResponse uploadFile(MultipartFile file);
}
