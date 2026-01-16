# Spring Boot Fintech Wallet Backend

Dokumen ini berfungsi sebagai **blueprint teknis** untuk proyek backend Spring Boot yang mensimulasikan sistem fintech (digital wallet). Cocok untuk:

* Portfolio profesional
* Bahan belajar Spring Boot + Spring Core (implisit)
* Persiapan interview backend fintech

---

## 1. Breakdown API Contract (Request / Response)

### 1.1 Authentication

#### POST /api/auth/register

**Request**

```json
{
  "email": "user@mail.com",
  "password": "StrongPassword123"
}
```

**Response**

```json
{
  "userId": "uuid",
  "email": "user@mail.com"
}
```

---

#### POST /api/auth/login

**Request**

```json
{
  "email": "user@mail.com",
  "password": "StrongPassword123"
}
```

**Response**

```json
{
  "accessToken": "jwt-token",
  "expiresIn": 3600
}
```

---

### 1.2 Wallet

#### POST /api/wallets

Create wallet for authenticated user

**Response**

```json
{
  "walletId": "uuid",
  "balance": 0
}
```

---

#### GET /api/wallets/{walletId}

**Response**

```json
{
  "walletId": "uuid",
  "balance": 150000,
  "currency": "IDR"
}
```

---

### 1.3 Transaction

#### POST /api/transactions/credit

**Headers**

```
Idempotency-Key: <uuid>
```

**Request**

```json
{
  "walletId": "uuid",
  "amount": 100000
}
```

**Response**

```json
{
  "transactionId": "uuid",
  "status": "SUCCESS",
  "balanceAfter": 250000
}
```

---

#### POST /api/transactions/debit

**Headers**

```
Idempotency-Key: <uuid>
```

**Request**

```json
{
  "walletId": "uuid",
  "amount": 50000
}
```

**Response**

```json
{
  "transactionId": "uuid",
  "status": "SUCCESS",
  "balanceAfter": 200000
}
```

---

### 1.4 Transaction History

#### GET /api/transactions?walletId={id}

**Response**

```json
[
  {
    "transactionId": "uuid",
    "type": "CREDIT",
    "amount": 100000,
    "status": "SUCCESS",
    "createdAt": "2026-01-01T10:00:00"
  }
]
```

---

## 2. Entity & Database Schema (Fintech Style)

### 2.1 User

```sql
users
- id (UUID, PK)
- email (VARCHAR, UNIQUE)
- password_hash
- created_at
```

---

### 2.2 Wallet

```sql
wallets
- id (UUID, PK)
- user_id (FK -> users.id)
- balance (BIGINT)
- currency (VARCHAR)
- created_at
```

> Balance disimpan dalam **integer (BIGINT)** untuk menghindari floating point error

---

### 2.3 Transaction

```sql
transactions
- id (UUID, PK)
- wallet_id (FK)
- type (CREDIT | DEBIT)
- amount (BIGINT)
- status (PENDING | SUCCESS | FAILED)
- idempotency_key (UUID)
- created_at
```

---

### 2.4 Ledger / Audit Log

```sql
ledger_entries
- id (UUID, PK)
- transaction_id (FK)
- balance_before
- balance_after
- created_at
```

> Ledger penting untuk **audit & compliance fintech**

---

## 3. Spring Boot Skeleton Setup

### 3.1 Project Structure

```
com.company.wallet-service
├── WalletApplication.java
├── domain
│   ├── model
│   ├── repository
│   └── service
├── application
│   ├── dto
│   └── usecase
├── infrastructure
│   ├── web
│   ├── persistence
│   └── security
└── config
```

---

### 3.2 pom.xml (Core Dependencies)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

### 3.3 application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wallet_db
    username: wallet
    password: wallet

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

  flyway:
    enabled: true
```

---

## Dependencies

✅ Core

- Spring Web

- Spring Data JPA

- Spring Security

- PostgreSQL Driver

- Flyway Migration

- Validation

✅ Dev & Test

- Spring Boot DevTools

### TODO

- [✔] Auth
- [✔] JWT
- [✔] /me
↓
- [ ] Role & Authorization
- [ ] Wallet
- [ ] Ledger Transaction
- [ ] Transfer / Topup
- [ ] Idempotency
- [ ] Locking
- [ ] Audit Log
