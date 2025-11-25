# Weather Search Service

A full-stack weather search application that allows users to search for current weather information by city name. Built with **Java Spring Boot** backend and **React** frontend, featuring intelligent caching for improved performance.

## 🌟 Features

### Backend (Java Spring Boot)
- **RESTful API** following REST best practices
- **Intelligent Caching** using Caffeine cache with configurable expiry and max entries
- **OpenWeatherMap API Integration** for real-time weather data
- **Robust Error Handling** with comprehensive exception management
- **Cache Statistics** endpoint for monitoring cache performance
- **Health Check** endpoint for service monitoring

### Frontend (React)
- **Modern, Responsive UI** with beautiful gradient design
- **Real-time Weather Display** with rich information
- **Recent Search History** with localStorage persistence
- **Cache Performance Dashboard** showing hit rate and statistics
- **Detailed Weather Metrics** including:
  - Temperature (current, min, max, feels like)
  - Humidity and pressure
  - Wind speed and direction
  - Visibility and cloudiness
  - Sunrise and sunset times
  - Geographic coordinates

### Performance Optimizations
- **LRU Cache** with configurable size (default: 1000 entries)
- **TTL-based Expiration** (default: 10 minutes)
- **Cache Hit Rate Tracking** for monitoring performance
- **Case-insensitive City Matching** for better user experience

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **OpenWeatherMap API Key** (free tier available)
- Modern web browser (Chrome, Firefox, Safari, Edge)

## 🚀 Quick Start

### 1. Get OpenWeatherMap API Key

1. Visit [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Navigate to API Keys section
4. Copy your API key

### 2. Configure the Application

Edit `src/main/resources/application.properties` and replace `YOUR_API_KEY_HERE` with your actual API key:

```properties
openweather.api.key=YOUR_ACTUAL_API_KEY_HERE
```

### 3. Build the Application

**Option A: Using Maven Wrapper (Recommended)**
```powershell
.\mvnw clean package
```

**Option B: Using System Maven**
```powershell
mvn clean package
```

This will create an executable JAR file in the `target` directory.

### 4. Run the Application

```powershell
java -jar target/weather-search-service-1.0.0.jar
```

Or using Maven:
```powershell
.\mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Access the Application

Open your web browser and navigate to:
```
http://localhost:8080/index.html
```

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api/weather
```

### Endpoints

#### 1. Search Weather by City
```http
GET /api/weather/search?city={cityName}
```

**Parameters:**
- `city` (required): Name of the city (e.g., "London", "New York", "Tokyo")

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "cityName": "London",
    "main": {
      "temperature": 15.5,
      "feelsLike": 14.2,
      "humidity": 72,
      "pressure": 1013
    },
    "weather": [
      {
        "main": "Clouds",
        "description": "scattered clouds",
        "icon": "03d"
      }
    ],
    "wind": {
      "speed": 5.5,
      "degrees": 230
    },
    ...
  },
  "cached": false,
  "timestamp": 1700000000000
}
```

#### 2. Get Cache Statistics
```http
GET /api/weather/cache/stats
```

**Response:**
```json
{
  "success": true,
  "data": {
    "hitCount": 45,
    "missCount": 12,
    "hitRate": 0.789,
    "evictionCount": 2,
    "size": 10
  }
}
```

#### 3. Clear Cache
```http
POST /api/weather/cache/clear
```

**Response:**
```json
{
  "success": true,
  "message": "Success",
  "data": "Cache cleared successfully"
}
```

#### 4. Health Check
```http
GET /api/weather/health
```

**Response:**
```json
{
  "success": true,
  "data": "Service is healthy"
}
```

## 🏗️ Project Structure

```
Weather/
├── src/
│   ├── main/
│   │   ├── java/com/weather/
│   │   │   ├── WeatherApplication.java          # Main application class
│   │   │   ├── config/
│   │   │   │   ├── CacheConfig.java             # Cache configuration
│   │   │   │   └── WebConfig.java               # CORS and RestTemplate config
│   │   │   ├── controller/
│   │   │   │   └── WeatherController.java       # REST API endpoints
│   │   │   ├── service/
│   │   │   │   └── WeatherService.java          # Business logic with caching
│   │   │   ├── model/
│   │   │   │   ├── WeatherResponse.java         # Weather data model
│   │   │   │   ├── ApiResponse.java             # Standard API response
│   │   │   │   └── CacheStats.java              # Cache statistics model
│   │   │   └── exception/
│   │   │       ├── WeatherServiceException.java # Custom exception
│   │   │       └── GlobalExceptionHandler.java  # Centralized error handling
│   │   └── resources/
│   │       ├── application.properties            # Application configuration
│   │       └── static/
│   │           ├── index.html                    # Main HTML page
│   │           ├── app.js                        # React application
│   │           └── styles.css                    # Styling
│   └── test/                                     # Unit tests (optional)
├── pom.xml                                       # Maven dependencies
└── README.md                                     # This file
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# OpenWeatherMap API
openweather.api.key=YOUR_API_KEY_HERE
openweather.api.url=https://api.openweathermap.org/data/2.5/weather

