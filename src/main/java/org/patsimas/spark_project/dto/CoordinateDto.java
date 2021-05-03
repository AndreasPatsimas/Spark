package org.patsimas.spark_project.dto;

public class CoordinateDto {

    private Long id;
    private Double lon;
    private Double lat;

    public CoordinateDto() {
    }

    public CoordinateDto(Long id, Double lon, Double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "CoordinateDto{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
