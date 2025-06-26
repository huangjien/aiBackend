#!/bin/bash

# Test runner script for AI Backend application
# This script can be used if Gradle test task has compatibility issues

echo "AI Backend - Manual Test Runner"
echo "================================"

# Set the project directory
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Compile the main application first
echo "Compiling main application..."
./gradlew compileJava
if [ $? -ne 0 ]; then
    echo "Failed to compile main application"
    exit 1
fi

# Compile test classes
echo "Compiling test classes..."
./gradlew compileTestJava
if [ $? -ne 0 ]; then
    echo "Failed to compile test classes"
    exit 1
fi

echo "Compilation successful!"
