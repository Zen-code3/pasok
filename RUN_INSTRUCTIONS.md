# How to Run Pasok Application

## Prerequisites
- **NetBeans 8.2** (or later) - You have the installer in your Videos folder
- **JDK 8** - Usually bundled with NetBeans or install separately
- **SQLite JDBC** - Already in `lib/sqlite-jdbc-3.44.1.0.jar`

## Run from NetBeans

1. Open NetBeans IDE
2. **File → Open Project** → select the `pasok` folder
3. Right-click the project → **Clean and Build**
4. Right-click the project → **Run**

The application starts at the **Landing Page**.

## Button & Flow Summary

### Landing Page
- **About us** / **Product** / **Contacts** – Menu highlighting
- **EXPLORE NOW** → Opens Login

### Login
- **Login** – Authenticates and routes:
  - **Admin** → Admin Dashboard
  - **User** → User Dashboard
- **Don't have an account? Register** → Register page

### Register
- **Register** – Creates new user (status: pending)
- **Back to Login** → Returns to Login

### Admin Dashboard
- **Dashboard** – Stays on dashboard
- **Customer** – Placeholder
- **Products** – Placeholder
- **Orders** – Placeholder
- **Reports** – Placeholder
- **User** → User Management (list users, approve pending)
- **Profile** → Admin Profile
- **LOG OUT** → Returns to Login

### Admin Profile
- Same menu as Admin Dashboard
- Shows admin profile data

### User Management (User button)
- **Approve Selected** – Approve pending user
- **Back to Dashboard** → Returns to Admin Dashboard

### User Dashboard
- **Dashboard** / **Customer** / **Products** / **Orders** – Placeholders
- **LOG OUT** → Returns to Login

## Default Admin Login
- **Email:** admin@pasok.com  
- **Password:** admin123

## Database
- `user.db` is created in the project root on first run
- New users need admin approval before they can log in
