# How to Run the Application

## ✅ FIXED ISSUES:

### 1. **Random Port Issue (FIXED)**
- Changed `server.port=0` to `server.port=8080`
- Your app now runs on a **fixed port 8080** instead of random port

### 2. **Swagger Security Configuration (FIXED)**
- Added all necessary Swagger UI paths to SecurityConfig (permitAll)
- Added `/webjars/**` path for Swagger static resources
- Added `/api/payments/aba/callback` for payment callbacks
- Swagger is now publicly accessible without authentication

### 3. **Rate Limiting Filter (FIXED)**
- Added `shouldNotFilter()` method to exclude Swagger paths from rate limiting
- Prevents "429 Too Many Requests" errors when accessing Swagger UI

### 4. **CORS Configuration (FIXED)**
- Disabled CORS in SecurityConfig to allow Swagger UI to load resources
- Prevents cross-origin errors

### 5. **Application Properties (FIXED)**
- Removed trailing spaces
- Fixed debug flag formatting

---

## 🚀 HOW TO RUN IN INTELLIJ IDEA:

### Method 1: Using the Run Button (Recommended)
1. Open the project in IntelliJ IDEA
2. Find `BackendApplication.java` in the Project Explorer
3. Right-click on the file (or the class) → Select **"Run 'BackendApplication'"**
4. **Look at the bottom of the screen** for the **"Run" tool window** (NOT Terminal)
5. You'll see logs like:
   ```
   Tomcat started on port(s): 8080 (http)
   Started BackendApplication in X.XXX seconds
   ```

### Method 2: Using Maven
Open Terminal in IntelliJ (View → Tool Windows → Terminal) and run:
```powershell
.\mvnw.cmd spring-boot:run
```

### Method 3: From Command Line (Outside IntelliJ)
```powershell
cd "D:\YEAR_4_Documents\YEAR_4_Documents\Semester_I\IPI (Internet Programming I)\ecommerce-project\backend"
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

---

## 📚 ACCESSING SWAGGER UI:

Once the application is running, access Swagger at:

**Swagger UI:** http://localhost:8080/swagger-ui/index.html

**OpenAPI Docs (JSON):** http://localhost:8080/v3/api-docs

**Alternative Swagger URL:** http://localhost:8080/swagger-ui.html

---

## 🔍 WHERE TO SEE LOGS (NOT TERMINAL):

**IMPORTANT:** When you run a Spring Boot app in IntelliJ, logs appear in the **"Run" tool window**, NOT the "Terminal" window!

### To View Logs:
1. After clicking Run, look at the **bottom** of your screen
2. Click on the **"Run"** tab (it has a green play icon)
3. You'll see all application logs there
4. Look for messages like:
   - `Tomcat started on port(s): 8080`
   - `Started BackendApplication in X.XXX seconds`

### Terminal vs Run Window:
- **Terminal**: For manually typing commands (like `mvnw spring-boot:run`)
- **Run Window**: Shows output when you use IntelliJ's Run button

---

## ⚙️ PREREQUISITES:

Before running, make sure:

1. **MySQL is running** on `localhost:3306`
2. **Database exists**: `apple_store`
3. **MySQL credentials** are correct:
   - Username: `root`
   - Password: Set via environment variable `DB_PASSWORD` or uses default from application.properties

To create the database:
```sql
CREATE DATABASE IF NOT EXISTS apple_store;
```

---

## 🐛 TROUBLESHOOTING:

### Problem: "No terminal running when I run application"
**Solution:** This is NORMAL! IntelliJ doesn't use the Terminal window for the Run button. Check the **"Run" tool window** at the bottom of the screen instead.

### Problem: Can't access Swagger UI
**Solution:** 
1. Make sure app is running (check Run window for "Started BackendApplication")
2. Use the correct URL: http://localhost:8080/swagger-ui/index.html
3. Try alternative URL: http://localhost:8080/swagger-ui.html
4. Clear browser cache (Ctrl+F5)
5. Try in incognito/private browsing mode

### Problem: Swagger shows "Unauthorized" or 403 Forbidden
**Solution (FIXED):**
- ✅ SecurityConfig now permits all Swagger paths
- ✅ JwtAuthenticationFilter allows requests without Bearer token
- ✅ RateLimitFilter excludes Swagger paths
- Just refresh the page after the fixes

### Problem: Swagger UI shows "Failed to load API definition" or "TypeError"
**Solution (FIXED):**
- ✅ Added `/webjars/**` to security permitAll (for Swagger CSS/JS resources)
- ✅ Disabled CORS to allow resource loading
- ✅ All static resources now accessible
- Clear browser cache and reload

### Problem: "429 Too Many Requests" error on Swagger
**Solution (FIXED):**
- ✅ RateLimitFilter now skips Swagger paths
- Swagger UI won't be rate-limited anymore

### Problem: Port already in use
**Solution:** Another app is using port 8080. Either:
- Stop the other application
- Change `server.port=8080` to another port (e.g., `8081`, `9090`)

### Problem: MySQL connection error
**Solution:**
1. Start MySQL service
2. Check database exists: `apple_store`
3. Verify credentials in `application.properties`

### Problem: Cannot resolve configuration property warnings
**Solution:** These are just warnings (not errors). The app will run fine. To fix:
- These properties are custom properties used in your code
- They work at runtime but IntelliJ can't validate them
- You can ignore these warnings

---

## 📝 TESTING THE API:

### Using Swagger UI (Easiest):
1. Go to http://localhost:8080/swagger-ui/index.html
2. Expand any endpoint
3. Click "Try it out"
4. Fill in parameters
5. Click "Execute"

### Using Postman/Thunder Client:
- Base URL: `http://localhost:8080`
- Public endpoints: `/api/auth/**`, `/api/public/**`
- Protected endpoints: Need JWT token in Authorization header

---

## 🎯 QUICK START CHECKLIST:

- [x] Fixed random port → Now using port 8080
- [x] Fixed Swagger security → All Swagger paths now public
- [x] Fixed application.properties formatting
- [ ] Ensure MySQL is running
- [ ] Run the application using Run button in IntelliJ
- [ ] Check "Run" window (bottom of screen) for logs
- [ ] Access Swagger: http://localhost:8080/swagger-ui/index.html

---

**Your application is now ready to run! Just click the green Run button in IntelliJ and check the Run window for logs.** 🚀

