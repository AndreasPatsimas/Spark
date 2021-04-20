package org.patsimas.spark_project.services;

import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Service
public interface DistanceJoinService {

    Set<DistanceJoinDto> fetchDistanceJoins(MultipartFile fileOne, MultipartFile fileTwo, Long value);
}
