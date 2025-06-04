# FinanceDKX

FinanceDKX is a Spring Boot project that serves a basic finance-themed website. It includes user signup and login pages, a dashboard, and a Python script for creating social media content.

## Prerequisites

- Java 17+
- Maven 3.8+

## Running Locally

To start the application locally and see your changes:

```bash
./mvnw spring-boot:run
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

The project uses an in-memory H2 database. Any data is reset each time you restart the server.

## Building a Package

To build a jar file:

```bash
./mvnw clean package
```

The resulting jar will be in the `target/` directory.

## Repository Cleanup

This repository previously contained compiled classes and database files. These have been removed and added to `.gitignore` so future commits remain clean.
