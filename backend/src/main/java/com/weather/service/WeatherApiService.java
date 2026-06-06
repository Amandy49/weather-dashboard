package com.weather.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class WeatherApiService {
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.endpoint}")
    private String endpoint;
    
    @Value("${weather.api.timeout:10000}")
    private int timeout;
    
    private final Gson gson = new Gson();
    
    public JsonObject getWeatherByCity(String city) {
        try {
            String url = endpoint + "/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=zh_cn";
            return makeApiCall(url);
        } catch (Exception e) {
            log.error("Failed to fetch weather for city: {}", city, e);
            return null;
        }
    }
    
    public JsonObject getWeatherByCoordinates(Double latitude, Double longitude) {
        try {
            String url = endpoint + "/weather?lat=" + latitude + "&lon=" + longitude + 
                        "&appid=" + apiKey + "&units=metric&lang=zh_cn";
            return makeApiCall(url);
        } catch (Exception e) {
            log.error("Failed to fetch weather for coordinates: {}, {}", latitude, longitude, e);
            return null;
        }
    }
    
    public JsonObject getForecast(String city) {
        try {
            String url = endpoint + "/forecast?q=" + city + "&appid=" + apiKey + "&units=metric&lang=zh_cn";
            return makeApiCall(url);
        } catch (Exception e) {
            log.error("Failed to fetch forecast for city: {}", city, e);
            return null;
        }
    }
    
    private JsonObject makeApiCall(String url) throws IOException {
          log.info("Request URL: {}", url);
    HttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader("User-Agent", "Weather-Dashboard/1.0");

    return httpClient.execute(httpGet, response -> {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            return gson.fromJson(content, JsonObject.class);
        }
        return null;
    });
  }
}
