package org.patsimas.spark_project.services;

import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public interface DistanceJoinService {

    Set<DistanceJoinDto> fetchDistanceJoins(String fileNameOne, String fileNameTwo, Long value);
}
