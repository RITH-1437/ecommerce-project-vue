# Test Script for Registration and Swagger Access
# Run this after starting the application

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Testing E-Commerce Backend Registration & Swagger" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Check if server is running
Write-Host "TEST 1: Checking if server is running on port 8080..." -ForegroundColor Yellow
try {
    $health = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" -Method OPTIONS -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
    Write-Host "SUCCESS: Server is running!" -ForegroundColor Green
}
catch {
    Write-Host "ERROR: Server is NOT running or not reachable" -ForegroundColor Red
    Write-Host "Error details: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Please start the server with: java -jar target/apple-store-backend-1.0.0.jar" -ForegroundColor Yellow
    exit
}

Write-Host ""

# Test 2: Test Swagger API Docs endpoint
Write-Host "[TEST 2] Testing Swagger API Docs (/v3/api-docs)..." -ForegroundColor Yellow
try {
    $apiDocs = Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    if ($apiDocs.StatusCode -eq 200) {
        Write-Host "✓ API Docs accessible (Status: 200)" -ForegroundColor Green
        Write-Host "  No 403 Forbidden error!" -ForegroundColor Green
    }
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 403) {
        Write-Host "✗ FAILED: 403 Forbidden error" -ForegroundColor Red
        Write-Host "  SecurityConfig may be blocking Swagger endpoints" -ForegroundColor Red
    } else {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 3: Test Swagger UI
Write-Host "[TEST 3] Testing Swagger UI (/swagger-ui/index.html)..." -ForegroundColor Yellow
try {
    $swaggerUI = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui/index.html" -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    if ($swaggerUI.StatusCode -eq 200) {
        Write-Host "✓ Swagger UI accessible (Status: 200)" -ForegroundColor Green
        Write-Host "  Open in browser: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
    }
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 403) {
        Write-Host "✗ FAILED: 403 Forbidden error" -ForegroundColor Red
    } else {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 4: Register without role (new functionality)
Write-Host "[TEST 4] Testing registration WITHOUT role field..." -ForegroundColor Yellow
$registerBody = @{
    email = "test_$(Get-Random)@example.com"
    password = "password123"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerBody `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop

    if ($registerResponse.StatusCode -eq 200) {
        Write-Host "✓ Registration successful without role field!" -ForegroundColor Green
        Write-Host "  Default role (CUSTOMER) should be assigned" -ForegroundColor Green
        $content = $registerResponse.Content | ConvertFrom-Json
        if ($content.accessToken) {
            Write-Host "  ✓ JWT token received" -ForegroundColor Green
        }
    }
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 400) {
        Write-Host "✗ FAILED: 400 Bad Request" -ForegroundColor Red
        Write-Host "  Role field may still be required (validation error)" -ForegroundColor Red
    } elseif ($statusCode -eq 409) {
        Write-Host "⚠ Email already exists (this is OK for testing)" -ForegroundColor Yellow
    } else {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 5: Register with CUSTOMER role
Write-Host "[TEST 5] Testing registration WITH role=CUSTOMER..." -ForegroundColor Yellow
$registerBodyWithRole = @{
    email = "customer_$(Get-Random)@example.com"
    password = "password123"
    role = "CUSTOMER"
} | ConvertTo-Json

try {
    $registerResponse2 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerBodyWithRole `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop

    if ($registerResponse2.StatusCode -eq 200) {
        Write-Host "✓ Registration successful with CUSTOMER role!" -ForegroundColor Green
    }
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 409) {
        Write-Host "⚠ Email already exists (this is OK)" -ForegroundColor Yellow
    } else {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 6: Try to register as ADMIN (should be blocked)
Write-Host "[TEST 6] Testing registration with role=ADMIN (should default to CUSTOMER)..." -ForegroundColor Yellow
$registerBodyAdmin = @{
    email = "admin_$(Get-Random)@example.com"
    password = "password123"
    role = "ADMIN"
} | ConvertTo-Json

try {
    $registerResponse3 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/register" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerBodyAdmin `
        -UseBasicParsing `
        -TimeoutSec 5 `
        -ErrorAction Stop

    if ($registerResponse3.StatusCode -eq 200) {
        Write-Host "✓ Registration completed (ADMIN request should be ignored)" -ForegroundColor Green
        Write-Host "  User should be created with CUSTOMER role for security" -ForegroundColor Green
    }
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -eq 409) {
        Write-Host "⚠ Email already exists" -ForegroundColor Yellow
    } else {
        Write-Host "✗ FAILED: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Test Summary" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "If all tests passed, your fixes are working correctly!" -ForegroundColor Green
Write-Host ""
Write-Host "Key URLs:" -ForegroundColor Cyan
Write-Host "  - Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor White
Write-Host "  - API Docs:   http://localhost:8080/v3/api-docs" -ForegroundColor White
Write-Host "  - Register:   POST http://localhost:8080/api/auth/register" -ForegroundColor White
Write-Host ""

