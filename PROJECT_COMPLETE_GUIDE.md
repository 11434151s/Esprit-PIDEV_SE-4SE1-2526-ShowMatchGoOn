# SMGO Project - Complete Educational Guide

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
4. [Input Validation Rules (Contrôle de Saisie)](#input-validation-rules)
5. [Spring Security Implementation](#spring-security-implementation)
6. [Frontend Validation](#frontend-validation)
7. [Implementation Steps](#implementation-steps)
8. [Code Examples](#code-examples)

---

# Project Overview

## What is SMGO?
SMGO (Show Match Go On) is a **Content Management System** for streaming content. It allows users to manage:
- **Films** (movies)
- **Series** (TV shows)
- **Documentaries**
- **Categories** (genres)
- **Notifications** (user alerts)
- **Users** (with role-based access control)

## Technology Stack

### Backend
- **Framework:** Spring Boot 3.2.3
- **Language:** Java 17
- **Database:** MongoDB (NoSQL)
- **Security:** Spring Security (JWT/BasicAuth)
- **API:** RESTful HTTP APIs

### Frontend
- **Framework:** Angular 18.2
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **UI Components:** Lucide Icons

---

# Architecture

## Backend Structure

```
backend/
├── src/main/java/com/example/contentmanagement/
│   ├── controller/          # API endpoints
│   ├── service/             # Business logic
│   ├── entity/              # Database models
│   ├── dto/                 # Data Transfer Objects
│   ├── repository/          # Database access
│   ├── security/            # Security config & services
│   ├── config/              # Spring configurations
│   └── exception/           # Custom exceptions
├── resources/
│   └── application.properties  # Configuration
└── pom.xml                  # Maven dependencies
```

## Frontend Structure

```
frontend/
├── src/app/
│   ├── components/          # UI components
│   │   ├── admin-content/
│   │   ├── admin-cinema/
│   │   └── admin-notifications/
│   ├── services/            # API & validation services
│   │   ├── api.service.ts
│   │   └── validators.ts
│   ├── app.config.ts        # Main configuration
│   └── app.routes.ts        # Routing
└── package.json             # NPM dependencies
```

---

# Data Transfer Objects (DTOs)

## What is a DTO?

**DTO stands for Data Transfer Object.** It's a class used to transfer data between different layers of your application.

### Why Use DTOs?

1. **Separation of Concerns** - Separates database models from API responses
2. **Validation** - Validates data before processing
3. **Security** - Only sends necessary data to frontend
4. **Flexibility** - Can transform data as needed

### How DTOs Work

```
User Input (Frontend)
        ↓
HTTP Request with JSON
        ↓
DTO receives data
        ↓
Validators check data
        ↓
Service processes valid data
        ↓
Entity saves to database
        ↓
DTO returns response to Frontend
```

## DTO Hierarchy

```
ContentDTO (Base/Parent)
├── FilmDTO
├── SeriesDTO
└── DocumentaryDTO
```

---

## 1. ContentDTO - Base Class

This is the **parent class** for all content types. All content (films, series, documentaries) share these fields.

### Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `id` | String | None | Unique identifier |
| `title` | String | Required, 1-255 chars | Content name |
| `description` | String | Max 1000 chars | Content summary |
| `releaseDate` | LocalDateTime | None | Release date |
| `categoryId` | String | Required (Not Null) | Category reference |
| `categoryName` | String | None | Category display name |
| `contentType` | String | FILM\|SERIES\|DOCUMENTARY | Type of content |
| `addedById` | String | None | Creator user ID |
| `addedByUsername` | String | None | Creator username |

### Example Java Code

```java
// Create a ContentDTO
ContentDTO film = new FilmDTO();
film.setTitle("Inception");
film.setDescription("A sci-fi thriller about dreams");
film.setReleaseDate(LocalDateTime.now());
film.setCategoryId("action-001");
film.setContentType("FILM");
```

### Validation Annotations Explained

| Annotation | What it Does | Example |
|------------|-------------|---------|
| `@NotBlank` | Field must not be empty or whitespace | `@NotBlank(message = "Title is mandatory")` |
| `@Size` | Length must be within range | `@Size(min = 1, max = 255)` |
| `@Pattern` | Must match regex pattern | `@Pattern(regexp = "FILM\|SERIES")` |
| `@NotNull` | Field must not be null | `@NotNull(message = "Category required")` |

---

## 2. FilmDTO - For Movies

**Extends:** ContentDTO

### Additional Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `durationInMinutes` | Integer | Positive number | How long the movie is |
| `director` | String | Required | Who directed the film |

### Complete Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDTO extends ContentDTO {
    
    @Positive(message = "Duration must be positive")
    private Integer durationInMinutes;  // Example: 120
    
    @NotBlank(message = "Director is mandatory")
    private String director;  // Example: "Christopher Nolan"
}
```

### Creating a Film DTO Example

```java
FilmDTO film = FilmDTO.builder()
    .id("film-001")
    .title("Inception")
    .description("A mind-bending sci-fi thriller")
    .durationInMinutes(148)  // 2 hours 28 minutes
    .director("Christopher Nolan")
    .categoryId("sci-fi")
    .contentType("FILM")
    .releaseDate(LocalDateTime.of(2010, 7, 16, 0, 0))
    .build();
```

---

## 3. SeriesDTO - For TV Shows

**Extends:** ContentDTO

### Additional Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `numberOfSeasons` | Integer | Positive, required | How many seasons |
| `numberOfEpisodes` | Integer | Positive, required | Total episodes |
| `isCompleted` | Boolean | Optional | Is the series finished |

### Complete Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesDTO extends ContentDTO {
    
    @NotNull(message = "Number of seasons is mandatory")
    @Positive(message = "Number of seasons must be positive")
    private Integer numberOfSeasons;  // Example: 5
    
    @NotNull(message = "Number of episodes is mandatory")
    @Positive(message = "Number of episodes must be positive")
    private Integer numberOfEpisodes;  // Example: 65
    
    private Boolean isCompleted;  // Example: true
}
```

### Creating a Series DTO Example

```java
SeriesDTO series = SeriesDTO.builder()
    .id("series-001")
    .title("Breaking Bad")
    .description("A chemistry teacher turns to crime")
    .numberOfSeasons(5)
    .numberOfEpisodes(62)
    .isCompleted(true)
    .categoryId("crime-drama")
    .contentType("SERIES")
    .releaseDate(LocalDateTime.of(2008, 1, 20, 0, 0))
    .build();
```

---

## 4. DocumentaryDTO - For Documentaries

**Extends:** ContentDTO

### Additional Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `duration` | Integer | None | Length in minutes |
| `director` | String | None | Documentary filmmaker |

### Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentaryDTO extends ContentDTO {
    private Integer duration;
    private String director;
}
```

---

## 5. UserDTO - For User Management

**Independent DTO** (not extending ContentDTO)

### Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `id` | String | None | User ID |
| `username` | String | Required, 3-50 chars | Login name |
| `email` | String | Required, valid email | Email address |

### Complete Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    
    private String id;
    
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;  // Example: "john_doe"
    
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;  // Example: "john@example.com"
    
    private String contentType;  // Response only
}
```

---

## 6. CategoryDTO - For Content Categories

### Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `id` | String | None | Category ID |
| `name` | String | Required | Category name |
| `description` | String | Required | Category details |

### Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private String id;
    
    @NotBlank(message = "Name is mandatory")
    private String name;  // Example: "Action"
    
    @NotBlank(message = "Description is mandatory")
    private String description;  // Example: "Fast-paced action movies"
}
```

---

## 7. NotificationDTO - For Alerts

### Fields

| Field | Type | Validation | Purpose |
|-------|------|-----------|---------|
| `id` | String | None | Notification ID |
| `title` | String | Required | Notification title |
| `message` | String | Required | Full message |
| `read` | Boolean | None | Read status |

### Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;
    
    @NotBlank(message = "Title is mandatory")
    private String title;  // Example: "New Content Added"
    
    @NotBlank(message = "Message is mandatory")
    private String message;  // Example: "The new Inception movie is now available"
    
    private Boolean read;  // Example: false
}
```

---

# Input Validation Rules

## What is Validation?

**Validation** is checking that data meets requirements before processing it.

### Validation Flow

```
Frontend                Backend                Database
   ↓                      ↓                       ↓
User Input → Validates → Sends to Server → DTO Validates → Saves
```

## Frontend Validation (Angular)

Validates in the browser **before sending** to server.

### Custom Validators in TypeScript

**File:** `src/app/services/validators.ts`

---

## All Frontend Validators

### 1. Text Validation

```typescript
// Required field
static required(control: AbstractControl): ValidationErrors | null {
    if (!control.value || (typeof control.value === 'string' && !control.value.trim())) {
        return { required: true };
    }
    return null;
}
```

**Usage Example:**
```typescript
this.form = this.fb.group({
    title: ['', [CustomValidators.required]]  // Cannot be empty
});
```

---

### 2. Length Validation

#### Minimum Length
```typescript
static minLength(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        if (control.value.length < min) {
            return { minlength: { requiredLength: min, actualLength: control.value.length } };
        }
        return null;
    };
}
```

**Usage:**
```typescript
title: ['', [CustomValidators.minLength(3)]]  // At least 3 characters
// Error if: "ab" (only 2 chars)
// OK if: "abc" (3 chars)
```

#### Maximum Length
```typescript
static maxLength(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        if (control.value.length > max) {
            return { maxlength: { requiredLength: max, actualLength: control.value.length } };
        }
        return null;
    };
}
```

**Usage:**
```typescript
title: ['', [CustomValidators.maxLength(255)]]  // Max 255 characters
// Error if: very long text with 300+ chars
// OK if: "My awesome movie title" (21 chars)
```

---

### 3. Number Validation

#### Is Number Check
```typescript
static numeric(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const value = Number(control.value);
    if (isNaN(value)) {
        return { numeric: true };  // Not a valid number
    }
    return null;
}
```

**Usage:**
```typescript
duration: ['', [CustomValidators.numeric]]
// Error if: "abc" or "12.5x"
// OK if: "120" or "12"
```

#### Minimum Value
```typescript
static minValue(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        const value = Number(control.value);
        if (isNaN(value) || value < min) {
            return { minvalue: { min, actual: value } };
        }
        return null;
    };
}
```

**Usage:**
```typescript
duration: ['', [CustomValidators.minValue(1)]]
// Error if: "0" or negative number
// OK if: "120"
```

#### Maximum Value
```typescript
static maxValue(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        const value = Number(control.value);
        if (isNaN(value) || value > max) {
            return { maxvalue: { max, actual: value } };
        }
        return null;
    };
}
```

**Usage:**
```typescript
rating: ['', [CustomValidators.maxValue(5)]]
// Error if: "6" or higher
// OK if: "4.5"
```

---

### 4. Date Validation

#### Date Format Check
```typescript
static dateFormat(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;  // YYYY-MM-DD format
    if (!dateRegex.test(control.value)) {
        return { dateformat: true };
    }
    const date = new Date(control.value);
    if (isNaN(date.getTime())) {
        return { dateformat: true };
    }
    return null;
}
```

**Usage:**
```typescript
releaseDate: ['', [CustomValidators.dateFormat]]
// Error if: "15/03/2026" or "2026-3-15" or "2026-13-01"
// OK if: "2026-03-15"
```

#### No Past Dates
```typescript
static futureDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (selectedDate < today) {
        return { pastdate: true };  // Date is in the past
    }
    return null;
}
```

**Usage:**
```typescript
releaseDate: ['', [CustomValidators.futureDateValidator]]
// Error if: "2025-12-25" (in the past, today is March 2, 2026)
// OK if: "2026-03-15" (in the future)
```

#### No Future Dates
```typescript
static pastDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(23, 59, 59, 999);
    if (selectedDate > today) {
        return { futuredate: true };  // Date is in the future
    }
    return null;
}
```

**Usage:**
```typescript
releaseDate: ['', [CustomValidators.pastDateValidator]]
// Error if: "2026-05-01" (in the future)
// OK if: "2025-12-25" or "2026-03-01"
```

---

### 5. Format Validation

#### Email
```typescript
static email(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(control.value)) {
        return { email: true };
    }
    return null;
}
```

**Usage:**
```typescript
email: ['', [CustomValidators.email]]
// Error if: "john@.com", "john.com", "@example.com"
// OK if: "john@example.com"
```

#### URL
```typescript
static url(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    try {
        new URL(control.value);  // Try to create URL object
        return null;  // Valid URL
    } catch {
        return { url: true };  // Invalid URL
    }
}
```

**Usage:**
```typescript
website: ['', [CustomValidators.url]]
// Error if: "not-a-url", "htp://example.com"
// OK if: "https://example.com"
```

---

### 6. Pattern Validation

#### Custom Regex
```typescript
static pattern(regex: RegExp | string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        const regexObj = typeof regex === 'string' ? new RegExp(regex) : regex;
        if (!regexObj.test(control.value)) {
            return { pattern: true };
        }
        return null;
    };
}
```

**Usage Examples:**
```typescript
// Only alphanumeric
username: ['', [CustomValidators.pattern(/^[a-zA-Z0-9]+$/)]]

// Phone number (US format)
phone: ['', [CustomValidators.pattern(/^\d{3}-\d{3}-\d{4}$/)]]

// Postal code (5 digits)
zipCode: ['', [CustomValidators.pattern(/^\d{5}$/)]]
```

---

### 7. Matching Fields

```typescript
static match(fieldName: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.parent) return null;
        const matchControl = control.parent.get(fieldName);
        if (!matchControl) return null;
        if (control.value !== matchControl.value) {
            return { match: true };  // Fields don't match
        }
        return null;
    };
}
```

**Usage (for password confirmation):**
```typescript
this.form = this.fb.group({
    password: ['', [Validators.required]],
    confirmPassword: ['', [Validators.required, CustomValidators.match('password')]]
});

// Error if: password = "123456" but confirmPassword = "123457"
// OK if: both are "123456"
```

---

## Backend Validation (Java Spring)

Backend validates **after receiving** data from frontend.

### Validation Annotations in DTOs

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@NotBlank` | Cannot be empty string | Title field |
| `@NotNull` | Cannot be null | Category ID |
| `@Size(min, max)` | Length constraints | 1-255 characters |
| `@Positive` | Must be > 0 | Duration minutes |
| `@Email` | Valid email format | User email |
| `@Pattern` | Matches regex | Content type |

### How Backend Validation Works

```java
// In Controller
@PostMapping("/films")
public ResponseEntity<FilmDTO> createFilm(
    @Valid @RequestBody FilmDTO filmDTO  // @Valid triggers validation
) {
    // If any @NotBlank, @Size etc fails → 400 Bad Request
    // If valid → continues to service
    return new ResponseEntity<>(contentService.createFilm(filmDTO), HttpStatus.CREATED);
}
```

### Validation Error Response

When validation fails, backend returns **400 Bad Request** with error details:

```json
{
    "timestamp": "2026-03-02T15:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "details": {
        "title": "Title is mandatory",
        "duration": "Duration must be positive"
    }
}
```

---

## Validation Rules Summary - All DTOs

### ContentDTO (Base)
```
title:       Required, 1-255 characters
description: Max 1000 characters
categoryId:  Required (Not Null)
contentType: Must be FILM | SERIES | DOCUMENTARY
```

### FilmDTO
```
duration:    Must be > 0 (positive number)
director:    Required (not blank)
```

### SeriesDTO
```
numberOfSeasons:  Required, > 0
numberOfEpisodes: Required, > 0
isCompleted:      Optional boolean
```

### UserDTO
```
username:  Required, 3-50 characters
email:     Required, valid email format
```

### CategoryDTO
```
name:        Required
description: Required
```

### NotificationDTO
```
title:   Required
message: Required
```

---

# Spring Security Implementation

## What is Spring Security?

**Spring Security** is a framework that provides:
- **Authentication** - Verifies who you are (login)
- **Authorization** - Decides what you can do (permissions)
- **Protection** from common attacks (CSRF, SQL injection, etc.)

---

## Security Architecture

```
Browser Request
      ↓
   [CORS Filter]
      ↓
   [Authentication Filter] - Who are you?
      ↓
   [Authorization Filter] - What can you access?
      ↓
   [Controller] - Process request
      ↓
  Database
      ↓
   Response
```

---

## How Authentication Works

### Step 1: User Registration
```
User submits: username "john_doe", password "123456"
       ↓
Backend receives UserDTO with plain password
       ↓
Password Encoder (BCrypt) scrambles password
       ↓
User saved in database with scrambled password: "$2a$10$EixZaYVK1fsbw1..."
```

### Step 2: User Login
```
User submits: username "john_doe", password "123456"
       ↓
CustomUserDetailsService.loadUserByUsername() finds user
       ↓
BCrypt compares submitted password with stored scrambled password
       ↓
If match → Authentication successful
If no match → Authentication failed
```

### Step 3: Access Control
```
Authenticated user makes request to /api/contents
       ↓
@PreAuthorize decorator checks: does user have permission?
       ↓
If yes → Process request
If no → 403 Forbidden error
```

---

## SecurityConfig.java - Detailed Explanation

### File Location
```
src/main/java/com/example/contentmanagement/config/SecurityConfig.java
```

### Complete Code Breakdown

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
```

**What each annotation does:**

| Annotation | Purpose |
|-----------|---------|
| `@Configuration` | This is a Spring configuration class |
| `@EnableWebSecurity` | Enable Spring Security web features |
| `@EnableMethodSecurity` | Allow @PreAuthorize on methods |
| `securedEnabled = true` | Allow @Secured annotation |
| `jsr250Enabled = true` | Allow @RolesAllowed annotation |
| `prePostEnabled = true` | Allow @PreAuthorize & @PostAuthorize |
| `@RequiredArgsConstructor` | Auto-inject dependencies |

---

### Bean 1: Password Encoder

```java
@Bean
public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**What it does:**
- Creates a BCrypt password encoder
- Converts plain passwords to scrambled format
- **Not reversible** - you can't un-scramble

**Example:**
```
Plain:     "123456"
Encoded:   "$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lm"
```

**When used:**
```java
// During registration
String encodedPassword = passwordEncoder().encode("123456");
user.setPassword(encodedPassword);  // Save encoded version

// During login
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
boolean matches = encoder.matches("123456", storedEncodedPassword);
```

---

### Bean 2: Authentication Manager

```java
@Bean
public AuthenticationManager authenticationManager(
    AuthenticationConfiguration configuration
) throws Exception {
    return configuration.getAuthenticationManager();
}
```

**What it does:**
- Creates AuthenticationManager bean
- Responsible for authenticating users (login)
- Checks username and password

**How it works:**
```
1. User submits username & password
2. AuthenticationManager.authenticate() called
3. Loads user details via UserDetailsService
4. Compares passwords
5. Returns User if valid, throws exception if invalid
```

---

### Bean 3: Security Filter Chain

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) 
    throws Exception {
    
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> {
            authorize
                // Step 1: Public endpoints (no auth required)
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs",
                    "/api/auth/**"
                ).permitAll()
                
                // Step 2: All other endpoints require authentication
                .anyRequest().authenticated()
            
        }).httpBasic(Customizer.withDefaults());
    
    return http.build();
}
```

**Breakdown:**

#### 1. CSRF Disable
```java
http.csrf(AbstractHttpConfigurer::disable)
```
- CSRF = Cross-Site Request Forgery attack protection
- Disabled for REST APIs (use tokens instead)
- ✅ OK for API, ❌ problematic for web forms

#### 2. Public Endpoints
```java
.requestMatchers(
    "/swagger-ui/**",           // Swagger UI
    "/api-docs/**",             // API documentation
    "/v3/api-docs/**",          // OpenAPI docs
    "/api/auth/**"              // Authentication endpoints
).permitAll()
```
**Allows access WITHOUT login:**
- `/api/auth/login` - User login
- `/api/auth/register` - User registration
- `/swagger-ui/` - API documentation

#### 3. All Other Endpoints Protected
```java
.anyRequest().authenticated()
```
**Requires authentication:**
- `/api/contents/**` - Content management
- `/api/categories/**` - Category management
- `/api/notifications/**` - Notifications

#### 4. HTTP Basic Auth
```java
.httpBasic(Customizer.withDefaults())
```
**Authentication method:**
```
Username: john_doe
Password: ***

Browser sends: Authorization: Basic am9obl9kb2U6MTIzNDU2
```

---

## CustomUserDetailsService - Detailed

### File Location
```
src/main/java/com/example/contentmanagement/security/CustomUserDetailsService.java
```

### Code

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) 
        throws UsernameNotFoundException {
        
        // Step 1: Find user in database
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username
            ));

        // Step 2: Convert roles to authorities
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),                    // "john_doe"
            user.getPassword(),                    // "$2a$10$..."
            user.getRoles().stream()               // [ADMIN, USER]
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList())      // [ROLE_ADMIN, ROLE_USER]
        );
    }
}
```

### How It Works

#### Step-by-Step Execution

```
1. User logs in with username: "john_doe"
   ↓
