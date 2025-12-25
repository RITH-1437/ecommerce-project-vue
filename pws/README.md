# PowerShell and Batch Scripts (pws)

This folder contains all PowerShell (.ps1) and batch (.bat) scripts for the Apple Store Backend project.

## 🚀 Application Scripts

### `run-app.ps1`
- **Purpose**: Start the Spring Boot application with proper environment setup
- **Usage**: `.\run-app.ps1`
- **Features**: Sets DB password, displays startup info, shows URLs and test accounts

### `run-app.bat`
- **Purpose**: Batch file version to start the application
- **Usage**: `.\run-app.bat`
- **Features**: Simple startup script for Windows Command Prompt

## 🧪 Testing Scripts

### `test-app-fixed.ps1`
- **Purpose**: Comprehensive application testing (main test script)
- **Usage**: `.\test-app-fixed.ps1`
- **Features**: 
  - Starts application if not running
  - Tests Swagger API docs
  - Tests Swagger UI
  - Tests user registration
  - Provides detailed status reports

### `simple-test.ps1`
- **Purpose**: Basic testing for Swagger and registration
- **Usage**: `.\simple-test.ps1`
- **Features**: Quick verification of core functionality

### `test-application.ps1`
- **Purpose**: Alternative testing script
- **Usage**: `.\test-application.ps1`
- **Features**: Similar to test-app-fixed.ps1 with different approach

### `test-swagger-access.ps1`
- **Purpose**: Specifically test Swagger endpoints
- **Usage**: `.\test-swagger-access.ps1`
- **Features**: Focus on Swagger UI and API docs access

### `test-registration.ps1`
- **Purpose**: Test user registration functionality
- **Usage**: `.\test-registration.ps1`
- **Features**: Tests registration with and without roles

### `test-swagger.bat`
- **Purpose**: Batch file version of Swagger testing
- **Usage**: `.\test-swagger.bat`
- **Features**: Command prompt compatible Swagger testing

## 📋 Usage Instructions

### Before Running Scripts
1. Make sure MySQL is running on port 3306
2. Database `appl_store` should exist
3. Application should be built: `mvn clean package`

### Setting Execution Policy (if needed)
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Quick Start
```powershell
# Start the application
.\run-app.ps1

# Test everything (in another terminal)
.\test-app-fixed.ps1
```

## 🔧 Environment Variables

The scripts use the following environment variable:
- `DB_PASSWORD`: MySQL database password (set to: Iloveyouforever@096)

## 📊 Expected Outputs

### Successful Application Start
- Application starts on port 8080
- Swagger UI accessible at: http://localhost:8080/swagger-ui/index.html
- API docs at: http://localhost:8080/v3/api-docs

### Test Account Information
- **Admin**: admin@applestore.com / admin@12345
- **User**: user@example.com / user@123345

## 🆘 Troubleshooting

If scripts fail to run:
1. Check PowerShell execution policy
2. Ensure you're in the correct directory
3. Verify MySQL is running
4. Check if port 8080 is available
5. Verify the JAR file exists in target/ folder

## 📁 File Organization

All scripts are organized in this `pws` folder to keep the main project directory clean while providing easy access to development and testing tools.
