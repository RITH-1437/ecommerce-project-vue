# Simple Test Script for Registration and Swagger
Write-Host "Testing Backend Services..." -ForegroundColor Cyan
Write-Host ""

# Test Swagger API Docs
Write-Host "Test 1: Swagger API Docs" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -UseBasicParsing -TimeoutSec 5
    Write-Host "SUCCESS - Status: $($response.StatusCode)" -ForegroundColor Green
}
catch {
    Write-Host "FAILED - Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test Swagger UI
Write-Host "Test 2: Swagger UI" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui/index.html" -UseBasicParsing -TimeoutSec 5
    Write-Host "SUCCESS - Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Open in browser: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
}
catch {
    Write-Host "FAILED - Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test Registration without role
Write-Host "Test 3: Register without role" -ForegroundColor Yellow
$randomEmail = "test$(Get-Random)@example.com"
$body = "{`"email`":`"$randomEmail`",`"password`":`"password123`"}"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -ContentType "application/json" -Body $body -TimeoutSec 5
    Write-Host "SUCCESS - Registration without role works!" -ForegroundColor Green
    Write-Host "Token received: $($response.accessToken.Substring(0, 20))..." -ForegroundColor Green
}
catch {
    $errorMessage = $_.Exception.Message
    if ($errorMessage -like "*409*") {
        Write-Host "Email conflict (OK for testing)" -ForegroundColor Yellow
    }
    else {
        Write-Host "FAILED - Error: $errorMessage" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Testing complete!" -ForegroundColor Cyan

