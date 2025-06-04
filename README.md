# FinanceDKX

FinanceDKX is a Spring Boot project that aims to become a **democratic finance hub**. It currently provides basic pages for signup, login and a dashboard along with a Python script for creating social media content. The goal is to connect traditional institutions, investments and crypto in one place.

### Features

- User registration and login with hashed passwords
- Dashboard and leaderboard sample pages
- REST APIs for token statistics and account data
- Python automation script (`createcontent.py`) for generating marketing posts
- Financial platforms leaderboard showcasing banks, brokers and credit cards
- New `/api/institutions` endpoint returning sample institution data

## Prerequisites

- Java 17+
- Maven 3.8+

## Running Locally

To start the application locally and see your changes you can use the provided
`run.sh` script (or `run-dkx.bat` on Windows):

```bash
./run.sh
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

The project uses an in-memory H2 database. Any data is reset each time you restart the server.

## Building a Package

To build a jar file:

```bash
./mvnw clean package
```

The resulting jar will be in the `target/` directory.

## Running Tests

Unit tests can be executed with Maven. On the first run Maven needs to download
dependencies from the internet:

```bash
./mvnw test
```

## Repository Cleanup

This repository previously contained compiled classes and database files. These have been removed and added to `.gitignore` so future commits remain clean.
