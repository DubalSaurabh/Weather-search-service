# ============================================================================
# Weather Search Service - Quick Setup Guide
# ============================================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Weather Search Service - Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java installation
Write-Host "[1/4] Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString() }
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found! Please install Java 17 or higher." -ForegroundColor Red
    Write-Host "Download from: https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Check if API key is configured
Write-Host "[2/4] Checking API key configuration..." -ForegroundColor Yellow
$propsFile = "src\main\resources\application.properties"

if (Test-Path $propsFile) {
    $content = Get-Content $propsFile -Raw
    if ($content -match "openweather\.api\.key=YOUR_API_KEY_HERE") {
        Write-Host "⚠ API key not configured!" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Please follow these steps:" -ForegroundColor Cyan
        Write-Host "1. Visit: https://openweathermap.org/api" -ForegroundColor White
        Write-Host "2. Sign up for a free account" -ForegroundColor White
        Write-Host "3. Get your API key" -ForegroundColor White
        Write-Host "4. Edit $propsFile" -ForegroundColor White
        Write-Host "5. Replace YOUR_API_KEY_HERE with your actual API key" -ForegroundColor White
        Write-Host ""
        $response = Read-Host "Have you configured your API key? (y/n)"
        if ($response -ne "y") {
            Write-Host "Please configure your API key and run this script again." -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "✓ API key configured" -ForegroundColor Green
    }
} else {
    Write-Host "✗ application.properties not found!" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Build the project
Write-Host "[3/4] Building the project..." -ForegroundColor Yellow
Write-Host "This may take a few minutes on first run..." -ForegroundColor Gray

if (Test-Path "mvnw.cmd") {
    & .\mvnw.cmd clean package -DskipTests
} else {
    mvn clean package -DskipTests
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Build successful!" -ForegroundColor Green
Write-Host ""

# Ready to run
Write-Host "[4/4] Setup complete!" -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Ready to Run!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "To start the application, run:" -ForegroundColor Yellow
Write-Host "  java -jar target\weather-search-service-1.0.0.jar" -ForegroundColor White
Write-Host ""
Write-Host "Or use Maven:" -ForegroundColor Yellow
Write-Host "  .\mvnw.cmd spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "Then open your browser to:" -ForegroundColor Yellow
Write-Host "  http://localhost:8080/index.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$runNow = Read-Host "Would you like to start the application now? (y/n)"
if ($runNow -eq "y") {
    Write-Host ""
    Write-Host "Starting Weather Search Service..." -ForegroundColor Green
    Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
    Write-Host ""
    
    if (Test-Path "mvnw.cmd") {
        & .\mvnw.cmd spring-boot:run
    } else {
        mvn spring-boot:run
    }
}
