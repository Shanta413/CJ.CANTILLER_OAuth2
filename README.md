# 🌐 OAuth2 Profile Application



---

## ✨ Features

- 🔐 OAuth2 authentication with Google and GitHub
- 👤 User profile management
- 💾 H2 in-memory database for data persistence
- 📊 Built-in H2 console for database inspection


---

🧱 Architecture Overview
🛠️ Technologies Used
Layer	Technology
Backend	Spring Boot 3.5+, Java 21
Security	Spring Security OAuth2 Client
OAuth2	Google, GitHub
Database	H2 (in-memory, dev mode)
Frontend	Static HTML/CSS/JS (/static folder) // will update in the future to ReactJS

```bash
+--------------------+
|    User Browser    |
+--------------------+
          |
          v
+------------------------------+
| Static Frontend (HTML/CSS/JS)|
+------------------------------+
          |
          v
+-----------------------------------------+
| Spring Boot Backend (OAuth Login Demo)  |
|                                         |
|  +-----------------------------------+  |
|  | Spring Security OAuth2 Client     |  |
|  |  - Handles login redirects        |  |
|  |  - Fetches access tokens          |  |
|  |  - Loads user info (OIDC/OAuth2)  |  |
|  +----------------+------------------+  |
|                       |                 |
|                       v                 |
|  +-----------------------------------+  |
|  | CustomOAuth2UserService           |  |
|  |  - Maps user info to DB record    |  |
|  |  - Links AuthProvider entries     |  |
|  +----------------+------------------+  |
|                       |                 |
|                       v                 |
|  +-----------------------------------+  |
|  | DelegatingOidcUserService         |  |
|  |  - Wraps Google OIDC login        |  |
|  |  - Returns compliant OidcUser     |  |
|  +-----------------------------------+  |
+-----------------------------------------+
          |
          v
+------------------------------+
|     H2 In-Memory Database    |
|  ┌────────────────────────┐  |
|  │      users table       │  |
|  │ ─ id, email, name...   │  |
|  └────────────────────────┘  |
|  ┌────────────────────────┐  |
|  │  auth_providers table  │  |
|  │ ─ provider, sub, FK... │  |
|  └────────────────────────┘  |
+------------------------------+
```



## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
```

### 2. Import Project

**Using IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. Select `File` → `Open`
3. Navigate to the project directory
4. Click `OK` and let IntelliJ import Maven dependencies automatically

**Using Eclipse:**
1. Select `File` → `Import` → `Existing Maven Projects`
2. Browse to the project directory
3. Click `Finish`

---

## 🔐 OAuth2 Configuration


###  Go to application.properties — Client ID & Client Secret

📝 Steps


Open the file:


src/main/resources/application.properties






Replace (or uncomment) `YOUR_*_CLIENT_ID` and `YOUR_*_CLIENT_SECRET` with your actual credentials.


Scroll to:

Lines 59–60 → Google 
Lines 67–68 → GitHub

## For Google
client id :
##  677025675545-rulp0k3cvmgrome4gudhhiugqbsvn2u3.apps.googleusercontent.com ##

client secret: 

## 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 ##

## For Github
client id :
## Ov23liV2r7mjBiICaU3S ##

client secret: 
## c49f0d79a1ae168d9d531fea57f7d02d22391a08 ##


---

## 🏃 Running the Application

### Method 1: Run from IntelliJ IDEA (Recommended)

1. Open the project in IntelliJ IDEA
2. Locate the main class:
   ```
   com.cantiller.oauth2_profile_app.Oauth2ProfileAppApplication
   ```
3. Right-click on the class → `Run 'Oauth2ProfileAppApplication.main()'`



**Access the application at:**
👉 **http://localhost:8080**

---

## 💾 Database Access

### H2 Console

This application uses an **in-memory H2 database** for development purposes.

**Access the H2 Console:**
👉 **http://localhost:8080/h2-console**

**Login Credentials:**

| Field | Value |
|-------|-------|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:oauth2db` |
| User Name | `sa` |
| Password | *(leave empty)* |

### SQL Queries

```sql
-- View all registered users
SELECT * FROM USERS;

-- View authentication providers
SELECT * FROM AUTHPROVIDER;
```

> 📝 **Note:** Data is stored in-memory and will be lost when the application stops.

---
