@echo off
REM Convenience scripts for running tests

if "%1"=="" (
    echo Usage: test.bat [command]
    echo.
    echo Commands:
    echo   unit           - Run unit tests
    echo   build          - Build without tests (fastest)
    echo   build-test     - Build with tests
    echo.
    exit /b 1
)

if "%1"=="unit" (
    echo Running backend unit tests...
    mvn test -Ptest
    exit /b %errorlevel%
)

if "%1"=="build" (
    echo Building backend without tests...
    mvn clean package
    exit /b %errorlevel%
)

if "%1"=="build-test" (
    echo Building backend with tests...
    mvn clean package -Ptest
    exit /b %errorlevel%
)

echo Unknown command: %1
exit /b 1
