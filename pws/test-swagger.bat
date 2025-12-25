@echo off
REM Test Swagger Endpoints Accessibility
echo ================================================
echo Testing Swagger UI Accessibility
echo ================================================
echo.

echo Starting tests in 3 seconds...
timeout /t 3 /nobreak >nul

echo.
echo [1] Testing Swagger UI HTML page...
curl -s -o nul -w "Status Code: %%{http_code}\n" http://localhost:8080/swagger-ui/index.html
echo.

echo [2] Testing OpenAPI docs JSON...
curl -s -o nul -w "Status Code: %%{http_code}\n" http://localhost:8080/v3/api-docs
echo.

echo [3] Testing alternative Swagger URL...
curl -s -o nul -w "Status Code: %%{http_code}\n" http://localhost:8080/swagger-ui.html
echo.

echo ================================================
echo Expected Results:
echo - Status Code 200 = SUCCESS (Swagger is accessible)
echo - Status Code 401 or 403 = FAILED (Security blocking)
echo - Status Code 404 = App running but Swagger not found
echo - Connection failed = App not running on port 8080
echo ================================================
echo.

echo Opening Swagger UI in browser...
start http://localhost:8080/swagger-ui/index.html

pause

