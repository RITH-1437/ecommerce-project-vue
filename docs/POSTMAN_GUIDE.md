### Postman quick start for Apple Store backend

Files provided in this repo:

- documents/postman_collection.json — the requests
- documents/postman_environment.json — the environment with variables

Steps

1) Import both files into Postman (Import -> Files).
2) Select the "Apple Store Local" environment.
3) Ensure the backend is running. By default we set baseUrl to http://localhost:8080.
   - Note: this project currently uses server.port=0 (random) in application.properties. If the app starts on a random port, update the baseUrl variable in the environment to match the actual port printed in logs (or change server.port to a fixed value like 8080 and restart).
4) Register (Auth -> Register) using the sample body or adjust email before re-running.
5) Login (Auth -> Login). The collection test script will save accessToken to the Postman environment variable automatically.
6) Call public endpoints (Products -> List / Get by ID / Get by Slug).
7) Call protected admin endpoints (e.g., Products -> Create Product). The Authorization header is already set to "Bearer {{accessToken}}". You need a user with ADMIN role for these endpoints to succeed.

Refresh token & cookies

- The backend stores the refresh token in an HttpOnly, Secure cookie during login. To test /api/auth/refresh in Postman, enable "Automatically follow redirects" and ensure Postman sends/receives cookies for your baseUrl. On localhost over HTTP, Secure cookies may not be sent by some clients; use HTTPS or temporarily relax cookie settings in code if necessary.

Swagger UI

- You can also explore and test endpoints via Swagger: http://localhost:8080/swagger-ui.html (adjust port to the actual one).

Sample payloads

- Register: { "name": "Test User", "email": "test.user@example.com", "password": "P@ssw0rd!" }
- Login: { "email": "test.user@example.com", "password": "P@ssw0rd!" }
- Create Product (ADMIN): { "name": "Sample Product", "slug": "sample-product", "description": "Short description", "price": 999.99, "stock": 5, "categoryId": 1 }
