# Run Spring Boot Application with proper environment setup
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Apple Store Backend Application" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set environment variables
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password_here"
$env:SPRING_PROFILES_ACTIVE="default"
$env:JWT_SECRET="your_jwt_secret_key_here_at_least_256_bits"
# $env:GOOGLE_CLIENT_ID="your_google_client_id_here.apps.googleusercontent.com"
# $env:GOOGLE_CLIENT_SECRET="your_google_client_secret_here"
$env:FRONTEND_URL="http://localhost:3000"
$env:PAYWAY_MERCHANT_ID="your_payway_merchant_id"
$env:PAYWAY_API_KEY="your_payway_api_key"
$env:PAYWAY_SECRET_KEY="your_payway_secret_key"
$env:PAYWAY_CALLBACK_URL="http://localhost:8080/api/payments/callback"
$env:PAYWAY_RETURN_URL="http://localhost:3000/payment/success"
$env:MAIL_USERNAME="your_email@gmail.com"
$env:MAIL_PASSWORD="your_app_password"
$env:ALERT_EMAIL_FROM="noreply@applestore.com"
$env:ALERT_ADMIN_EMAIL="admin@applestore.com"
# $env:OPENAI_API_KEY="sk-proj-your_openai_api_key_here"

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
