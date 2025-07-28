# sms-service API

## Overview
This document provides an overview and usage instructions for the sms-service API. The API allows user authentication, SMS management, user profile access, and SMS metrics reporting.

There is a swagger page that documents every possible API call:
http://localhost:8080/q/swagger-ui

---

## Building

### Build jars with dependencies
```bash
mvn clean package -Dquarkus.package.type=uber-jar
```
### Build docker images and start services
```bash
docker-compose -f docker-compose.local.yml up --build
```
### Start services by using prebuilt docker images
```bash
docker-compose -f docker-compose.remote.yml up --build
```

---

## API calls

### POST /auth/login
Authenticate user and return a JWT token.

### GET /metrics
Retrieve aggregated SMS metrics.

### GET /profile
Get user profile extracted from JWT claims.

### GET /sms/listIncoming
List incoming SMS messages for authenticated user.

### GET /sms/listOutgoing
List outgoing SMS messages for authenticated user.

### POST /sms/send
Send an SMS message if authorized and valid.

---

## Architecture

![Alt text](docs/arch-idea.png)
![Alt text](docs/arch-packages.png)
