package com.weather.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    
    private Long id;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private Double pressure;
    private Double windSpeed;
    private String description;
    private String icon;
    private Integer cloudiness;
    private Double visibility;
    private Integer uvIndex;
    private String createdAt;
    private String updatedAt;
}