2. loadUserByUsername("john_doe") called
   ↓
3. Query database: UserRepository.findByUsername("john_doe")
   ↓
4. Database returns User object:
   {
       id: "123",
       username: "john_doe",
       password: "$2a$10$...",  (BCrypt encoded)
       roles: [ROLE_ADMIN, ROLE_USER]
   }
   ↓
5. If user not found → throw UsernameNotFoundException
   ↓
6. Convert roles to authorities:
   ROLE_ADMIN → SimpleGrantedAuthority("ROLE_ADMIN")
   ROLE_USER  → SimpleGrantedAuthority("ROLE_USER")
   ↓
7. Return UserDetails object with:
   - Username
   - Encoded password
   - List of authorities (permissions)
```

### UserRepository.findByUsername()

```java
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
```

**Explanation:**
- MongoDB finds document where `username` field matches
- Returns `Optional<User>` (may or may not exist)
- `.orElseThrow()` throws exception if not found

---

## Role-Based Access Control (RBAC)

### How Roles Work

#### Define Roles
```java
// Entity class
@Document(collection = "roles")
public class Role {
    private String id;          // "admin"
    private String name;        // "ROLE_ADMIN"
    private List<String> permissions;  // ["CREATE", "READ", "UPDATE", "DELETE"]
}
```

#### Assign to Users
```java
@Document(collection = "users")
public class User {
    private String id;          // "user123"
    private String username;    // "john_doe"
    private String password;    // Encoded password
    private List<Role> roles;   // [ROLE_ADMIN, ROLE_USER]
    private String email;
}
```

#### Create Database Entries

```sql
// Roles Collection
{
    _id: "1",
    name: "ROLE_ADMIN",
    permissions: ["ALL"]
}

