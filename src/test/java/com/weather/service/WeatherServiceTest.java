package com.weather.service;

import com.weather.exception.WeatherServiceException;
import com.weather.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CacheManager cacheManager;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(restTemplate, cacheManager);
    }

    @Test
    void getCurrentWeather_WithValidCity_ReturnsWeatherData() {
        // Arrange
        String city = "London";
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCityName("London");
        
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class)))
                .thenReturn(mockResponse);

        // Act
        WeatherResponse result = weatherService.getCurrentWeather(city);

        // Assert
        assertNotNull(result);
        assertEquals("London", result.getCityName());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(WeatherResponse.class));
    }

    @Test
    void getCurrentWeather_WithEmptyCity_ThrowsException() {
        // Act & Assert
        assertThrows(WeatherServiceException.class, () -> {
            weatherService.getCurrentWeather("");
        });
    }

    @Test
    void getCurrentWeather_WithNullCity_ThrowsException() {
        // Act & Assert
        assertThrows(WeatherServiceException.class, () -> {
            weatherService.getCurrentWeather(null);
        });
    }
}
