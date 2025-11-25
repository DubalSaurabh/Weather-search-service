const { useState, useEffect } = React;

const API_BASE_URL = 'http://localhost:8080/api/weather';

function App() {
    const [city, setCity] = useState('');
    const [weather, setWeather] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [cached, setCached] = useState(false);
    const [cacheStats, setCacheStats] = useState(null);
    const [recentSearches, setRecentSearches] = useState([]);

    useEffect(() => {
        const saved = localStorage.getItem('recentSearches');
        if (saved) {
            setRecentSearches(JSON.parse(saved));
        }
        fetchCacheStats();
    }, []);

    const fetchCacheStats = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/cache/stats`);
            const data = await response.json();
            if (data.success) {
                setCacheStats(data.data);
            }
        } catch (err) {
            console.error('Failed to fetch cache stats:', err);
        }
    };

    const searchWeather = async (searchCity) => {
        if (!searchCity || searchCity.trim() === '') {
            setError('Please enter a city name');
            return;
        }

        setLoading(true);
        setError('');
        setWeather(null);

        try {
            const response = await fetch(`${API_BASE_URL}/search?city=${encodeURIComponent(searchCity.trim())}`);
            const data = await response.json();

            if (data.success) {
                setWeather(data.data);
                setCached(data.cached);
                
                // Update recent searches
                const newSearches = [searchCity.trim(), ...recentSearches.filter(s => s.toLowerCase() !== searchCity.trim().toLowerCase())].slice(0, 5);
                setRecentSearches(newSearches);
                localStorage.setItem('recentSearches', JSON.stringify(newSearches));
                
                fetchCacheStats();
            } else {
                setError(data.message || 'Failed to fetch weather data');
            }
        } catch (err) {
            setError('Network error. Please check if the server is running.');
            console.error('Error:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        searchWeather(city);
    };

    const handleRecentSearch = (searchCity) => {
        setCity(searchCity);
        searchWeather(searchCity);
    };

    const clearCache = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/cache/clear`, { method: 'POST' });
            const data = await response.json();
            if (data.success) {
                alert('Cache cleared successfully!');
                fetchCacheStats();
            }
        } catch (err) {
            alert('Failed to clear cache');
        }
    };

    const getWeatherIcon = (iconCode) => {
        return `https://openweathermap.org/img/wn/${iconCode}@4x.png`;
    };

    const formatTime = (timestamp) => {
        return new Date(timestamp * 1000).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    return (
        <div className="app">
            <div className="container">
                <header className="header">
                    <h1 className="title">🌤️ Weather Search</h1>
                    <p className="subtitle">Get current weather information for any city worldwide</p>
                </header>

                <div className="search-section">
                    <form onSubmit={handleSubmit} className="search-form">
                        <div className="search-input-wrapper">
                            <input
                                type="text"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                placeholder="Enter city name (e.g., London, Tokyo, New York)"
                                className="search-input"
                                disabled={loading}
                            />
                            <button type="submit" className="search-button" disabled={loading}>
                                {loading ? '🔍 Searching...' : '🔍 Search'}
                            </button>
                        </div>
                    </form>

                    {recentSearches.length > 0 && (
                        <div className="recent-searches">
                            <span className="recent-label">Recent:</span>
                            {recentSearches.map((search, index) => (
                                <button
                                    key={index}
                                    onClick={() => handleRecentSearch(search)}
                                    className="recent-chip"
                                    disabled={loading}
                                >
                                    {search}
                                </button>
                            ))}
                        </div>
                    )}
                </div>

                {error && (
                    <div className="error-message">
                        ⚠️ {error}
                    </div>
                )}

                {loading && (
                    <div className="loading">
                        <div className="spinner"></div>
                        <p>Fetching weather data...</p>
                    </div>
                )}

                {weather && !loading && (
                    <div className="weather-card">
                        <div className="weather-header">
                            <div className="city-info">
                                <h2 className="city-name">
                                    {weather.cityName}, {weather.system.country}
                                </h2>
                                <p className="coordinates">
                                    📍 {weather.coordinates.latitude.toFixed(2)}°, {weather.coordinates.longitude.toFixed(2)}°
                                </p>
                            </div>
                            {cached && (
                                <div className="cache-badge">⚡ Cached</div>
                            )}
                        </div>

                        <div className="weather-main">
                            <div className="weather-icon-section">
                                <img
                                    src={getWeatherIcon(weather.weather[0].icon)}
                                    alt={weather.weather[0].description}
                                    className="weather-icon"
                                />
                                <p className="weather-description">
                                    {weather.weather[0].description}
                                </p>
                            </div>

                            <div className="temperature-section">
                                <div className="temperature">
                                    {Math.round(weather.main.temperature)}°C
                                </div>
                                <div className="feels-like">
                                    Feels like {Math.round(weather.main.feelsLike)}°C
                                </div>
                            </div>
                        </div>

                        <div className="weather-details">
                            <div className="detail-card">
                                <div className="detail-icon">🌡️</div>
                                <div className="detail-content">
                                    <div className="detail-label">Min / Max</div>
                                    <div className="detail-value">
                                        {Math.round(weather.main.tempMin)}° / {Math.round(weather.main.tempMax)}°
                                    </div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">💧</div>
                                <div className="detail-content">
                                    <div className="detail-label">Humidity</div>
                                    <div className="detail-value">{weather.main.humidity}%</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">💨</div>
                                <div className="detail-content">
                                    <div className="detail-label">Wind Speed</div>
                                    <div className="detail-value">{weather.wind.speed} m/s</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">🧭</div>
                                <div className="detail-content">
                                    <div className="detail-label">Wind Direction</div>
                                    <div className="detail-value">{weather.wind.degrees}°</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">🔽</div>
                                <div className="detail-content">
                                    <div className="detail-label">Pressure</div>
                                    <div className="detail-value">{weather.main.pressure} hPa</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">👁️</div>
                                <div className="detail-content">
                                    <div className="detail-label">Visibility</div>
                                    <div className="detail-value">{(weather.visibility / 1000).toFixed(1)} km</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">☁️</div>
                                <div className="detail-content">
                                    <div className="detail-label">Cloudiness</div>
                                    <div className="detail-value">{weather.clouds.all}%</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">🌅</div>
                                <div className="detail-content">
                                    <div className="detail-label">Sunrise</div>
                                    <div className="detail-value">{formatTime(weather.system.sunrise)}</div>
                                </div>
                            </div>

                            <div className="detail-card">
                                <div className="detail-icon">🌇</div>
                                <div className="detail-content">
                                    <div className="detail-label">Sunset</div>
                                    <div className="detail-value">{formatTime(weather.system.sunset)}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {cacheStats && (
                    <div className="cache-stats">
                        <div className="cache-stats-header">
                            <h3>📊 Cache Performance</h3>
                            <button onClick={clearCache} className="clear-cache-btn">
                                🗑️ Clear Cache
                            </button>
                        </div>
                        <div className="stats-grid">
                            <div className="stat-item">
                                <div className="stat-label">Hit Rate</div>
                                <div className="stat-value">{(cacheStats.hitRate * 100).toFixed(1)}%</div>
                            </div>
                            <div className="stat-item">
                                <div className="stat-label">Hits</div>
                                <div className="stat-value">{cacheStats.hitCount}</div>
                            </div>
                            <div className="stat-item">
                                <div className="stat-label">Misses</div>
                                <div className="stat-value">{cacheStats.missCount}</div>
                            </div>
                            <div className="stat-item">
                                <div className="stat-label">Cached Entries</div>
                                <div className="stat-value">{cacheStats.size}</div>
                            </div>
                        </div>
                    </div>
                )}

                <footer className="footer">
                    <p>Powered by <a href="https://openweathermap.org" target="_blank" rel="noopener noreferrer">OpenWeatherMap API</a></p>
                    <p className="tech-stack">Built with Java Spring Boot + React</p>
                </footer>
            </div>
        </div>
    );
}

ReactDOM.render(<App />, document.getElementById('root'));
