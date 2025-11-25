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
