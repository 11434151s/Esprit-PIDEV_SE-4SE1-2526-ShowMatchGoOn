# API Testing Guide - Swagger UI

## 🌐 Accessing Swagger UI

Open in your browser:
```
http://localhost:8090/swagger-ui.html
```

## 🔒 Authentication with Swagger

### Step 1: Create Sample User
1. Go to `/api/auth` section if available
2. Or use a REST client to create a user with POST request

### Step 2: Add Credentials to Swagger
1. Look for the green "Authorize" button at the top of Swagger UI
2. Provide credentials in format: `username:password` (in Base64 encoding)
3. Click "Authorize"

### Three Ways to Test Endpoints:

#### Method 1: Using Browser (No Auth Required)
Public endpoints don't require authentication:
- Swagger UI (`/swagger-ui.html`) - Public
- API Docs (`/v3/api-docs`) - Public

#### Method 2: Using Swagger's "Try it out"
1. Click on any endpoint in Swagger
2. Click "Try it out" button
3. Provide request body (for POST/PUT)
4. Click "Execute"
5. View response below

#### Method 3: Using Authorization Header
If endpoints show lock icon:
1. Click "Authorize" button
2. Enter `Basic <base64-encoded-credentials>`
3. Example: `Basic dXNlcm5hbWU6cGFzc3dvcmQ=`

## 📊 Available Endpoints

### Categories API (`/api/categories`)
```
POST   /api/categories                 - Create category [Protected]
GET    /api/categories                 - Get all categories [Protected]
GET    /api/categories/{id}            - Get category by ID [Protected]
PUT    /api/categories/{id}            - Update category [Protected]
DELETE /api/categories/{id}            - Delete category [Protected - ADMIN]
```

**Example POST Body:**
```json
{
  "name": "Action"
}
```

### Content API (`/api/contents`)
```
POST   /api/contents/films             - Create film [Protected]
POST   /api/contents/series            - Create series [Protected]
POST   /api/contents/documentaries     - Create documentary [Protected]
GET    /api/contents                   - Get all content [Protected]
GET    /api/contents/{id}              - Get content by ID [Protected]
PUT    /api/contents/films/{id}        - Update film [Protected]
PUT    /api/contents/series/{id}       - Update series [Protected]
PUT    /api/contents/documentaries/{id} - Update documentary [Protected]
DELETE /api/contents/{id}              - Delete content [Protected - ADMIN]
```

**Example Film POST Body:**
```json
{
  "title": "The Matrix",
  "description": "A sci-fi action film",
  "releaseDate": "1999-03-31T00:00:00",
  "categoryId": "<category-id>",
  "durationInMinutes": 136,
  "director": "Wachowski Sisters"
}
```

**Example Series POST Body:**
```json
{
  "title": "Breaking Bad",
  "description": "Drama series about a chemistry teacher",
  "releaseDate": "2008-01-20T00:00:00",
  "categoryId": "<category-id>",
  "numberOfSeasons": 5,
  "numberOfEpisodes": 62,
  "isCompleted": true
}
```

**Example Documentary POST Body:**
```json
{
  "title": "Planet Earth",
  "description": "Documentary about the natural world",
  "releaseDate": "2016-11-01T00:00:00",
  "categoryId": "<category-id>",
  "topic": "Nature and Wildlife",
  "narrator": "David Attenborough"
}
```

### Notifications API (`/api/notifications`)
```
POST   /api/notifications              - Create notification [Protected]
GET    /api/notifications/{userId}     - Get notifications by user [Protected]
PUT    /api/notifications/{id}/read    - Mark as read [Protected]
DELETE /api/notifications/{id}         - Delete notification [Protected]
```

**Example POST Body:**
```json
{
  "message": "New content added to your favorite category",
  "type": "INTERNAL",
  "userId": "<user-id>"
}
```

## ✅ Response Examples

### Successful Create (201 Created)
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Action",
  "contentIds": []
}
```

### Validation Error (400 Bad Request)
```json
{
  "timestamp": "2026-03-02T22:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "errors": {
    "name": "Category name must be between 2 and 50 characters"
  }
}
```

### Not Found (404)
```json
{
  "timestamp": "2026-03-02T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Category not found with id: 123"
}
```

### Unauthorized (401)
```json
{
  "timestamp": "2026-03-02T22:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed: Bad credentials"
}
```

## 🧪 Sample Testing Workflow

### 1. Create a Category
```
POST /api/categories
Body: {
  "name": "Drama"
}
Response: Category ID (save this)
```

### 2. Create Content
```
POST /api/contents/films
Body: {
  "title": "Inception",
  "description": "A mind-bending thriller",
  "categoryId": "<saved-category-id>",
  "durationInMinutes": 148,
  "director": "Christopher Nolan"
}
Response: Film details with ID
```

### 3. Retrieve Content
```
GET /api/contents/<film-id>
Response: Film details
```

### 4. Update Content
```
PUT /api/contents/films/<film-id>
Body: {
  "title": "Inception (Updated)",
  "description": "Updated description",
  "categoryId": "<category-id>",
  "durationInMinutes": 150,
  "director": "Christopher Nolan"
}
```

### 5. Delete Content
```
DELETE /api/contents/<film-id>
Response: 204 No Content
```

## 🔍 Validation Rules to Test

### Category Name
- ❌ Too short: "A"
- ❌ Too long: "This is a very long category name that exceeds fifty characters"
- ✅ Valid: "Action" (2-50 chars)

### Film Duration
- ❌ Zero or negative: 0, -100
- ✅ Valid: 120

### Series Episodes
- ❌ Without numberOfSeasons
- ❌ Zero episodes: 0
- ✅ Valid: numberOfSeasons: 5, numberOfEpisodes: 50

### Notification Type
- ❌ Invalid: "PUSH", "TELEGRAM"
- ✅ Valid: "INTERNAL", "EMAIL", "SMS"

## 📝 Notes

- All timestamps should be in ISO 8601 format: `YYYY-MM-DDTHH:mm:ss`
- All IDs are MongoDB ObjectIDs (string format)
- Authentication uses HTTP Basic Auth
- Protected endpoints require valid user credentials
- Admin operations (DELETE) may require ADMIN role

## 🐛 Troubleshooting

**"401 Unauthorized"**
- Ensure you've clicked "Authorize" button
- Check credentials format is correct
- Base64 encode credentials properly

**"400 Validation Failed"**
- Check all required fields are present
- Verify field values match validation rules
- Check string lengths and patterns

**"404 Not Found"**
- Verify the ID exists
- Check the collection (films/series/documentaries)
- Ensure you're using correct endpoint path

**CORS Issues**
- API is configured for local testing
- Configure CORS if accessing from different port
- Update `SecurityConfig` if needed

---

**For API Documentation**, visit:
```
http://localhost:8090/v3/api-docs
```

This provides the raw OpenAPI 3.0 specification.
