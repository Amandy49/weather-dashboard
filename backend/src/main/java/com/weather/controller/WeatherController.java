package com.weather.controller;

import com.google.gson.JsonObject;
import com.weather.model.ApiResponse;
import com.weather.model.dto.WeatherDTO;
import com.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/weather")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WeatherController {
    
    private final WeatherService weatherService;
    
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
    @GetMapping("/city")
    public ApiResponse<WeatherDTO> getWeatherByCity(@RequestParam String name) {
        log.info("Fetching weather for city: {}", name);
        try {
            WeatherDTO weather = weatherService.getOrUpdateWeatherByCity(name);
            if (weather != null) {
                return ApiResponse.success("获取天气成功", weather);
            }
            return ApiResponse.error(404, "城市未找到");
        } catch (Exception e) {
            log.error("Error fetching weather: ", e);
            return ApiResponse.error(500, "获取天气失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/coordinates")
    public ApiResponse<WeatherDTO> getWeatherByCoordinates(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        log.info("Fetching weather for coordinates: {}, {}", lat, lon);
        try {
            WeatherDTO weather = weatherService.getWeatherByCoordinates(lat, lon);
            if (weather != null) {
                return ApiResponse.success("获取天气成功", weather);
            }
            return ApiResponse.error(404, "无法获取该位置的天气信息");
        } catch (Exception e) {
            log.error("Error fetching weather: ", e);
            return ApiResponse.error(500, "获取天气失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/forecast")
    public ApiResponse<JsonObject> getForecast(@RequestParam String city) {
        log.info("Fetching forecast for city: {}", city);
        try {
            JsonObject forecast = weatherService.getForecast(city);
            if (forecast != null && forecast.has("list")) {
                return ApiResponse.success("获取预报成功", forecast);
            }
            return ApiResponse.error(404, "无法获取预报数据");
        } catch (Exception e) {
            log.error("Error fetching forecast: ", e);
            return ApiResponse.error(500, "获取预报失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/recent")
    public ApiResponse<List<WeatherDTO>> getRecentWeathers() {
        log.info("Fetching recent weathers");
        try {
            List<WeatherDTO> weathers = weatherService.getRecentWeathers();
            return ApiResponse.success("获取成功", weathers);
        } catch (Exception e) {
            log.error("Error fetching recent weathers: ", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ApiResponse<List<WeatherDTO>> searchCity(@RequestParam String keyword) {
        log.info("Searching for cities with keyword: {}", keyword);
        try {
            List<WeatherDTO> results = weatherService.searchCity(keyword);
            return ApiResponse.success("搜索成功", results);
        } catch (Exception e) {
            log.error("Error searching cities: ", e);
            return ApiResponse.error(500, "搜索失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("Weather API is running");
    }
}
