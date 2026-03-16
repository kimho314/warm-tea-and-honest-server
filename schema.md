좋아, 지금까지 확정된 요구사항을 **모두 반영한 최종 MongoDB Schema 파일**를 하나의 문서 형태로 정리해줄게.
이 파일은 그대로 `schema.md` 또는 `mongodb-schema.md`로 저장해서 **설계 기준 문서(Single Source of Truth)**로 사용하면 돼.

아래 내용에는 ✅ `admin_users` 컬렉션 + ✅ `book_reviews` 컬렉션 + ✅ 둘 사이의 관계가 모두 포함돼 있어.

---

# MongoDB Schema

## Book Review Admin System

---

## 1. Overview

This document defines the MongoDB schema for the **Book Review Admin System**.

### Design Goals

* Support **Spring Security form login**
* Enable **admin-authored book reviews**
* Keep schema simple and extensible
* No user registration (admin-only system)

---

## 2. Collections Overview

| Collection Name | Description                           |
| --------------- | ------------------------------------- |
| `admin_users`   | Stores admin login credentials        |
| `book_reviews`  | Stores book reviews created by admins |

---

## 3. `admin_users` Collection

### 3.1 Purpose

Stores administrator authentication data.

* Used by **Spring Security (form login)**
* Admin accounts are **manually created by developer**
* No signup / reset / role hierarchy

---

### 3.2 Document Structure

```json
{
  "_id": "ObjectId",
  "username": "admin",
  "password": "$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXX",
  "createdAt": "2026-01-20T10:30:00Z"
}
```

---

### 3.3 Field Definitions

| Field        | Type     | Required | Description                |
|--------------| -------- | -------- | -------------------------- |
| `_id`        | ObjectId | Yes      | Primary key                |
| `username`   | String   | Yes      | Login identifier           |
| `password`   | String   | Yes      | BCrypt-hashed password     |
| `createdAt`  | Date     | Yes      | Account creation timestamp |

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
* Content is generated from **Word document → HTML**
* Public website consumes read-only APIs

---

### 4.2 Document Structure

```json
{
  "_id": "ObjectId",
  "adminUserId": "ObjectId",
  "title": "Atomic Habits",
  "author": "James Clear",
  "rating": 4,
  "reviewDate": "2026-01-18",
  "coverImageUrl": "/images/atomic-habits.jpg",
  "contentHtml": "<p>This book explains...</p>",
  "createdAt": "2026-01-18T09:00:00Z",
  "updatedAt": "2026-01-18T09:00:00Z"
}
```

---

### 4.3 Field Definitions

| Field           | Type     | Required | Description                    |
| --------------- | -------- | -------- | ------------------------------ |
| `_id`           | ObjectId | Yes      | Primary key                    |
| `adminUserId`   | ObjectId | Yes      | Reference to `admin_users._id` |
| `title`         | String   | Yes      | Book title                     |
| `author`        | String   | Yes      | Book author                    |
| `rating`        | Number   | Yes      | Rating (1–5)                   |
| `reviewDate`    | Date     | Yes      | Review publication date        |
| `coverImageUrl` | String   | No       | Book cover image path          |
| `contentHtml`   | String   | Yes      | Review body (HTML)             |
| `createdAt`     | Date     | Yes      | Creation timestamp             |
| `updatedAt`     | Date     | Yes      | Last update timestamp          |

---

### 4.4 Indexes

```js
{
  reviewDate: -1,
  adminUserId: 1
}
```

* `reviewDate`: for public listing (latest first)
* `adminUserId`: for admin ownership tracking

---

## 5. Collection Relationship

### Logical Relationship (Application-Level)

```text
admin_users (1) ──── (N) book_reviews
```

* `book_reviews.adminUserId` → `admin_users._id`
* No MongoDB DBRef (resolved in application layer)

---

## 6. Security & Integrity Notes

* Passwords are **never stored in plain text**
* Admin identity is resolved from **HTTP session**
* `adminUserId` is set server-side (not from client)

---

## 7. Future Extensions (Optional)

* `isPublished` flag for drafts
* `slug` field for SEO-friendly URLs
* Audit log collection for admin actions

---
