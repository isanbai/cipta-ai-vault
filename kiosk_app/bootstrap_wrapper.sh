#!/usr/bin/env bash
set -e
# Generate Gradle wrapper locally (requires Gradle installed)
gradle wrapper --gradle-version 8.7
chmod +x ./gradlew
echo "Wrapper generated. Now run: ./gradlew assembleDebug"
