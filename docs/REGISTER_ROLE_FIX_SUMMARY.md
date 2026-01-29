# Registration Role Requirement Fix - Summary

## Date: December 10, 2025

## Problem
- Registration API required `role` field as mandatory (`@NotBlank`)
- Users couldn't register without providing a role
- Security risk: users could self-register as ADMIN

## Changes Made

### 1. RegisterRequestDTO.java
**File**: `src/main/java/com/demo/backend/dto/request/RegisterRequestDTO.java`

**Changed:**
```java
// BEFORE
@NotBlank
private String role;   // <-- "ADMIN" or "USER"

// AFTER
// Optional: if not provided, defaults to CUSTOMER role
private String role;
```

**Impact**: Role field is now optional during registration.

---

### 2. AuthServiceImpl.java
**File**: `src/main/java/com/demo/backend/service/impl/AuthServiceImpl.java`

**Changed**: Updated `register()` method to:
- Default to `CUSTOMER` role if no role provided
- Validate role value and prevent invalid roles
- **Security**: Block self-registration as `ADMIN` role
- Handle `IllegalArgumentException` for invalid role strings

**Code Logic:**
```java
UserRole assignedRole = UserRole.CUSTOMER; // Default role

if (req.getRole() != null && !req.getRole().isBlank()) {
    try {
        UserRole requestedRole = UserRole.valueOf(req.getRole().toUpperCase());
        // Security: Prevent self-registration as ADMIN
        if (requestedRole != UserRole.ADMIN) {
            assignedRole = requestedRole;
        }
    } catch (IllegalArgumentException e) {
        // Invalid role provided, use default CUSTOMER
    }
}

user.setRole(assignedRole);
```

**Impact**: 
- Users can register without specifying a role → defaults to CUSTOMER
- Users can register as CUSTOMER or EMPLOYEE
- Users cannot self-register as ADMIN (security protection)
- Invalid role values are handled gracefully

---

## Available User Roles
Based on `UserRole` enum:
- `CUSTOMER` (default for registration)
- `EMPLOYEE` (can be specified)
- `ADMIN` (cannot be self-assigned, admin-only)

---

## API Usage

### Register without role (recommended for public registration)
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123"
}
```
Result: User registered with `CUSTOMER` role

### Register with role (optional)
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123",
  "role": "EMPLOYEE"
}
```
Result: User registered with `EMPLOYEE` role

### Attempt to register as ADMIN (blocked)
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```
Result: User registered with `CUSTOMER` role (ADMIN request silently ignored for security)

---

## Security Configuration
**File**: `src/main/java/com/demo/backend/security/SecurityConfig.java`

Already properly configured:
- `/api/auth/**` endpoints are public (permitAll)
- Swagger endpoints are public:
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
  - `/swagger-ui.html`

No changes needed to SecurityConfig.

---

## Swagger Configuration
**File**: `src/main/java/com/demo/backend/config/SwaggerConfig.java`

Already properly configured with:
- JWT Bearer authentication scheme
- OpenAPI 3.0 documentation

**Version**: SpringDoc OpenAPI 2.6.0 (correct for Spring Boot 3.x)

No changes needed to SwaggerConfig.

---

## Testing

### Build Status
✅ Compilation successful
✅ No errors in RegisterRequestDTO
✅ No errors in AuthServiceImpl
✅ Package built successfully

### What to Test
1. **Register without role**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123"}'
   ```
   Expected: User created with CUSTOMER role

2. **Access Swagger UI**:
   - URL: http://localhost:8080/swagger-ui/index.html
   - Expected: Swagger UI loads without 403 error
   - Expected: `/v3/api-docs` accessible without authentication

3. **Access API Docs JSON**:
   - URL: http://localhost:8080/v3/api-docs
   - Expected: JSON response with OpenAPI schema
   - Expected: No 403 error

---

## Common Issues & Solutions

### Issue: 403 Forbidden on /v3/api-docs
**Cause**: SecurityConfig blocking Swagger endpoints
**Solution**: Already fixed - endpoints are in `permitAll()` list

### Issue: 500 Error when opening Swagger
**Possible Causes**:
1. Circular reference in DTOs
2. Invalid Lombok annotations
3. Wrong SpringDoc version
4. Model validation errors

**Solution**: Check application logs for exact error

### Issue: Port 8080 already in use
**Solution**: 
```powershell
taskkill /F /IM java.exe
```

---

## Summary of Benefits

✅ **User-friendly**: Registration no longer requires role field
✅ **Secure**: Prevents self-registration as ADMIN
✅ **Flexible**: Allows CUSTOMER or EMPLOYEE registration
✅ **Robust**: Handles invalid role values gracefully
✅ **Backward compatible**: Still accepts role field if provided
✅ **No breaking changes**: Existing API calls with role still work

---

## Files Modified
1. `src/main/java/com/demo/backend/dto/request/RegisterRequestDTO.java`
2. `src/main/java/com/demo/backend/service/impl/AuthServiceImpl.java`

## Files Verified (No Changes Needed)
1. `src/main/java/com/demo/backend/security/SecurityConfig.java`
2. `src/main/java/com/demo/backend/config/SwaggerConfig.java`
3. `pom.xml` (SpringDoc version 2.6.0 is correct)

---

## Next Steps

1. ✅ Code changes completed
2. ✅ Compilation successful
3. ✅ Package built
4. ⚠️ Start application: `java -jar target/apple-store-backend-1.0.0.jar`
5. ⚠️ Test registration endpoint
6. ⚠️ Verify Swagger UI access at http://localhost:8080/swagger-ui/index.html
7. ⚠️ Test with frontend or Postman

---

## Contact
For issues or questions, check the application logs when starting the server.

