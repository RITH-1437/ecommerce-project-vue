# Test if the backend application is running
Write-Host "Testing Backend Application..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Kill any existing Java processes first
Write-Host "Cleaning up any existing Java processes..." -ForegroundColor Yellow
try {
    Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
} catch {
    # Ignore errors if no Java processes are running
}

# Test 1: Check if port 8080 is listening
Write-Host "[TEST 1] Checking if port 8080 is listening..." -ForegroundColor Yellow
$portInUse = $false
try {
    $port = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
    if ($port) {
        Write-Host "✓ Port 8080 is listening" -ForegroundColor Green
        $portInUse = $true
    } else {
        Write-Host "✗ Port 8080 is not listening" -ForegroundColor Red
        Write-Host "Starting application..." -ForegroundColor Yellow

        # Set environment variable and start application
        $env:DB_PASSWORD = "Iloveyouforever@096"
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD'; `$env:DB_PASSWORD='Iloveyouforever@096'; Write-Host 'Starting Apple Store Backend...'; java -jar target/apple-store-backend-1.0.0.jar" -WindowStyle Normal

        Write-Host "Waiting for application to start..." -ForegroundColor Yellow
        Start-Sleep -Seconds 45

        # Check again
        $port = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
        if ($port) {
            Write-Host "✓ Application started successfully!" -ForegroundColor Green
            $portInUse = $true
        } else {
            Write-Host "✗ Application failed to start" -ForegroundColor Red
        }
    }
} catch {
    Write-Host "✗ Could not check port status: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Only proceed with tests if application is running
if ($portInUse) {
    # Test 2: Check Swagger API Docs
    Write-Host "[TEST 2] Testing Swagger API Docs..." -ForegroundColor Yellow
    try {
        $response1 = Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -UseBasicParsing -TimeoutSec 10
        if ($response1.StatusCode -eq 200) {
            Write-Host "✓ SUCCESS - API Docs accessible (Status: 200)" -ForegroundColor Green
        }
    } catch {
        $statusCode = "Unknown"
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
        }
        Write-Host "✗ FAILED - Status: $statusCode" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "  This indicates a 403 Forbidden error - SecurityConfig issue" -ForegroundColor Yellow
    }

    Write-Host ""

    # Test 3: Check Swagger UI
    Write-Host "[TEST 3] Testing Swagger UI..." -ForegroundColor Yellow
    try {
        $response2 = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui/index.html" -UseBasicParsing -TimeoutSec 10
        if ($response2.StatusCode -eq 200) {
            Write-Host "✓ SUCCESS - Swagger UI accessible (Status: 200)" -ForegroundColor Green
            Write-Host "  Open in browser: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
        }
    } catch {
        $statusCode = "Unknown"
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
        }
        Write-Host "✗ FAILED - Status: $statusCode" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
    }

    Write-Host ""

    # Test 4: Test registration without role
    Write-Host "[TEST 4] Testing Registration without role..." -ForegroundColor Yellow
    $registerBody = @{
        email = "testuser_$(Get-Random)@example.com"
        password = "password123"
    } | ConvertTo-Json

    try {
        $response3 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
            -Method POST `
            -ContentType "application/json" `
            -Body $registerBody `
            -UseBasicParsing `
            -TimeoutSec 10
        if ($response3.StatusCode -eq 200) {
            Write-Host "✓ SUCCESS - Registration without role works!" -ForegroundColor Green
            $jsonResponse = $response3.Content | ConvertFrom-Json
            if ($jsonResponse.accessToken) {
                Write-Host "  Token received: $($jsonResponse.accessToken.Substring(0, 20))..." -ForegroundColor Green
            } elseif ($jsonResponse.token) {
                Write-Host "  Token received: $($jsonResponse.token.Substring(0, 20))..." -ForegroundColor Green
            } else {
                Write-Host "  Response: $($response3.Content)" -ForegroundColor Green
            }
        }
    } catch {
        $statusCode = "Unknown"
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
        }
        Write-Host "✗ FAILED - Status: $statusCode" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red

        if ($statusCode -eq 409) {
            Write-Host "  Note: 409 conflict usually means email already exists (this is OK)" -ForegroundColor Yellow
        }
    }
} else {
    Write-Host "⚠ Skipping API tests - Application is not running" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Testing complete!" -ForegroundColor Green
Write-Host ""

# Provide helpful information
Write-Host "📋 Summary:" -ForegroundColor White
if ($portInUse) {
    Write-Host "✅ Application is running on http://localhost:8080" -ForegroundColor Green
    Write-Host "🌐 Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
    Write-Host "📄 API Docs: http://localhost:8080/v3/api-docs" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "🔐 Test Accounts:" -ForegroundColor White
    Write-Host "   Admin: admin@applestore.com / admin@12345" -ForegroundColor Yellow
    Write-Host "   User:  user@example.com / user@123345" -ForegroundColor Yellow
} else {
    Write-Host "❌ Application failed to start" -ForegroundColor Red
    Write-Host "💡 Try running manually:" -ForegroundColor Yellow
    Write-Host "   Set `$env:DB_PASSWORD='Iloveyouforever@096'" -ForegroundColor Gray
    Write-Host "   java -jar target/apple-store-backend-1.0.0.jar" -ForegroundColor Gray
}
Write-Host ""
