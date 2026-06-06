package com.weather.service;

import com.google.gson.JsonObject;
import com.weather.model.Weather;
import com.weather.model.dto.WeatherDTO;
import com.weather.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class WeatherService {
    
    private final WeatherRepository weatherRepository;
    private final WeatherApiService weatherApiService;
    
    public WeatherService(WeatherRepository weatherRepository, WeatherApiService weatherApiService) {
        this.weatherRepository = weatherRepository;
        this.weatherApiService = weatherApiService;
    }
    
    public WeatherDTO getOrUpdateWeatherByCity(String city) {
       JsonObject apiResponse = weatherApiService.getWeatherByCity(city);

       if (apiResponse == null) {
            log.error("API response is NULL for city: {}", city);
            return weatherRepository.findByCity(city)
                    .map(this::convertToDTO)
                    .orElse(null);
        }

        String cod = apiResponse.has("cod")
                ? apiResponse.get("cod").getAsString()
                : "unknown";

        if (!"200".equals(cod)) {
            log.warn("Weather API failed for city: {}, response: {}", city, apiResponse);

            return weatherRepository.findByCity(city)
                    .map(this::convertToDTO)
                    .orElse(null);
        }
          // ⭐⭐⭐ 关键：成功情况必须 return
    Weather weather = parseWeatherFromApi(apiResponse);
    weather = weatherRepository.save(weather);
    return convertToDTO(weather);
    }
    
    public WeatherDTO getWeatherByCoordinates(Double latitude, Double longitude) {
        JsonObject apiResponse = weatherApiService.getWeatherByCoordinates(latitude, longitude);
        
        if (apiResponse == null || apiResponse.has("cod") && apiResponse.get("cod").getAsInt() != 200) {
            log.warn("Failed to fetch weather from API for coordinates: {}, {}", latitude, longitude);
            return null;
        }
        
        Weather weather = parseWeatherFromApi(apiResponse);
        weather = weatherRepository.save(weather);
        return convertToDTO(weather);
    }
    
    public List<WeatherDTO> getRecentWeathers() {
        return weatherRepository.findRecentWeathers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<WeatherDTO> searchCity(String keyword) {
        return weatherRepository.searchByCity(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public JsonObject getForecast(String city) {
        return weatherApiService.getForecast(city);
    }
    
    private Weather parseWeatherFromApi(JsonObject json) {
        JsonObject main = json.getAsJsonObject("main");
        JsonObject wind = json.getAsJsonObject("wind");
        JsonObject clouds = json.getAsJsonObject("clouds");
        JsonObject sys = json.getAsJsonObject("sys");
        JsonObject coord = json.getAsJsonObject("coord");
        JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();
        
        return Weather.builder()
                .city(json.get("name").getAsString())
                .country(sys.get("country").getAsString())
                .latitude(coord.get("lat").getAsDouble())
                .longitude(coord.get("lon").getAsDouble())
                .temperature(main.get("temp").getAsDouble())
                .feelsLike(main.get("feels_like").getAsDouble())
                .humidity(main.get("humidity").getAsInt())
                .pressure(main.get("pressure").getAsDouble())
                .windSpeed(wind.get("speed").getAsDouble())
                .description(weather.get("description").getAsString())
                .icon(weather.get("icon").getAsString())
                .cloudiness(clouds.get("all").getAsInt())
                .visibility(json.has("visibility") ? json.get("visibility").getAsDouble() : 10000.0)
                .uvIndex(0)
                .build();
    }
    
    private WeatherDTO convertToDTO(Weather weather) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return WeatherDTO.builder()
                .id(weather.getId())
                .city(weather.getCity())
                .country(weather.getCountry())
                .latitude(weather.getLatitude())
                .longitude(weather.getLongitude())
                .temperature(weather.getTemperature())
                .feelsLike(weather.getFeelsLike())
                .humidity(weather.getHumidity())
                .pressure(weather.getPressure())
                .windSpeed(weather.getWindSpeed())
                .description(weather.getDescription())
                .icon(weather.getIcon())
                .cloudiness(weather.getCloudiness())
                .visibility(weather.getVisibility())
                .uvIndex(weather.getUvIndex())
                .createdAt(weather.getCreatedAt().format(formatter))
                .updatedAt(weather.getUpdatedAt().format(formatter))
                .build();
    }
}
