package com.weather.controller;

import com.weather.model.ApiResponse;
import com.weather.model.CacheStats;
import com.weather.model.WeatherResponse;
import com.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void searchWeather_WithValidCity_ReturnsWeatherData() throws Exception {
        // Arrange
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCityName("London");
        
        when(weatherService.isCached(anyString())).thenReturn(false);
        when(weatherService.getCurrentWeather(anyString())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/weather/search")
                        .param("city", "London")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cityName").value("London"));
    }

    @Test
    void getCacheStats_ReturnsStatistics() throws Exception {
        // Arrange
        CacheStats mockStats = new CacheStats(10, 5, 0.667, 0, 5);
        when(weatherService.getCacheStats()).thenReturn(mockStats);

        // Act & Assert
        mockMvc.perform(get("/api/weather/cache/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hitCount").value(10));
    }

    @Test
    void healthCheck_ReturnsHealthy() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/weather/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Service is healthy"));
    }
}
