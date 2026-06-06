package com.weather.repository;

import com.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    
    Optional<Weather> findByCity(String city);
    
    @Query("SELECT w FROM Weather w WHERE LOWER(w.city) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY w.updatedAt DESC")
    List<Weather> searchByCity(@Param("keyword") String keyword);
    
    @Query(value = "SELECT * FROM weather ORDER BY updated_at DESC LIMIT 10", nativeQuery = true)
    List<Weather> findRecentWeathers();
    
    @Query("SELECT w FROM Weather w WHERE w.latitude = :latitude AND w.longitude = :longitude")
    Optional<Weather> findByCoordinates(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