{
    _id: "2",
    name: "ROLE_USER",
    permissions: ["READ", "CREATE_OWN"]
}

// Users Collection
{
    _id: "user123",
    username: "john_doe",
    password: "$2a$10$...",
    roles: ["ROLE_ADMIN", "ROLE_USER"],
    email: "john@example.com"
}
```

---

## @PreAuthorize - Control Who Can Access Endpoints

### What is @PreAuthorize?

**Decorator** that controls access based on roles or expressions.

### In Controllers

```java
@RestController
@RequestMapping("/api/contents")
public class ContentController {

    // Any authenticated user can create content
    @PostMapping("/films")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FilmDTO> createFilm(
        @Valid @RequestBody FilmDTO filmDTO
    ) {
        return new ResponseEntity<>(
            contentService.createFilm(filmDTO),
            HttpStatus.CREATED
        );
    }

    // Only ADMIN can delete content
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    // Public read (no auth needed - but already secured at filter level)
    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllContent());
    }
}
```

### Authorization Rules

| Rule | Meaning | Example |
|------|---------|---------|
| `hasRole('ADMIN')` | Only ADMIN | Admin only endpoints |
| `hasRole('USER')` | Only USER | User endpoints |
| `hasRole('ADMIN') or hasRole('USER')` | Either role | Create content |
| `hasAuthority('READ')` | Specific permission | View data |
| `isAuthenticated()` | Any logged-in user | Not public |
| `permitAll()` | Anyone including anonymous | Login page |

### Access Denied Response

```json
HTTP 403 Forbidden
{
    "timestamp": "2026-03-02T15:30:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied: User does not have required role"
}
```

---

## Authentication Flow Diagram

```
┌─────────────────────────────────────────────────────┐
│ Frontend (Angular)                                  │
│  - Opens login form                                 │
│  - User enters: username, password                  │
└──────────────────┬──────────────────────────────────┘
                   │ POST /api/auth/login
                   │ { username, password }
                   ↓
┌─────────────────────────────────────────────────────┐
│ Backend - Security Filter Chain                     │
│  1. Check if request needs auth                     │
│  2. Extract credentials from request                │
│  3. Call AuthenticationManager                      │
└──────────────────┬──────────────────────────────────┘
                   │ 
                   ↓
┌─────────────────────────────────────────────────────┐
│ CustomUserDetailsService                            │
│  1. loadUserByUsername(username)                    │
│  2. Query database for user                         │
│  3. Return UserDetails with roles                   │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│ PasswordEncoder (BCrypt)                            │
│  1. Compare submitted password with stored          │
│  2. Return true/false                               │
└──────────────────┬──────────────────────────────────┘
                   │
        ┌──────────┤
        │          │
   ✓ Match    ✗ No Match
        │          │
        ↓          ↓
    Auth OK   Throw Exception
        │          │
        └──────┬───┘
               │
               ↓
