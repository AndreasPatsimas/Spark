package org.patsimas.spark_project.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.orc.FileFormatException;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class DistanceJoinServiceImpl implements DistanceJoinService {

    @Value("${file.upload-dir}")
    private String fileStorageLocation;

    @Override
    public Set<DistanceJoinDto> fetchDistanceJoins(String fileNameOne, String fileNameTwo, Long value) {

        log.info("Fetch distance joins for value: {} process begins", value);

        System.out.println(fileStorageLocation + fileNameOne);
        System.out.println(value);

        log.info("Fetch distance joins for value: {} process end", value);

        return new HashSet<>();
    }
}
