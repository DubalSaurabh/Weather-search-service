package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private boolean cached;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data, boolean cached) {
        return new ApiResponse<>(true, "Success", data, cached, java.lang.System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, false, java.lang.System.currentTimeMillis());
    }
}
