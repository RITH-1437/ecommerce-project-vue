# Run Spring Boot Application with environment variables from .env file
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Apple Store Backend Application" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if .env file exists
if (Test-Path ".env") {
    Write-Host "📁 Loading environment variables from .env file..." -ForegroundColor Yellow

    # Read .env file and set environment variables
    Get-Content .env | ForEach-Object {
        if ($_ -match '^([^#][^=]+)=(.*)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            Set-Item -Path "env:$name" -Value $value
            Write-Host "   ✓ Set $name" -ForegroundColor Green
        }
    }
} else {
    Write-Host "❌ .env file not found!" -ForegroundColor Red
    Write-Host "Please copy environment-variables.example to .env and update with your credentials" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "🔧 Configuration:" -ForegroundColor Yellow
Write-Host "   Database: appl_store" -ForegroundColor White
Write-Host "   Port: 8080" -ForegroundColor White
Write-Host "   Profile: default" -ForegroundColor White
Write-Host ""

# Change to project directory
Set-Location $PSScriptRoot\..

Write-Host "🚀 Starting Spring Boot application..." -ForegroundColor Green
Write-Host "   Access at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "   Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "   Stop with: Ctrl+C" -ForegroundColor Yellow
Write-Host ""

# Run the application
& ./mvnw spring-boot:run
