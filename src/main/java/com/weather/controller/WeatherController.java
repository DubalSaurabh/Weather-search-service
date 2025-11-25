package com.weather.controller;

import com.weather.model.ApiResponse;
import com.weather.model.CacheStats;
import com.weather.model.WeatherResponse;
import com.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;

/**
 * REST Controller for weather operations.
 * Provides endpoints to search weather by city name and manage cache.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * GET /api/weather/search?city={cityName}
     * Search for current weather by city name.
     * 
     * @param city The name of the city (required)
     * @return ApiResponse containing weather data and cache status
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<WeatherResponse>> searchWeather(
            @RequestParam @NotBlank(message = "City name is required") String city) {
        
        logger.info("Received weather search request for city: {}", city);
        
        boolean wasCached = weatherService.isCached(city);
        WeatherResponse weatherData = weatherService.getCurrentWeather(city);
        boolean isCached = weatherService.isCached(city);
        
        logger.info("Weather data for '{}' served from {}", city, isCached && !wasCached ? "API (now cached)" : isCached ? "cache" : "API");
        
        return ResponseEntity.ok(ApiResponse.success(weatherData, isCached && wasCached));
    }

    /**
     * GET /api/weather/cache/stats
     * Get cache statistics.
     * 
     * @return ApiResponse containing cache statistics
     */
    @GetMapping("/cache/stats")
    public ResponseEntity<ApiResponse<CacheStats>> getCacheStats() {
        logger.info("Received cache stats request");
        CacheStats stats = weatherService.getCacheStats();
        return ResponseEntity.ok(ApiResponse.success(stats, false));
    }

    /**
     * POST /api/weather/cache/clear
     * Clear all cached weather data.
     * 
     * @return ApiResponse with success message
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResponse<String>> clearCache() {
        logger.info("Received cache clear request");
        weatherService.clearCache();
        return ResponseEntity.ok(ApiResponse.success("Cache cleared successfully", false));
    }

    /**
     * GET /api/weather/health
     * Health check endpoint.
     * 
     * @return ApiResponse with health status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", false));
    }
}
