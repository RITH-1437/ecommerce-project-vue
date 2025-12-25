# Test Swagger Access
Write-Host "Testing Swagger and API endpoints..." -ForegroundColor Cyan
Write-Host ""

# Test 1: Swagger UI HTML
Write-Host "[TEST 1] Testing Swagger UI (HTML)..." -ForegroundColor Yellow
try {
    $response1 = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui/index.html" -UseBasicParsing -TimeoutSec 5
    if ($response1.StatusCode -eq 200) {
        Write-Host "✓ SUCCESS - Swagger UI accessible (Status: 200)" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ FAILED - Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: API Docs JSON
Write-Host "[TEST 2] Testing API Docs (/v3/api-docs)..." -ForegroundColor Yellow
try {
    $response2 = Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -UseBasicParsing -TimeoutSec 5
    if ($response2.StatusCode -eq 200) {
        Write-Host "✓ SUCCESS - API Docs accessible (Status: 200)" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ FAILED - Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Register without role
Write-Host "[TEST 3] Testing Registration without role..." -ForegroundColor Yellow
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
        -TimeoutSec 5

    if ($response3.StatusCode -eq 200) {
        Write-Host "✓ SUCCESS - Registration without role works!" -ForegroundColor Green
        $jsonResponse = $response3.Content | ConvertFrom-Json
        Write-Host "  Token received: $($jsonResponse.accessToken.Substring(0, 50))..." -ForegroundColor Green
    }
} catch {
    Write-Host "✗ FAILED - Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 4: Login with admin account
Write-Host "[TEST 4] Testing Admin Login..." -ForegroundColor Yellow
$loginBody = @{
    email = "admin@applestore.com"
    password = "admin@12345"
} | ConvertTo-Json

try {
    $response4 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody `
        -UseBasicParsing `
        -TimeoutSec 5

    if ($response4.StatusCode -eq 200) {
        Write-Host "✓ SUCCESS - Admin login works!" -ForegroundColor Green
        $jsonResponse = $response4.Content | ConvertFrom-Json
        Write-Host "  Admin email: $($jsonResponse.email)" -ForegroundColor Green
        Write-Host "  Role: $($jsonResponse.role)" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ FAILED - Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 5: Login with user account
Write-Host "[TEST 5] Testing User Login..." -ForegroundColor Yellow
$loginBody2 = @{
    email = "user@example.com"
    password = "user@123345"
} | ConvertTo-Json

try {
    $response5 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody2 `
        -UseBasicParsing `
        -TimeoutSec 5

    if ($response5.StatusCode -eq 200) {
        Write-Host "✓ SUCCESS - User login works!" -ForegroundColor Green
        $jsonResponse = $response5.Content | ConvertFrom-Json
        Write-Host "  User email: $($jsonResponse.email)" -ForegroundColor Green
        Write-Host "  Role: $($jsonResponse.role)" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ FAILED - Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "Testing complete!" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Access Swagger UI at: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Yellow

