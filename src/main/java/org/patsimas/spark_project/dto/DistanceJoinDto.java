package org.patsimas.spark_project.dto;

public class DistanceJoinDto {

    private CoordinateDto coordinatesOne;
    private CoordinateDto coordinatesTwo;

    public DistanceJoinDto() {
    }

    public DistanceJoinDto(CoordinateDto coordinatesOne, CoordinateDto coordinatesTwo) {
        this.coordinatesOne = coordinatesOne;
        this.coordinatesTwo = coordinatesTwo;
    }

    public CoordinateDto getCoordinatesOne() {
        return coordinatesOne;
    }

    public void setCoordinatesOne(CoordinateDto coordinatesOne) {
        this.coordinatesOne = coordinatesOne;
    }

    public CoordinateDto getCoordinatesTwo() {
        return coordinatesTwo;
    }

    public void setCoordinatesTwo(CoordinateDto coordinatesTwo) {
        this.coordinatesTwo = coordinatesTwo;
    }

    @Override
    public String toString() {
        return "DistanceJoinDto{" +
                "coordinatesOne=" + coordinatesOne +
                ", coordinatesTwo=" + coordinatesTwo +
                '}';
    }
}