┌─────────────────────────────────────────────────────┐
│ Response to Frontend                                │
│  Success: JWT token or session                      │
│  Failure: 401 Unauthorized                          │
└─────────────────────────────────────────────────────┘
```

---

## Security Configuration Step-by-Step

### Step 1: Add Spring Security Dependency

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Step 2: Create SecurityConfig Class

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // Configuration beans
}
```

### Step 3: Configure Password Encoder

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### Step 4: Configure User Details Service

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Load user from database
    }
}
```

### Step 5: Configure Security Filter Chain

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.authorizeHttpRequests(...)
        .httpBasic(...)
        .build();
    return http;
}
```

### Step 6: Protect Endpoints with @PreAuthorize

```java
@PreAuthorize("hasRole('USER')")
public ResponseEntity<?> protectedEndpoint() {
    // Only users with USER role can access
}
```

---

# Frontend Validation

## How Angular Validates Forms

### Form Setup with Reactive Forms

```typescript
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomValidators } from './validators';

export class AdminContentComponent implements OnInit {
    contentForm!: FormGroup;

    constructor(private fb: FormBuilder) {
        this.initializeForm();
    }

    initializeForm() {
        this.contentForm = this.fb.group({
            // Field: [Initial Value, [Validators...]]
            
            title: [
                '',  // Initial value: empty
                [
                    Validators.required,                    // Built-in validator
                    CustomValidators.minLength(3),          // Custom validator
                    CustomValidators.maxLength(255)         // Custom validator
                ]
            ],
            
            description: [
                '',
                [
                    Validators.required,
                    CustomValidators.minLength(10)
                ]
            ],
            
            releaseDate: [
                '',
                [
                    CustomValidators.dateFormat,            // Must be YYYY-MM-DD
                    CustomValidators.pastDateValidator      // Cannot be in past
                ]
            ],
            
            duration: [
                '',
                [
                    CustomValidators.numeric,               // Must be number
                    CustomValidators.minValue(1)            // Min 1
                ]
            ],
            
            genre: [
                '',
                [Validators.required]
            ]
        });
    }
}
```

### Validate on Submit

```typescript
saveContent() {
    // Mark all fields as touched to show errors
    if (!this.contentForm.valid) {
        Object.keys(this.contentForm.controls).forEach(key => {
            const control = this.contentForm.get(key);
            if (control && control.invalid) {
                control.markAsTouched();  // Shows error message
            }
        });
        return;  // Don't submit
    }

    // Form is valid, send to backend
    const formData = this.contentForm.value;
    this.contentService.createFilm(formData).subscribe(
        response => {
            console.log('Created successfully!', response);
        },
        error => {
            console.error('Error:', error);
        }
    );
}
```

### Display Errors in Template

```html
<div>
    <label class="block text-sm text-gray-300 mb-2">
        Title <span class="text-red-500">*</span>
    </label>
    
    <input
        type="text"
        formControlName="title"
        placeholder="Content title"
        class="w-full px-4 py-2 bg-[#0B0E14] border border-[#8B5CF6]/30 rounded-lg text-white"
        [class.border-red-500]="contentForm.get('title')?.invalid && contentForm.get('title')?.touched"
    />
    
    <!-- Show error message -->
    <p *ngIf="contentForm.get('title')?.invalid && contentForm.get('title')?.touched" 
       class="text-red-500 text-sm mt-1">
        {{ getFieldError('title') }}
    </p>
</div>
```

### Get Error Messages

```typescript
getFieldError(fieldName: string): string {
    const control = this.contentForm.get(fieldName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength']) 
        return `${fieldName} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) 
        return `${fieldName} must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['numeric']) return `${fieldName} must be a valid number`;
    if (errors['minvalue']) return `${fieldName} must be at least ${errors['minvalue'].min}`;
    if (errors['dateformat']) return 'Date must be in YYYY-MM-DD format';
    if (errors['pastdate']) return 'Release date cannot be in the past';

    return 'Invalid value';
}
```

---

# How We Implemented Everything - Complete Walkthrough

This section explains **exactly how we built** the REST API consumption and validation system. It answers all questions about what we did and why.

## What We Built

When you asked us to:
1. **"Consume RESTful web services (full CRUD)"** → We created API services
2. **"Implement Front-end input validation"** → We created custom validators
3. **"Implement Backend input validation"** → We used Spring annotations

Let us walk you through **exactly what we did, step-by-step**.

---

## Part 1: REST API Services - How We Consumed the Backend

### The Problem We Solved

Frontend needs to talk to Backend. How?
```
Frontend (Angular)  ←→  HTTP Requests/Responses  ←→  Backend (Spring Boot)
http://localhost:4200                              http://localhost:8080
```

### Solution: Create API Services

**Location:** `src/app/services/api.service.ts`

**Why we created this:**
- Single file to manage all API calls
- Reusable across all components
- Easy to change backend URL if needed

### What We Created: 3 Main Services

#### Service 1: ContentService

```typescript
@Injectable({ providedIn: 'root' })
export class ContentService {
    private baseUrl = 'http://localhost:8080/api/contents';

    constructor(private http: HttpClient) { }

    // CREATE - Add new film
    createFilm(film: Film): Observable<Film> {
        return this.http.post<Film>(`${this.baseUrl}/films`, film)
            .pipe(catchError(this.handleError));
    }

    // READ - Get all content
    getAllContent(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl)
            .pipe(catchError(this.handleError));
    }

    // UPDATE - Modify existing film
    updateFilm(id: string, film: Film): Observable<Film> {
        return this.http.put<Film>(`${this.baseUrl}/films/${id}`, film)
            .pipe(catchError(this.handleError));
    }

    // DELETE - Remove content
    deleteContent(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    private handleError(error: HttpErrorResponse) {
        let message = 'An error occurred';
        if (error.error instanceof ErrorEvent) {
            message = `Error: ${error.error.message}`;
        } else {
            message = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        return throwError(() => new Error(message));
    }
}
```

**How it works:**

1. **createFilm(film)**
   - Takes film object from form
   - Sends POST request to backend
   - Backend validates with @Valid annotation
   - Returns created film with ID
   ```
   Frontend: POST /api/contents/films
   Body: { title: "Inception", duration: 148, ... }
   Backend: Validates, saves, returns FilmDTO
   ```

2. **getAllContent()**
   - Fetches all content from backend
   - Called when component initializes
   - Loads data into table
   ```
   Frontend: GET /api/contents
   Backend: Queries database, returns List<ContentDTO>
   ```

3. **updateFilm(id, film)**
   - Updates existing content
   - Backend checks if user has permission
   - Only ADMIN can update (checked with @PreAuthorize)
   ```
   Frontend: PUT /api/contents/films/{id}
   Body: { title: "Updated Title", ... }
   Backend: Updates in DB, returns updated FilmDTO
   ```

4. **deleteContent(id)**
   - Removes content from database
   - Only ADMIN can delete
   - Returns no content (204 status)
   ```
   Frontend: DELETE /api/contents/{id}
   Backend: Deletes from DB
   Returns: 204 No Content
   ```

#### Service 2: CategoryService

```typescript
@Injectable({ providedIn: 'root' })
export class CategoryService {
    private baseUrl = 'http://localhost:8080/api/categories';

    constructor(private http: HttpClient) { }

    createCategory(category: Category): Observable<Category> {
        return this.http.post<Category>(this.baseUrl, category)
            .pipe(catchError(this.handleError));
    }

    getAllCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(this.baseUrl)
            .pipe(catchError(this.handleError));
    }

    updateCategory(id: string, category: Category): Observable<Category> {
        return this.http.put<Category>(`${this.baseUrl}/${id}`, category)
            .pipe(catchError(this.handleError));
    }

    deleteCategory(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    private handleError(error: HttpErrorResponse) {
        let message = 'An error occurred';
        if (error.error instanceof ErrorEvent) {
            message = `Error: ${error.error.message}`;
        } else {
            message = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        return throwError(() => new Error(message));
    }
}
```

**Same pattern as ContentService** - provides CRUD operations for categories.

#### Service 3: NotificationService

