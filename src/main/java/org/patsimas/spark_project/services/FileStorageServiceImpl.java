package org.patsimas.spark_project.services;

import lombok.extern.slf4j.Slf4j;
import org.patsimas.spark_project.domain.UploadFileResponse;
import org.patsimas.spark_project.exceptions.FileStorageException;
import org.patsimas.spark_project.utils.NumericUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String fileStorageLocation;

    @Override
    public UploadFileResponse uploadFile(MultipartFile file) {

        log.info("Upload file {} process begins", file.getOriginalFilename());

        validation(file);

        String fileName = storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        log.info("Upload file process completed");

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    private void validation(MultipartFile file){

        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith("csv"))
            throw new FileStorageException("Only file with csv format is allowed to import.");

        try {

            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            br.readLine();
            List<String> firstRowColumns = Stream.of(br.readLine().split(";", -1))
                    .collect(Collectors.toList());
            if (firstRowColumns.size() != 3 || !NumericUtils.isNumeric(firstRowColumns.get(1)) ||
                    !NumericUtils.isNumeric(firstRowColumns.get(2)))
                throw new Exception();
        }
        catch (Exception e){
            throw new FileStorageException("Csv File must have three columns. " +
                    "First column must me id, second point x and third point y.", e);
        }
    }

    private String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(fileStorageLocation + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
