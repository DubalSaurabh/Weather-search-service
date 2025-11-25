package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CacheStats {
    private long hitCount;
    private long missCount;
    private double hitRate;
    private long evictionCount;
    private long size;
}
