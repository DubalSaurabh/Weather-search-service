package com.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    
    @JsonProperty("coord")
    private Coordinates coordinates;
    
    @JsonProperty("weather")
    private List<Weather> weather;
    
    @JsonProperty("base")
    private String base;
    
    @JsonProperty("main")
    private Main main;
    
    @JsonProperty("visibility")
    private Integer visibility;
    
    @JsonProperty("wind")
    private Wind wind;
    
    @JsonProperty("clouds")
    private Clouds clouds;
    
    @JsonProperty("dt")
    private Long timestamp;
    
    @JsonProperty("sys")
    private System system;
    
    @JsonProperty("timezone")
    private Integer timezone;
    
    @JsonProperty("id")
    private Long cityId;
    
    @JsonProperty("name")
    private String cityName;
    
    @JsonProperty("cod")
    private Integer code;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {
        @JsonProperty("lon")
        private Double longitude;
        
        @JsonProperty("lat")
        private Double latitude;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("main")
        private String main;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("icon")
        private String icon;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private Double temperature;
        
        @JsonProperty("feels_like")
        private Double feelsLike;
        
        @JsonProperty("temp_min")
        private Double tempMin;
        
        @JsonProperty("temp_max")
        private Double tempMax;
        
        @JsonProperty("pressure")
        private Integer pressure;
        
        @JsonProperty("humidity")
        private Integer humidity;
        
        @JsonProperty("sea_level")
        private Integer seaLevel;
        
        @JsonProperty("grnd_level")
        private Integer groundLevel;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        @JsonProperty("speed")
        private Double speed;
        
        @JsonProperty("deg")
        private Integer degrees;
        
        @JsonProperty("gust")
        private Double gust;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        @JsonProperty("all")
        private Integer all;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class System {
        @JsonProperty("type")
        private Integer type;
        
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("country")
        private String country;
        
        @JsonProperty("sunrise")
        private Long sunrise;
        
        @JsonProperty("sunset")
        private Long sunset;
    }
}
