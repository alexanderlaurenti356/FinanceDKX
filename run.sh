#!/usr/bin/env bash
# Simple helper to run FinanceDKX using the Maven wrapper
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
"$SCRIPT_DIR/mvnw" spring-boot:run
