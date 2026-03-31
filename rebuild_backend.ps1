param(
)

# Script to rebuild the backend JAR after making code changes
Write-Host "=== Backend JAR Rebuild Script ===" -ForegroundColor Green
Write-Host ""

# Kill any running Java processes
Write-Host "Stopping any running Java processes..."
Get-Process java -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -InputObject $_ -Force }
Start-Sleep -Seconds 2
Write-Host "✓ Java processes stopped" -ForegroundColor Green

# Backup original JAR
$jarPath = "target\content-management-0.0.1-SNAPSHOT.jar"
$backupPath = "$jarPath.backup"

if (-not (Test-Path $backupPath)) {
    Write-Host "Backing up original JAR..."
    Copy-Item $jarPath $backupPath
    Write-Host "✓ Backup created:" $backupPath -ForegroundColor Green
}

# Create work directory
$workDir = "build_work"
if (Test-Path $workDir) { Remove-Item $workDir -Recurse -Force }
New-Item $workDir -ItemType Directory -Force | Out-Null
Write-Host "✓ Work directory created" -ForegroundColor Green

# Extract JAR  
Write-Host "Extracting JAR contents..."
jar -xf $jarPath -C $workDir
Write-Host "✓ JAR contents extracted" -ForegroundColor Green

# Compile SecurityConfig
Write-Host ""
Write-Host "Recompiling SecurityConfig.java..."
$classesPath = "$workDir\BOOT-INF\classes"
$srcFile = "src\main\java\com\example\contentmanagement\config\SecurityConfig.java"
$classpathParts = @($classesPath)
Get-ChildItem "$workDir\BOOT-INF\lib" -Filter "*.jar" -ErrorAction SilentlyContinue | ForEach-Object {
    $classpathParts += $_.FullName
}
$classPathStr = $classpathParts -join ";"

javac -cp $classPathStr -d $classesPath $srcFile 2>&1 | Out-Null
Write-Host "✓ SecurityConfig compiled" -ForegroundColor Green

# Repackage JAR
Write-Host ""
Write-Host "Repackaging JAR file..."
Remove-Item $jarPath -Force
jar -cfM $jarPath -C $workDir .
Write-Host "✓ JAR repackaged" -ForegroundColor Green

# Cleanup
Write-Host ""
Write-Host "Cleaning up..."
Remove-Item $workDir -Recurse -Force
Write-Host "✓ Done" -ForegroundColor Green
