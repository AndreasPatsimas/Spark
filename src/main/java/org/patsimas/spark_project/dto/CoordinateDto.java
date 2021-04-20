package org.patsimas.spark_project.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoordinateDto {

    private Long id;
    private Double lon;
    private Double lat;
}
