#!/bin/bash
# Convenience scripts for running tests

if [ -z "$1" ]; then
    echo "Usage: ./test.sh [command]"
    echo ""
    echo "Commands:"
    echo "  unit           - Run unit tests"
    echo "  build          - Build without tests (fastest)"
    echo "  build-test     - Build with tests"
    echo ""
    exit 1
fi

case "$1" in
    unit)
        echo "Running backend unit tests..."
        mvn test -Ptest
        exit $?
        ;;
    build)
        echo "Building backend without tests..."
        mvn clean package
        exit $?
        ;;
    build-test)
        echo "Building backend with tests..."
        mvn clean package -Ptest
        exit $?
        ;;
    *)
        echo "Unknown command: $1"
        exit 1
        ;;
esac
