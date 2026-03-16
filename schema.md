# MongoDB Schema

## Book Review Admin System

---

## 1. Overview

This document defines the MongoDB schema for the **Book Review Admin System**.

### Design Goals

* Support **Spring Security Basic authentication**
* Enable **admin-authored book reviews**
* Keep schema simple and extensible
* No user registration (admin-only system)
* Dynamic category management

---

## 2. Collections Overview

| Collection Name | Description                             | Entity Class         |
| --------------- | --------------------------------------- | -------------------- |
| `users`         | Stores admin login credentials          | `UserEntity`         |
| `book_reviews`  | Stores book reviews created by admins   | `BookReviewEntity`   |
| `categories`    | Stores book categories/tags             | `CategoryEntity`     |

---

## 3. `users` Collection

### 3.1 Purpose

Stores administrator authentication data.

* Used by **Spring Security**
* Admin accounts are **manually created or via CommandLineRunner**
* Role-based access control (default role: `ROLE_ADMIN`)

---

### 3.2 Document Structure

```json
{
  "_id": "ObjectId",
  "username": "NilKim",
  "password": "{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  "role": "ROLE_ADMIN",
  "createdAt": "2026-03-16T18:31:00Z",
  "updatedAt": "2026-03-16T18:31:00Z"
}
```

---

### 3.3 Field Definitions

| Field        | Type     | Required | Description                     |
|--------------| -------- | -------- | ------------------------------- |
| `_id`        | ObjectId | Yes      | Primary key                     |
| `username`   | String   | Yes      | Login identifier (unique)       |
| `password`   | String   | Yes      | Encoded password (e.g., BCrypt) |
| `role`       | String   | Yes      | User role (e.g., `ROLE_ADMIN`)  |
| `createdAt`  | Date     | Yes      | Account creation timestamp      |
| `updatedAt`  | Date     | Yes      | Last modification timestamp     |

---

### 3.4 Indexes

```js
{
  username: 1
}
```

* Unique index on `username`
* Prevents duplicate admin accounts

---

## 4. `book_reviews` Collection

### 4.1 Purpose

Stores book reviews authored by admins.

* Created via **admin panel**
* Supports multi-category tagging
* Stores cover image filenames
* Public website consumes read-only APIs

---

### 4.2 Document Structure

```json
{
  "_id": "ObjectId",
  "adminUserId": "ObjectId",
  "title": "Atomic Habits",
  "author": "James Clear",
  "rating": 4.5,
  "page": 320,
  "language": "English",
  "categories": ["Self-Help", "Psychology"],
  "publishedAt": "2026-01-18T09:00:00Z",
  "coverImage": "atomic-habits.jpg",
  "contents": "<p>This book explains...</p>",
  "excerpt": "A short summary of the review.",
  "createdAt": "2026-01-18T09:00:00Z",
  "updatedAt": "2026-01-18T09:00:00Z"
}
```

---

### 4.3 Field Definitions

| Field           | Type     | Required | Description                      |
| --------------- | -------- | -------- | -------------------------------- |
| `_id`           | ObjectId | Yes      | Primary key                      |
| `adminUserId`   | ObjectId | Yes      | Reference to `users._id`         |
| `title`         | String   | Yes      | Book title                       |
| `author`        | String   | Yes      | Book author                      |
| `rating`        | Number   | Yes      | Rating (e.g., 0.0 - 5.0)         |
| `page`          | Number   | Yes      | Number of pages                  |
| `language`      | String   | Yes      | Book language                    |
| `categories`    | Array    | Yes      | List of category names (Strings) |
| `publishedAt`   | Date     | Yes      | Review publication date          |
| `coverImage`    | String   | Yes      | Filename of the cover image      |
| `contents`      | String   | Yes      | Review body content              |
| `excerpt`       | String   | No       | Brief summary of the review      |
| `createdAt`     | Date     | Yes      | Creation timestamp               |
| `updatedAt`     | Date     | Yes      | Last update timestamp            |

---

### 4.4 Indexes

```js
{
  publishedAt: -1,
  adminUserId: 1
}
```

* `publishedAt`: for public listing (latest first)
* `adminUserId`: for admin ownership tracking

---

## 5. `categories` Collection

### 5.1 Purpose

Stores a master list of categories used across book reviews.

---

### 5.2 Document Structure

```json
{
  "_id": "ObjectId",
  "name": "Self-Help",
  "createdAt": "2026-01-18T09:00:00Z",
  "updatedAt": "2026-01-18T09:00:00Z"
}
```

---

### 5.3 Field Definitions

| Field        | Type     | Required | Description                 |
|--------------| -------- | -------- | --------------------------- |
| `_id`        | ObjectId | Yes      | Primary key                 |
| `name`       | String   | Yes      | Category name (unique)      |
| `createdAt`  | Date     | Yes      | Creation timestamp          |
| `updatedAt`  | Date     | Yes      | Last modification timestamp |

---

## 6. Collection Relationships

### Logical Relationship (Application-Level)

```text
users (1) ──── (N) book_reviews
```

* `book_reviews.adminUserId` → `users._id`
* No MongoDB DBRef (resolved in application layer using Spring Data MongoDB)

---

## 7. Security & Integrity Notes

* Passwords are **never stored in plain text** (using Spring Security's `PasswordEncoder`)
* Authentication is handled via **HTTP Basic** (managed by `SecurityConfig`)
* `adminUserId` is resolved from the authenticated `UserDetails` in `ReviewController`
* Entities include `createdAt` and `updatedAt` via Spring Data's `@CreatedDate` and `@LastModifiedDate` annotations.

---

## 8. Future Extensions (Optional)

* `isPublished` flag for draft status
* `slug` field for SEO-friendly URLs
* Audit log collection for tracking admin actions
