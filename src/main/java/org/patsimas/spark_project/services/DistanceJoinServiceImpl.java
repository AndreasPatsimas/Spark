package org.patsimas.spark_project.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.orc.FileFormatException;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class DistanceJoinServiceImpl implements DistanceJoinService {

    @Override
    public Set<DistanceJoinDto> fetchDistanceJoins(MultipartFile fileOne, MultipartFile fileTwo, Long value) {

        log.info("Fetch distance joins for value: {} process begins", value);

        if (!fileOne.getOriginalFilename().endsWith("csv") || !fileTwo.getOriginalFilename().endsWith("csv"))
            throw new RuntimeException("Only file with csv format is allowed to import.");

        System.out.println(fileOne.getOriginalFilename());
        System.out.println(fileTwo.getOriginalFilename());
        System.out.println(value);

        log.info("Fetch distance joins for value: {} process end", value);

        return new HashSet<>();
    }
}
