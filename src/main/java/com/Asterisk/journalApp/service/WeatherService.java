package com.Asterisk.journalApp.service;

import com.Asterisk.journalApp.api.response.WeatherResponse;
import com.Asterisk.journalApp.cache.AppCache;
import com.Asterisk.journalApp.constants.PlaceHolders;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        } else {
            String finalAPI = appCache.getAppCache().get("weather_api")
                    .replace(PlaceHolders.CITY, city)
                    .replace(PlaceHolders.API_KEY, apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(
                    finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body != null) {
                redisService.set("weather_of_" + city, body, 300l);
            }
            return body;
        }
    }
}