```typescript
@Injectable({ providedIn: 'root' })
export class NotificationService {
    private baseUrl = 'http://localhost:8080/api/notifications';

    constructor(private http: HttpClient) { }

    createNotification(notification: Notification): Observable<Notification> {
        return this.http.post<Notification>(this.baseUrl, notification)
            .pipe(catchError(this.handleError));
    }

    getNotificationsByUserId(userId: string): Observable<Notification[]> {
        return this.http.get<Notification[]>(`${this.baseUrl}/user/${userId}`)
            .pipe(catchError(this.handleError));
    }

    markAsRead(id: string): Observable<void> {
        return this.http.patch<void>(`${this.baseUrl}/${id}/read`, {})
            .pipe(catchError(this.handleError));
    }

    deleteNotification(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    private handleError(error: HttpErrorResponse) {
        let message = 'An error occurred';
        if (error.error instanceof ErrorEvent) {
            message = `Error: ${error.error.message}`;
        } else {
            message = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        return throwError(() => new Error(message));
    }
}
```

**Provides:** Create, Read, Update (mark as read), Delete for notifications.

---

### How We Connected Services to Components

#### Step 1: Inject Service in Component

```typescript
export class AdminContentComponent implements OnInit {
    
    constructor(
        private contentService: ContentService,  // Inject here
        private fb: FormBuilder
    ) {}
    
    ngOnInit() {
        this.loadAllContent();  // Call when component loads
    }
}
```

#### Step 2: Call Service Methods

```typescript
// Load all content when page opens
loadAllContent() {
    this.loading.set(true);
    this.error.set(null);
    
    this.contentService.getAllContent().subscribe({
        next: (data) => {
            // Convert API response to UI format
            const mappedContent = data.map((item: any) => ({
                id: item.id,
                title: item.title,
                platform: item.platform || 'Unknown',
                genre: item.genre || 'Unknown',
                rating: item.rating || 0,
                views: item.views || 0,
                status: item.status || 'active',
                hiddenGem: item.hiddenGem || false,
                releaseDate: item.releaseDate || new Date().toISOString().split('T')[0],
            }));
            
            this.contentList.set(mappedContent);  // Update UI
            this.loading.set(false);
        },
        error: (err) => {
            this.error.set('Failed to load content: ' + err.message);
            this.loading.set(false);
        },
    });
}
```

#### Step 3: Handle Create/Update/Delete

```typescript
// Create new content
createContent(data: any) {
    this.loading.set(true);
    
    this.contentService.createFilm(data).subscribe({
        next: (response) => {
            // Add to list
            const newContent: Content = {
                id: response.id || Math.random().toString(),
                title: response.title,
                platform: data.platform || 'Unknown',
                genre: response.genre,
                rating: 0,
                views: 0,
                status: data.status,
                hiddenGem: data.hiddenGem,
                releaseDate: response.releaseDate,
            };
            
            this.contentList.set([...this.contentList(), newContent]);  // Add to table
            this.closeForm();
            this.loading.set(false);
            alert('Content created successfully!');
        },
        error: (err) => {
            this.error.set('Failed to create content: ' + err.message);
            this.loading.set(false);
        },
    });
}

// Update existing content
updateContent(id: string, data: any) {
    this.loading.set(true);
    
    this.contentService.updateFilm(id, data).subscribe({
        next: (response) => {
            // Update in list
            const updated = this.contentList().map(item =>
                item.id === id ? { ...item, ...response, status: data.status } : item
            );
            this.contentList.set(updated);
            this.closeForm();
            this.loading.set(false);
            alert('Content updated successfully!');
        },
        error: (err) => {
            this.error.set('Failed to update content: ' + err.message);
            this.loading.set(false);
        },
    });
}

// Delete content
deleteContent(id: string) {
    if (confirm('Are you sure you want to delete this content?')) {
        this.loading.set(true);
        
        this.contentService.deleteContent(id).subscribe({
            next: () => {
                // Remove from list
                this.contentList.set(this.contentList().filter(item => item.id !== id));
                this.loading.set(false);
                alert('Content deleted successfully!');
            },
            error: (err) => {
                this.error.set('Failed to delete content: ' + err.message);
                this.loading.set(false);
            },
        });
    }
}
```

---

## Part 2: Frontend Input Validation - How We Validated on Browser

### The Problem We Solved

We needed to validate **before sending** to backend:
```
User enters data → Validate in browser → Show errors → Submit if valid
```

### Solution: Create Custom Validators

**Location:** `src/app/services/validators.ts`

### Pattern for Each Validator

```typescript
// General Pattern
static validatorName(config?: any): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        // 1. Check if control has value
        if (!control.value) return null;
        
        // 2. Apply validation logic
        if (condition) {
            return { errorKey: true };  // Return error
        }
        
        // 3. Return null if valid
        return null;
    };
}
```

### All 15+ Validators We Created

#### 1. Required Validator
```typescript
static required(control: AbstractControl): ValidationErrors | null {
    if (!control.value || (typeof control.value === 'string' && !control.value.trim())) {
        return { required: true };
    }
    return null;
}
```
**Why we created it:**
- Built-in Validators.required only checks null
- We also check empty strings and whitespace
- Prevents users from submitting empty fields

**Usage:**
```typescript
title: ['', [CustomValidators.required]]
```

#### 2. minLength Validator
```typescript
static minLength(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        if (control.value.length < min) {
            return { minlength: { requiredLength: min, actualLength: control.value.length } };
        }
        return null;
    };
}
```
**Why:**
- Ensures text fields meet minimum length
- Example: Username must be at least 3 characters

**Usage:**
```typescript
title: ['', [CustomValidators.minLength(3)]]
```

#### 3. maxLength Validator
```typescript
static maxLength(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        if (control.value.length > max) {
            return { maxlength: { requiredLength: max, actualLength: control.value.length } };
        }
        return null;
    };
}
```
**Why:**
- Ensures database fields don't overflow
- Example: Title can't exceed 255 characters

**Usage:**
```typescript
title: ['', [CustomValidators.maxLength(255)]]
```

#### 4. Numeric Validator
```typescript
static numeric(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const value = Number(control.value);
    if (isNaN(value)) {
        return { numeric: true };
    }
    return null;
}
```
**Why:**
- Ensures duration is a number
- Prevents "abc" being entered in number field

**Usage:**
```typescript
duration: ['', [CustomValidators.numeric]]
```

#### 5. minValue Validator
```typescript
static minValue(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        const value = Number(control.value);
        if (isNaN(value) || value < min) {
            return { minvalue: { min, actual: value } };
        }
        return null;
    };
}
```
**Why:**
- Duration must be at least 1 minute
- Movie/show can't have 0 or negative duration

**Usage:**
```typescript
duration: ['', [CustomValidators.minValue(1)]]
```

#### 6. maxValue Validator
```typescript
static maxValue(max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (!control.value) return null;
        const value = Number(control.value);
        if (isNaN(value) || value > max) {
            return { maxvalue: { max, actual: value } };
        }
        return null;
    };
}
```
**Why:**
- Rating can't exceed 5 stars
- Seasons can't be unrealistic

**Usage:**
```typescript
rating: ['', [CustomValidators.maxValue(5)]]
```

#### 7. dateFormat Validator
```typescript
static dateFormat(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;  // YYYY-MM-DD
    if (!dateRegex.test(control.value)) {
        return { dateformat: true };
    }
    const date = new Date(control.value);
    if (isNaN(date.getTime())) {
        return { dateformat: true };
    }
    return null;
}
```
**Why:**
- Ensures date is in correct format
- Prevents invalid dates like 2026-13-01

**Usage:**
```typescript
releaseDate: ['', [CustomValidators.dateFormat]]
```

#### 8. futureDateValidator
```typescript
static futureDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (selectedDate < today) {
        return { pastdate: true };
    }
    return null;
}
```
**Why:**
- Release date can't be in the past
- Only allows future dates

**Usage:**
```typescript
releaseDate: ['', [CustomValidators.futureDateValidator]]
// Error if: 2025-12-25
// OK if: 2026-03-15
```

#### 9. Email Validator
```typescript
static email(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(control.value)) {
        return { email: true };
    }
    return null;
}
```
**Why:**
- Verifies email format
- Prevents invalid emails

**Usage:**
```typescript
email: ['', [CustomValidators.email]]
// Error if: john@.com
// OK if: john@example.com
```

#### 10-15. Other Validators
We also created:
- **URL Validator** - Validates URLs
- **Pattern Validator** - Custom regex patterns
- **Match Validator** - Two fields match (password confirmation)
- **pastDateValidator** - Date not in future

---

### How We Used Validators in Components

#### Step 1: Create Form with Validators