# Cache Configuration
cache.max-size=1000                    # Maximum number of cached entries
cache.expire-after-write-minutes=10    # Cache TTL in minutes

# CORS Configuration
cors.allowed-origins=http://localhost:3000,http://localhost:8080
```

## 🎯 Technical Highlights

### Cache Implementation
- **Technology**: Caffeine (high-performance caching library)
- **Strategy**: LRU (Least Recently Used) eviction policy
- **Key Normalization**: Case-insensitive city name matching
- **Metrics**: Real-time hit/miss tracking for performance monitoring

### REST API Design
- **Follows REST Principles**: Resource-based URLs, proper HTTP methods
- **Standard Response Format**: Consistent API response structure
- **Error Handling**: Comprehensive exception handling with meaningful messages
- **CORS Enabled**: Cross-origin requests supported

### Code Quality
- **Clean Architecture**: Separation of concerns (Controller → Service → External API)
- **Exception Handling**: Global exception handler for centralized error management
- **Validation**: Input validation for API parameters
- **Logging**: Comprehensive logging using SLF4J
- **Configuration Management**: Externalized configuration via properties file

### Edge Cases Handled
- Invalid/empty city names
- API key not configured
- Network timeouts and connection errors
- City not found (404 from OpenWeatherMap)
- API rate limiting
- Malformed API responses
- Cache overflow (LRU eviction)
- Concurrent request handling

## 🧪 Testing the Application

### Manual Testing

1. **Search for a city**:
   - Enter "London" and click Search
   - Verify weather data displays correctly
   - Note "Cached" badge is NOT shown (first request)

2. **Test caching**:
   - Search for "London" again
   - Verify "⚡ Cached" badge appears
   - Response should be instantaneous

3. **Test case-insensitivity**:
   - Search for "LONDON", "london", "London"
   - All should return cached data after first request

4. **Test cache statistics**:
   - Observe the cache performance section
   - Hit rate should increase with repeated searches

5. **Test error handling**:
   - Search for "InvalidCityName123456"
   - Verify appropriate error message displays

### Using cURL

```powershell
# Search for weather
curl "http://localhost:8080/api/weather/search?city=London"

# Get cache stats
curl "http://localhost:8080/api/weather/cache/stats"

# Clear cache
curl -X POST "http://localhost:8080/api/weather/cache/clear"

# Health check
curl "http://localhost:8080/api/weather/health"
```

## 📊 Performance Metrics

The application provides real-time cache performance metrics:

- **Hit Rate**: Percentage of requests served from cache
- **Hit Count**: Number of cache hits
- **Miss Count**: Number of cache misses (API calls)
- **Cached Entries**: Current number of cities in cache
- **Eviction Count**: Number of entries evicted due to size/TTL limits

With proper cache configuration, you can achieve:
- **90%+ hit rate** for frequently searched cities
- **Sub-millisecond response times** for cached data
- **Significant reduction** in API calls to OpenWeatherMap

## 🔧 Troubleshooting

### Port 8080 Already in Use
```powershell
# Windows: Find and kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8081
```

### API Key Issues
- Verify API key is correctly set in `application.properties`
- Ensure there are no extra spaces or quotes
- Check if API key is activated (may take a few minutes)
- Free tier has rate limits (60 calls/minute)

### Build Failures
```powershell
# Clean and rebuild
.\mvnw clean install -U

# Skip tests if needed
.\mvnw clean package -DskipTests
```

### CORS Errors
If accessing from a different port, add it to `cors.allowed-origins` in `application.properties`

## 📝 API Provider Information

**Provider**: [OpenWeatherMap](https://openweathermap.org)

**API Used**: Current Weather Data API  
**Endpoint**: `https://api.openweathermap.org/data/2.5/weather`

**Why OpenWeatherMap?**
- Free tier with generous limits (60 calls/minute, 1M calls/month)
- Comprehensive weather data coverage
- Reliable service with 99.9% uptime
- Rich data attributes (temperature, humidity, wind, etc.)
- Icon codes for weather visualization
- Well-documented API

**Rate Limits (Free Tier)**:
- 60 calls per minute
- 1,000,000 calls per month
- No credit card required

## 🚀 Deployment Considerations

### Running as a Service

**Windows Service:**
Use NSSM (Non-Sucking Service Manager) or create a Windows Service wrapper

**Linux Service (systemd):**
```ini
[Unit]
Description=Weather Search Service
After=network.target

[Service]
Type=simple
User=weatheruser
ExecStart=/usr/bin/java -jar /opt/weather/weather-search-service-1.0.0.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

### Production Checklist
- [ ] Configure proper API key
- [ ] Adjust cache size based on expected load
- [ ] Set appropriate cache TTL
- [ ] Configure logging levels
- [ ] Enable HTTPS
- [ ] Set up monitoring and alerting
- [ ] Configure firewall rules
- [ ] Set up database for persistent storage (optional)
- [ ] Implement rate limiting
- [ ] Add authentication/authorization if needed

## 📄 License

