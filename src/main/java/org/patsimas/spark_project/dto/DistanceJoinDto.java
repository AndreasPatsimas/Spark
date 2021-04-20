package org.patsimas.spark_project.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistanceJoinDto {

    private CoordinateDto coordinatesOne;
    private CoordinateDto coordinatesTwo;
}