```typescript
export class AdminContentComponent implements OnInit {
    contentForm!: FormGroup;

    constructor(private fb: FormBuilder) {
        this.initializeForm();
    }

    initializeForm() {
        this.contentForm = this.fb.group({
            title: [
                '',
                [
                    Validators.required,
                    CustomValidators.minLength(3),
                    CustomValidators.maxLength(255)
                ]
            ],
            description: [
                '',
                [
                    Validators.required,
                    CustomValidators.minLength(10)
                ]
            ],
            releaseDate: [
                '',
                [
                    CustomValidators.dateFormat,
                    CustomValidators.pastDateValidator  // Don't allow past
                ]
            ],
            duration: [
                '',
                [
                    CustomValidators.numeric,
                    CustomValidators.minValue(1)
                ]
            ],
            genre: ['', Validators.required],
            status: ['active', Validators.required],
            hiddenGem: [false]
        });
    }
}
```

**What happens when user types:**
1. Real-time validation
2. Field turns red if invalid
3. Error message appears below
4. Save button stays disabled

#### Step 2: Display Errors in Template

```html
<div>
    <label class="block text-sm text-gray-300 mb-2">
        Title <span class="text-red-500">*</span>
    </label>
    
    <input
        type="text"
        formControlName="title"
        placeholder="Content title"
        class="w-full px-4 py-2 bg-[#0B0E14] border border-[#8B5CF6]/30 rounded-lg text-white"
        [class.border-red-500]="contentForm.get('title')?.invalid && contentForm.get('title')?.touched"
    />
    
    <!-- Error message only shows if:
         1. Field is invalid
         2. User clicked on it (touched = true)
    -->
    <p *ngIf="contentForm.get('title')?.invalid && contentForm.get('title')?.touched" 
       class="text-red-500 text-sm mt-1">
        {{ getFieldError('title') }}
    </p>
</div>
```

#### Step 3: Get Error Messages

```typescript
getFieldError(fieldName: string): string {
    const control = this.contentForm.get(fieldName);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;
    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength']) 
        return `${fieldName} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) 
        return `${fieldName} must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['numeric']) return `${fieldName} must be a valid number`;
    if (errors['minvalue']) 
        return `${fieldName} must be at least ${errors['minvalue'].min}`;
    if (errors['dateformat']) return 'Date must be in YYYY-MM-DD format';
    if (errors['pastdate']) return 'Release date cannot be in the past';
    if (errors['futuredate']) return 'Release date cannot be in the future';

    return 'Invalid value';
}
```

**How it works:**
1. Check which validator failed
2. Return appropriate error message
3. Template displays the message

#### Step 4: Validate Before Submit

```typescript
saveContent() {
    // Step 1: Check if form is complete and valid
    if (!this.contentForm.valid) {
        // Step 2: Mark all fields as touched so errors show
        Object.keys(this.contentForm.controls).forEach(key => {
            const control = this.contentForm.get(key);
            if (control && control.invalid) {
                control.markAsTouched();  // Triggers error display
            }
        });
        return;  // Don't submit
    }

    // Step 3: Form is valid, get data
    const formData = this.contentForm.value;

    // Step 4: Determine if creating or updating
    if (this.editingId()) {
        this.updateContent(this.editingId()!, formData);
    } else {
        this.createContent(formData);
    }
}
```

---

## Part 3: Backend Input Validation - How We Validated on Server

### The Problem We Solved

Backend needs to **validate data again** even if frontend validated:
```
Frontend validation ❌ Backend still validates
  (User can bypass) ✅ (Server enforces rules)
```

### Solution: Use Spring Validation Annotations

**In DTO files:** `src/main/java/.../dto/`

### How Backend Validates: Step by Step

#### Step 1: Add Annotations to DTO

```java
public class FilmDTO extends ContentDTO {
    
    @Positive(message = "Duration must be positive")
    private Integer durationInMinutes;
    
    @NotBlank(message = "Director is mandatory")
    private String director;
}
```

**What each annotation does:**
- `@Positive` - Checks: durationInMinutes > 0
- `@NotBlank` - Checks: director is not empty

#### Step 2: Use @Valid in Controller

```java
@PostMapping("/films")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public ResponseEntity<FilmDTO> createFilm(
    @Valid @RequestBody FilmDTO filmDTO  // This triggers validation
) {
    // If any @Positive, @NotBlank etc fails:
    //   → 400 Bad Request returned
    //   → Error message sent to frontend
    
    // If all valid, continue to service
    return new ResponseEntity<>(
        contentService.createFilm(filmDTO),
        HttpStatus.CREATED
    );
}
```

**How it works:**
```
1. Frontend sends: POST /api/contents/films
   Body: { durationInMinutes: -5, director: "" }
   
2. Spring receives request
   
3. Spring sees @Valid annotation
   
4. Spring validates each field:
   - @Positive check: -5 is NOT > 0 ❌
   - @NotBlank check: "" IS blank ❌
   
5. Spring stops and returns 400:
   {
       "error": "Validation failed",
       "details": {
           "durationInMinutes": "Duration must be positive",
           "director": "Director is mandatory"
       }
   }
   
6. Frontend receives 400 error and shows to user
```

#### Step 3: All Validation Annotations We Used

| Annotation | Purpose | Example |
|-----------|---------|---------|
| `@NotBlank` | Not empty string | @NotBlank(message = "Title required") |
| `@NotNull` | Not null value | @NotNull(message = "ID required") |
| `@Size(min, max)` | Length range | @Size(min=1, max=255) |
| `@Positive` | Value > 0 | @Positive(message = "Duration > 0") |
| `@Email` | Valid email | @Email(message = "Invalid email") |
| `@Pattern` | Matches regex | @Pattern(regexp = "...") |

#### Step 4: Complete Example - FilmDTO

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDTO extends ContentDTO {
    
    // VALIDATION 1: Duration must be a positive number
    @Positive(message = "Duration must be positive")
    private Integer durationInMinutes;
    
    // VALIDATION 2: Director cannot be empty
    @NotBlank(message = "Director is mandatory")
    private String director;
}

// Parent class ContentDTO also validates:
@Getter
@Setter
public abstract class ContentDTO {
    
    // VALIDATION 3: Title is required
    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title 1-255 chars")
    private String title;
    
    // VALIDATION 4: Description max 1000 chars
    @Size(max = 1000, message = "Description max 1000 chars")
    private String description;
    
    // VALIDATION 5: Category required
    @NotNull(message = "Category ID is mandatory")
    private String categoryId;
    
    // VALIDATION 6: Type must be FILM|SERIES|DOCUMENTARY
    @Pattern(regexp = "FILM|SERIES|DOCUMENTARY", 
             message = "Type must be FILM, SERIES, or DOCUMENTARY")
    private String contentType;
}
```

#### Step 5: Error Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        
        // Extract each validation error
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(
            Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "Validation failed",
                "details", errors
            )
        );
    }
}
```

**Returns error like:**
```json
{
    "timestamp": "2026-03-02T15:30:00",
    "status": 400,
    "error": "Validation failed",
    "details": {
        "title": "Title is mandatory",
        "durationInMinutes": "Duration must be positive",
        "director": "Director is mandatory"
    }
}
```

---

## Part 4: Complete CRUD Operation - End to End

Let me show you **one complete example** from frontend to backend and back.

### Scenario: User Creates a Film

