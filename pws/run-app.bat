@echo off
set DB_PASSWORD=Iloveyouforever@096
echo Starting Apple Store Backend Application...
echo Database: appl_store
echo Port: 8080
echo Swagger UI: http://localhost:8080/swagger-ui/index.html
echo.
java -jar target/apple-store-backend-1.0.0.jar
pause

