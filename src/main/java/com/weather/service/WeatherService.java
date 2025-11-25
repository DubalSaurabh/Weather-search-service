package com.weather.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.weather.exception.WeatherServiceException;
import com.weather.model.CacheStats;
import com.weather.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;
    private final CacheManager cacheManager;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    public WeatherService(RestTemplate restTemplate, CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.cacheManager = cacheManager;
    }

    /**
     * Fetch current weather for a city with caching support.
     * Cache key is the lowercase city name for case-insensitive matching.
     * 
     * @param cityName The name of the city
     * @return WeatherResponse containing current weather data
     * @throws WeatherServiceException if the API call fails
     */
    @Cacheable(value = "weatherCache", key = "#cityName.toLowerCase()")
    public WeatherResponse getCurrentWeather(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new WeatherServiceException("City name cannot be empty");
        }

        if ("YOUR_API_KEY_HERE".equals(apiKey)) {
            throw new WeatherServiceException("Please configure your OpenWeatherMap API key in application.properties");
        }

        logger.info("Fetching weather data from API for city: {}", cityName);

        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", cityName.trim())
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            
            if (response == null) {
                throw new WeatherServiceException("Empty response from weather API");
            }

            logger.info("Successfully fetched weather data for: {}", cityName);
            return response;

        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {}", cityName, e);
            throw e;
        }
    }

    /**
     * Check if weather data for a city is cached.
     * 
     * @param cityName The name of the city
     * @return true if cached, false otherwise
     */
    public boolean isCached(String cityName) {
        org.springframework.cache.Cache cache = cacheManager.getCache("weatherCache");
        if (cache != null) {
            return cache.get(cityName.toLowerCase()) != null;
        }
        return false;
    }

    /**
     * Get cache statistics.
     * 
     * @return CacheStats object containing cache metrics
     */
    public CacheStats getCacheStats() {
        org.springframework.cache.Cache cache = cacheManager.getCache("weatherCache");
        
        if (cache instanceof CaffeineCache) {
            Cache<Object, Object> nativeCache = ((CaffeineCache) cache).getNativeCache();
            com.github.benmanes.caffeine.cache.stats.CacheStats stats = nativeCache.stats();
            
            return new CacheStats(
                    stats.hitCount(),
                    stats.missCount(),
                    stats.hitRate(),
                    stats.evictionCount(),
                    nativeCache.estimatedSize()
            );
        }
        
        return new CacheStats(0, 0, 0.0, 0, 0);
    }

    /**
     * Clear all cached weather data.
     */
    public void clearCache() {
        org.springframework.cache.Cache cache = cacheManager.getCache("weatherCache");
        if (cache != null) {
            cache.clear();
            logger.info("Weather cache cleared");
        }
    }
}
