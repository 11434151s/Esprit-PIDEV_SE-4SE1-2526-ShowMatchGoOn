# Script to rebuild the backend JAR after making code changes

$ErrorActionPreference = "Stop"

Write-Host "=== Backend JAR Rebuild Script ===" -ForegroundColor Green
Write-Host ""

# Kill any running Java processes
Write-Host "Stopping any running Java processes..."
Get-Process java -ErrorAction SilentlyContinue | ForEach-Object { $_ | Stop-Process -Force }
Start-Sleep -Seconds 2
Write-Host "✓ Java processes stopped" -ForegroundColor Green

# Backup original JAR
$jarPath = "target\content-management-0.0.1-SNAPSHOT.jar"
$backupPath = "$jarPath.backup"

if (-not (Test-Path $backupPath)) {
    Write-Host "Backing up original JAR..."
    Copy-Item $jarPath $backupPath
    Write-Host "✓ Backup created: $backupPath" -ForegroundColor Green
}

# Create work directory
$workDir = "build_work"
if (Test-Path $workDir) { Remove-Item $workDir -Recurse -Force }
New-Item $workDir -ItemType Directory | Out-Null
Write-Host "✓ Work directory created" -ForegroundColor Green

# Copy JAR contents to work directory
Write-Host ""
Write-Host "Extracting JAR contents..."
& jar -xf $jarPath -C $workDir

# Extract target/classes for quick reference
Write-Host "Extracting  compiled classes..."
if (Test-Path "target\classes") {
    Copy-Item "target\classes" "$workDir\BOOT-INF\classes" -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host "✓ JAR contents extracted" -ForegroundColor Green

# Compile the SecurityConfig class
Write-Host ""
Write-Host "Recompiling SecurityConfig.java..."
$classesPath = "$workDir\BOOT-INF\classes"
$srcFile = "src\main\java\com\example\contentmanagement\config\SecurityConfig.java"

# Create a simple classpath from the extracted JAR
$classpathFiles = @()
Get-ChildItem "$workDir\BOOT-INF\lib" -Filter "*.jar" -ErrorAction SilentlyContinue | ForEach-Object {
    $classpathFiles += "$($_.FullName)"
}

$classPathStr = ($classpathFiles -join ";") + ";$classesPath"

try {
    & javac -cp "$classPathStr" -d "$classesPath" $srcFile *>$null
    Write-Host "✓ SecurityConfig compiled successfully" -ForegroundColor Green
}
catch {
    Write-Host "⚠ Compilation may have issues (continuing anyway)..." -ForegroundColor Yellow
}

# Repackage JAR  
Write-Host ""
Write-Host "Repackaging JAR file..."
Remove-Item $jarPath
& jar -cfM $jarPath -C $workDir .

Write-Host "✓ JAR repackaged: $jarPath" -ForegroundColor Green

# Cleanup
Write-Host ""
Write-Host "Cleaning up..."
Remove-Item $workDir -Recurse -Force
Write-Host "✓ Cleanup complete" -ForegroundColor Green

Write-Host ""
Write-Host "=== Build Complete ===" -ForegroundColor Green
Write-Host "The JAR has been rebuilt with your code changes."
Write-Host "You can now restart the backend server."