```
FRONTEND
  ↓
USER ENTERS DATA
├─ Title: "Inception"
├─ Description: "A sci-fi thriller..."
├─ Duration: 148
├─ Director: "Christopher Nolan"
├─ Release Date: 2026-04-15
└─ Genre: "Sci-Fi"
  ↓
FRONTEND VALIDATES
├─ Title: 1-255 chars? ✓ (9 chars)
├─ Description: 10+ chars? ✓ (46 chars)
├─ Duration: numeric + >0? ✓ (148)
├─ Director: not blank? ✓
├─ Date: valid format? ✓ (2026-04-15)
├─ Date: not in past? ✓ (Apr 15 is future)
└─ Save button: ENABLED ✓
  ↓
USER CLICKS SAVE
  ↓
COMPONENT CALLS SERVICE
contentService.createFilm({
    title: "Inception",
    description: "A sci-fi thriller...",
    durationInMinutes: 148,
    director: "Christopher Nolan",
    releaseDate: "2026-04-15",
    categoryId: "sci-fi",
    contentType: "FILM"
})
  ↓
HTTP REQUEST SENT
POST http://localhost:8080/api/contents/films
Content-Type: application/json
Body: {
    "title": "Inception",
    "description": "A sci-fi thriller...",
    "durationInMinutes": 148,
    "director": "Christopher Nolan",
    "releaseDate": "2026-04-15",
    "categoryId": "sci-fi",
    "contentType": "FILM"
}
  ↓
BACKEND RECEIVES
  ↓
SPRING SEES @Valid ANNOTATION
  ↓
BACKEND VALIDATES (using DTOs)
FilmDTO:
├─ @Positive: 148 > 0? ✓
├─ @NotBlank: "Christopher Nolan" not blank? ✓
ContentDTO (parent):
├─ @NotBlank: "Inception" not blank? ✓
├─ @Size: 9 chars within 1-255? ✓
├─ @Size: 46 chars within 1000? ✓
├─ @NotNull: categoryId exists? ✓
└─ @Pattern: "FILM" matches pattern? ✓
ALL VALID ✓
  ↓
CALLS CONTROLLER METHOD
@PostMapping("/films")
public ResponseEntity<FilmDTO> createFilm(
    @Valid @RequestBody FilmDTO filmDTO
) {
    // filmDTO is valid, continue
}
  ↓
CALLS SERVICE
contentService.createFilm(filmDTO)
  ↓
SERVICE LOGIC
├─ Check database permissions
├─ Convert DTO to Entity
├─ Save to MongoDB
├─ Get saved object with ID
└─ Convert back to DTO
  ↓
DATABASE SAVE
MongoDB saves:
{
    "_id": "64e2f3a8b9c1d2e3f4g5h6i7",
    "title": "Inception",
    "description": "A sci-fi thriller...",
    "durationInMinutes": 148,
    "director": "Christopher Nolan",
    "releaseDate": ISODate("2026-04-15"),
    "categoryId": "sci-fi",
    "contentType": "FILM",
    "addedByUsername": "john_doe",
    "createdAt": ISODate("2026-03-02T15:30:00Z")
}
  ↓
RETURNS TO FRONTEND
HTTP 201 CREATED
{
    "id": "64e2f3a8b9c1d2e3f4g5h6i7",
    "title": "Inception",
    "description": "A sci-fi thriller...",
    "durationInMinutes": 148,
    "director": "Christopher Nolan",
    "releaseDate": "2026-04-15",
    "categoryId": "sci-fi",
    "contentType": "FILM"
}
  ↓
FRONTEND RECEIVES RESPONSE
  ↓
COMPONENT.subscribe()
next: (response) => {
    // Add to list
    contentList.push(response);
    // Close form
    showForm = false;
    // Show success
    alert("Created successfully!");
}
  ↓
TABLE REFRESHES
  ↓
USER SEES NEW FILM IN TABLE
✓ Success!
```

---

## What We Did - Summary Table

| Feature | We Created | Location | Purpose |
|---------|-----------|----------|---------|
| API Services | ContentService | src/app/services/api.service.ts | Connect to backend |
| API Services | CategoryService | src/app/services/api.service.ts | GET/POST categories |
| API Services | NotificationService | src/app/services/api.service.ts | Manage notifications |
| Validators | 15+ validators | src/app/services/validators.ts | Validate on browser |
| Forms | Reactive forms | admin-content.component.ts | Create/edit content |
| Templates | Form templates | admin-content.component.html | Display forms |
| DTOs | FilmDTO, SeriesDTO, etc | Backend DTO files | Server-side validation |
| Controllers | ContentController | Backend | Handle API requests |
| Security | @PreAuthorize | Controllers | Control access |

---

## Key Concepts We Implemented

### 1. Separation of Concerns
```
Frontend owns:    User experience, visual validation
Backend owns:     Data integrity, business logic, security
```

### 2. Defense in Depth
```
Layer 1: Frontend validation      (User convenience)
Layer 2: Backend validation       (Data security)
Layer 3: Database constraints     (Last resort)
```

### 3. Single Responsibility
```
Service:     Handles HTTP calls only
Validator:   Checks validation rules only
Component:   Manages UI and state only
Controller:  Routes requests only
DTO:         Transfers data with validation only
```

### 4. Error Handling
```
Frontend Error:   Shows friendly message to user
Backend Error:    Returns detailed error response
Network Error:    Shows "Connection failed" message
```

---

# Implementation Steps

## Complete Implementation Checklist

### Phase 1: Backend Setup

#### Step 1.1: Create DTOs
```bash
Create files in: src/main/java/com/example/contentmanagement/dto/
├── ContentDTO.java       (base class)
├── FilmDTO.java
├── SeriesDTO.java
├── DocumentaryDTO.java
├── UserDTO.java
├── CategoryDTO.java
└── NotificationDTO.java
```

**What to include:**
- All fields with correct types
- Validation annotations (@NotBlank, @Size, etc.)
- Lombok annotations (@Getter, @Setter, @Builder)

#### Step 1.2: Create Entity Classes
```bash
Create files in: src/main/java/com/example/contentmanagement/entity/
├── Content.java
├── User.java
├── Role.java
├── Category.java
└── Notification.java
```

**Key points:**
- Use `@Document` for MongoDB
- Map relationships between entities
- Add indexes for performance

#### Step 1.3: Create Repositories
```java
public interface ContentRepository extends MongoRepository<Content, String> {
    List<Content> findByTitle(String title);
    List<Content> findByCategoryId(String categoryId);
}

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
```

#### Step 1.4: Create Services
```java
@Service
public class ContentService {
    public FilmDTO createFilm(FilmDTO filmDTO) {
        // Validate DTO
        // Convert to Entity
        // Save to database
        // Return DTO
    }
}
```

#### Step 1.5: Create Controllers
```java
@RestController
@RequestMapping("/api/contents")
public class ContentController {
    @PostMapping("/films")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FilmDTO> createFilm(
        @Valid @RequestBody FilmDTO filmDTO
    ) {
        return new ResponseEntity<>(
            contentService.createFilm(filmDTO),
            HttpStatus.CREATED
        );
    }
}
```

#### Step 1.6: Setup Security
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // Configure password encoder
    // Configure authentication manager
    // Configure security filter chain
}
```

### Phase 2: Frontend Setup

#### Step 2.1: Create API Services
```typescript
@Injectable({ providedIn: 'root' })
export class ContentService {
    constructor(private http: HttpClient) {}
    
    createFilm(film: Film): Observable<Film> {
        return this.http.post<Film>(`${this.baseUrl}/films`, film);
    }
}
```

#### Step 2.2: Create Validator Service
```typescript
export class CustomValidators {
    static required(control: AbstractControl) { /* ... */ }
    static minLength(min: number) { /* ... */ }
    static dateFormat(control: AbstractControl) { /* ... */ }
    // ...all validators
}
```

#### Step 2.3: Update Components
```typescript
export class AdminContentComponent implements OnInit {
    contentForm!: FormGroup;
    
    constructor(
        private fb: FormBuilder,
        private contentService: ContentService
    ) {
        this.initializeForm();
    }
    
    ngOnInit() {
        this.loadContent();
    }
    
    initializeForm() {
        this.contentForm = this.fb.group({
            // Form controls with validators
        });
    }
}
```

#### Step 2.4: Update Templates
```html
<form [formGroup]="contentForm">
    <input formControlName="title" />
    <p *ngIf="contentForm.get('title')?.invalid">
        {{ getFieldError('title') }}
    </p>
    <button [disabled]="!contentForm.valid" (click)="saveContent()">
        Save
    </button>
</form>
```

### Phase 3: Testing

#### Test Content CRUD
```bash
1. Create: Add new content with form validation
2. Read: Load all content on page init
3. Update: Edit existing content
4. Delete: Remove content with confirmation
```

#### Test Validation
```bash
1. Required fields: Leave empty and try to submit
2. Length validation: Enter too short/long text
3. Date validation: Try past date
4. Number validation: Enter non-numeric value
5. Format validation: Enter invalid email/URL
```

#### Test Security
```bash
1. Public endpoints: /api/auth/login (no auth required)
2. Protected endpoints: /api/contents (auth required)
3. Admin only: /api/contents/{id} DELETE (admin role required)
4. JWT token: Include in Authorization header
```

---

# Code Examples

## Complete CRUD Example

### Frontend - Add Content Component

```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContentService } from '../../services/api.service';
import { CustomValidators } from '../../services/validators';

