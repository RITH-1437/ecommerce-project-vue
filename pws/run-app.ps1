# Run Spring Boot Application with proper environment setup
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Apple Store Backend Application" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set environment variables
$env:DB_PASSWORD="Iloveyouforever@096"
$env:SPRING_PROFILES_ACTIVE="default"
$env:JWT_SECRET="ILOVEYOUFOREVER_1437_143_5201314_@_0966273314"
$env:GOOGLE_CLIENT_ID="526699009685-7m4br5u539va9ls80ge99vpp7qulm3s9.apps.googleusercontent.com"
$env:GOOGLE_CLIENT_SECRET="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"
$env:PAYWAY_MERCHANT_ID="ec462838"
$env:PAYWAY_API_KEY="3a29307362b55f16c1eaaf402df554c296736383"
$env:PAYWAY_SECRET_KEY="Iloveyouforever@096"
$env:OPENAI_API_KEY="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"

Write-Host "🔧 Configuration:" -ForegroundColor Yellow
Write-Host "   Database: appl_store" -ForegroundColor White
Write-Host "   Port: 8080" -ForegroundColor White
Write-Host "   Profile: default" -ForegroundColor White
Write-Host ""

Write-Host "🌐 URLs after startup:" -ForegroundColor Yellow
Write-Host "   Application: http://localhost:8080" -ForegroundColor Cyan
Write-Host "   Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "   API Docs: http://localhost:8080/v3/api-docs" -ForegroundColor Cyan
Write-Host ""

Write-Host "🔐 Test Accounts:" -ForegroundColor Yellow
Write-Host "   Admin: admin@applestore.com / admin@12345" -ForegroundColor White
Write-Host "   User:  user@example.com / user@123345" -ForegroundColor White
Write-Host ""

Write-Host "Starting application..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

# Run the application
try {
    java -jar target/apple-store-backend-1.0.0.jar
} catch {
    Write-Host "Error starting application: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "💡 Troubleshooting:" -ForegroundColor Yellow
    Write-Host "1. Make sure MySQL is running" -ForegroundColor White
    Write-Host "2. Check database password is correct" -ForegroundColor White
    Write-Host "3. Verify database 'appl_store' exists" -ForegroundColor White
    Write-Host "4. Run: mvn clean package to rebuild" -ForegroundColor White
}
