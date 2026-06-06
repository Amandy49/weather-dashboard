package com.weather.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "weather")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String country;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    private Double temperature;
    
    @Column(nullable = false)
    private Double feelsLike;
    
    @Column(nullable = false)
    private Integer humidity;
    
    @Column(nullable = false)
    private Double pressure;
    
    @Column(nullable = false)
    private Double windSpeed;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String icon;
    
    @Column(nullable = false)
    private Integer cloudiness;
    
    @Column(nullable = false)
    private Double visibility;
    
    @Column(nullable = false)
    private Integer uvIndex;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
