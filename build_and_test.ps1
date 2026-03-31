# Kill all Java processes
Get-Process java -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.Id -Force }
Start-Sleep -Seconds 3

# Build
Write-Output "Building backend..."
cd "C:\Users\SBS\Desktop\SMGO\backend"
& mvn clean package -DskipTests | Write-Output

# Start backend in a new window
Write-Output "Starting backend..."
$backendCmd = "java --add-opens java.base/sun.security.util=ALL-UNNAMED -jar 'C:\Users\SBS\Desktop\SMGO\backend\target\content-management-0.0.1-SNAPSHOT.jar'"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $backendCmd

Start-Sleep -Seconds 5

# Test
Write-Output "Testing API..."

# Login
$loginResp = Invoke-WebRequest "http://localhost:8090/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}' -UseBasicParsing -ErrorAction SilentlyContinue
if ($loginResp.StatusCode -eq 200) {
    Write-Output "✅ Login successful"
    $token = ($loginResp.Content | ConvertFrom-Json).token
    
    # Get films
    $filmsResp = Invoke-WebRequest "http://localhost:8090/api/contents" -UseBasicParsing -ErrorAction SilentlyContinue
    if ($filmsResp.StatusCode -eq 200) {
        Write-Output "✅ GET successful"
        $films = $filmsResp.Content | ConvertFrom-Json
        $film = $films | Where-Object { $_.contentType -eq "FILM" } | Select-Object -First 1
        
        if ($film) {
            Write-Output "🎬 Testing UPDATE: $($film.title)"
            $updateData = @{
                title = "UPDATED: $($film.title)"
                description = "Updated"
                releaseDate = "2024-03-29"
                categoryId = $film.categoryId
                durationInMinutes = 999
                director = " Updated Director"
            } | ConvertTo-Json
            
            $updateResp = Invoke-WebRequest "http://localhost:8090/api/contents/films/$($film.id)" -Method PUT -ContentType "application/json" -Headers @{"Authorization"="Bearer $token"} -Body $updateData -UseBasicParsing -ErrorAction SilentlyContinue
            if ($updateResp.StatusCode -eq 200) {
                Write-Output "✅ UPDATE successful!"
                Write-Output ($updateResp.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2)
            } else {
                Write-Output "❌ UPDATE failed: $($updateResp.StatusCode)"
            }
        }
    }
}
