# Backend Run Script
param(
    [switch]$Build = $true,
    [switch]$Debug = $false
)

# Set environment
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:M2_HOME = "C:\Users\azuz\.maven\maven-3.9.14(1)"
$mavenBin = "C:\Users\azuz\.maven\maven-3.9.14(1)\bin\mvn.cmd"

$backendDir = "C:\Users\azuz\Desktop\SMGO\backend"
Set-Location $backendDir

Write-Host "═══════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  SMGO Backend Launcher" -ForegroundColor Cyan
Write-Host "═══════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Maven Home: $env:M2_HOME" -ForegroundColor Green
Write-Host ""

if ($Build) {
    Write-Host "Building backend..." -ForegroundColor Yellow
    & $mavenBin clean install -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Build failed!" -ForegroundColor Red
        exit 1
    }
    Write-Host "Build successful" -ForegroundColor Green
}

Write-Host ""
Write-Host "Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host "Access at: http://localhost:8090" -ForegroundColor Cyan
Write-Host "Swagger UI: http://localhost:8090/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""

if ($Debug) {
    & $mavenBin spring-boot:run -X
} else {
    $startupMessageShown = $false
    & $mavenBin spring-boot:run 2>&1 | ForEach-Object {
        $line = $_.ToString()

        # Hide verbose initialization lines from startup output.
        if ($line -match '(?i)initializ') {
            return
        }

        Write-Host $line

        if (-not $startupMessageShown -and $line -match 'Started .* in .* seconds') {
            Write-Host "Application started successfully." -ForegroundColor Green
            $startupMessageShown = $true
        }
    }
}