@Component({
    selector: 'app-admin-content',
    template: `
        <div>
            <!-- Form -->
            <form [formGroup]="contentForm" (ngSubmit)="saveContent()">
                <div>
                    <label>Title</label>
                    <input formControlName="title" />
                    <error *ngIf="contentForm.get('title')?.invalid">
                        {{ getFieldError('title') }}
                    </error>
                </div>
                
                <div>
                    <label>Duration</label>
                    <input formControlName="duration" type="number" />
                    <error *ngIf="contentForm.get('duration')?.invalid">
                        {{ getFieldError('duration') }}
                    </error>
                </div>
                
                <button [disabled]="!contentForm.valid" type="submit">
                    Save Content
                </button>
            </form>
            
            <!-- List -->
            <table>
                <tr *ngFor="let item of contentList">
                    <td>{{ item.title }}</td>
                    <td>{{ item.duration }}</td>
                    <td>
                        <button (click)="editContent(item.id)">Edit</button>
                        <button (click)="deleteContent(item.id)">Delete</button>
                    </td>
                </tr>
            </table>
        </div>
    `
})
export class AdminContentComponent implements OnInit {
    contentForm!: FormGroup;
    contentList: Film[] = [];
    editingId: string | null = null;

    constructor(
        private fb: FormBuilder,
        private contentService: ContentService
    ) {
        this.initializeForm();
    }

    ngOnInit() {
        this.loadAllContent();
    }

    initializeForm() {
        this.contentForm = this.fb.group({
            title: [
                '',
                [
                    Validators.required,
                    CustomValidators.minLength(3),
                    CustomValidators.maxLength(255)
                ]
            ],
            description: [
                '',
                [
                    Validators.required,
                    CustomValidators.minLength(10)
                ]
            ],
            duration: [
                '',
                [
                    CustomValidators.numeric,
                    CustomValidators.minValue(1)
                ]
            ],
            releaseDate: [
                '',
                [
                    CustomValidators.dateFormat,
                    CustomValidators.pastDateValidator
                ]
            ],
            director: ['', Validators.required]
        });
    }

    loadAllContent() {
        this.contentService.getAllContent().subscribe({
            next: (data) => {
                this.contentList = data;
            },
            error: (error) => {
                console.error('Error loading content:', error);
            }
        });
    }

    saveContent() {
        if (!this.contentForm.valid) {
            this.markAllFieldsTouched();
            return;
        }

        const formData = this.contentForm.value;

        if (this.editingId) {
            this.contentService.updateFilm(this.editingId, formData)
                .subscribe({
                    next: () => {
                        alert('Updated successfully!');
                        this.loadAllContent();
                        this.contentForm.reset();
                    }
                });
        } else {
            this.contentService.createFilm(formData).subscribe({
                next: () => {
                    alert('Created successfully!');
                    this.loadAllContent();
                    this.contentForm.reset();
                }
            });
        }
    }

    editContent(id: string) {
        const content = this.contentList.find(c => c.id === id);
        if (content) {
            this.editingId = id;
            this.contentForm.patchValue(content);
        }
    }

    deleteContent(id: string) {
        if (confirm('Delete this content?')) {
            this.contentService.deleteContent(id).subscribe({
                next: () => {
                    alert('Deleted successfully!');
                    this.loadAllContent();
                }
            });
        }
    }

    getFieldError(fieldName: string): string {
        const control = this.contentForm.get(fieldName);
        if (!control?.errors || !control.touched) return '';

        if (control.errors['required']) return `${fieldName} is required`;
        if (control.errors['minlength']) 
            return `Minimum ${control.errors['minlength'].requiredLength} characters`;
        if (control.errors['maxlength'])
            return `Maximum ${control.errors['maxlength'].requiredLength} characters`;
        if (control.errors['numeric']) return 'Must be a number';
        if (control.errors['minvalue']) return 'Must be at least 1';
        if (control.errors['dateformat']) return 'Invalid date format';
        if (control.errors['pastdate']) return 'Cannot be in the past';

        return 'Invalid';
    }

    private markAllFieldsTouched() {
        Object.keys(this.contentForm.controls).forEach(key => {
            const control = this.contentForm.get(key);
            control?.markAsTouched();
        });
    }
}
```

### Backend - Film Service

```java
@Service
@RequiredArgsConstructor
public class ContentService {
    
    private final ContentRepository contentRepository;
    private final CategoryRepository categoryRepository;

    public FilmDTO createFilm(FilmDTO filmDTO, String username) {
        // Validate DTO input
        if (filmDTO.getTitle() == null || filmDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        if (filmDTO.getDurationInMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        // Convert DTO to Entity
        Film film = new Film();
        film.setTitle(filmDTO.getTitle());
        film.setDescription(filmDTO.getDescription());
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());
        film.setReleaseDate(filmDTO.getReleaseDate());
        film.setContentType("FILM");
        film.setAddedByUsername(username);
        
        // Set category
        if (filmDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(filmDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            film.setCategory(category);
        }
        
        // Save to database
        Film savedFilm = contentRepository.save(film);
        
        // Convert Entity back to DTO
        return convertToFilmDTO(savedFilm);
    }

    public List<ContentDTO> getAllContent() {
        return contentRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public FilmDTO updateFilm(String id, FilmDTO filmDTO) {
        Film film = contentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film not found"));
        
        film.setTitle(filmDTO.getTitle());
        film.setDescription(filmDTO.getDescription());
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());
        film.setReleaseDate(filmDTO.getReleaseDate());
        
        Film updated = contentRepository.save(film);
        return convertToFilmDTO(updated);
    }

    public void deleteContent(String id) {
        contentRepository.deleteById(id);
    }

    private FilmDTO convertToFilmDTO(Film film) {
        return FilmDTO.builder()
            .id(film.getId())
            .title(film.getTitle())
            .description(film.getDescription())
            .durationInMinutes(film.getDurationInMinutes())
            .director(film.getDirector())
            .releaseDate(film.getReleaseDate())
            .contentType("FILM")
            .categoryId(film.getCategory().getId())
            .categoryName(film.getCategory().getName())
            .addedByUsername(film.getAddedByUsername())
            .build();
    }
}
```

### Backend - Controller with Security

```java
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
    
    private final ContentService contentService;

    @PostMapping("/films")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FilmDTO> createFilm(
        @Valid @RequestBody FilmDTO filmDTO,
        Authentication authentication
    ) {
        String username = authentication.getName();
        FilmDTO created = contentService.createFilm(filmDTO, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        List<ContentDTO> content = contentService.getAllContent();
        return ResponseEntity.ok(content);
    }

    @PutMapping("/films/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FilmDTO> updateFilm(
        @PathVariable String id,
        @Valid @RequestBody FilmDTO filmDTO
    ) {
        FilmDTO updated = contentService.updateFilm(id, filmDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContent(@PathVariable String id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(
            Map.of("error", e.getMessage())
        );
    }
}
```

---

## Summary Table: All Validation Rules

| Field | Frontend | Backend | Rule |
|-------|----------|---------|------|
| Title | minLength(3) + maxLength(255) | @Size(1-255) | Required text |
| Description | minLength(10) | @Size(max=1000) | Optional text |
| Duration | numeric + minValue(1) | @Positive | Positive integer |
| Director | required | @NotBlank | Required text |
| ReleaseDate | dateFormat + pastDateValidator | - | Valid future date |
| CategoryId | required | @NotNull | Required reference |
| Username | minLength(3) + maxLength(50) | @Size(3-50) | Login name |
| Email | email | @Email | Valid email format |

---

## Glossary

| Term | Meaning |
|------|---------|
| DTO | Data Transfer Object - class to transfer data |
| Entity | Database model class |
| Repository | Interface for database operations |
| Service | Business logic layer |
| Controller | API endpoint handler |
| @Valid | Triggers validation on DTO |
| @PreAuthorize | Controls access to methods |
| @NotBlank | Validation: field cannot be empty |
| @Pattern | Validation: must match regex |
| BCrypt | Password hashing algorithm |
| JWT | JSON Web Token for authentication |
| RBAC | Role-Based Access Control |
| Authentication | Verifying user identity |
| Authorization | Checking user permissions |

---

## Next Steps

1. **Study the code examples** - Understand how everything works together
2. **Create DTOs** - Practice creating validation rules
3. **Implement Services** - Write business logic
4. **Create Controllers** - Build API endpoints
5. **Setup Security** - Configure authentication & authorization
6. **Build Frontend** - Create forms with validation
7. **Test Everything** - Run through all test scenarios
8. **Deploy** - Push to production

---

**This guide provides a complete understanding of:**
- ✅ What DTOs are and why they matter
- ✅ How validation works on frontend and backend
- ✅ How Spring Security protects your application
- ✅ Step-by-step implementation guide
- ✅ Real code examples you can use

**Good luck with your SMGO project!** 🚀
