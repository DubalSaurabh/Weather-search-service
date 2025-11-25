package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherApplication {

    public static void main(String[] args) {

        SpringApplication.run(WeatherApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("Weather Search Service Started Successfully");
        System.out.println("API Available at: http://localhost:8080");
        System.out.println("Frontend: http://localhost:8080/index.html");
        System.out.println("===========================================\n");
    }
}
