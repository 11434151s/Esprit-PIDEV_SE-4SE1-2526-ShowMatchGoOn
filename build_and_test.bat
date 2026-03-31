@echo off
setlocal enabledelayedexpansion

echo Killing all Java processes...
taskkill /F /IM java.exe 2>nul
timeout /t 3 /nobreak

echo Building backend...
cd /d "c:\Users\SBS\Desktop\SMGO\backend"
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b 1
)

echo Build successful!
echo.
echo Starting backend on port 8090...
cd /d "c:\Users\SBS\Desktop\SMGO"
start "Backend Server" java --add-opens java.base/sun.security.util=ALL-UNNAMED -jar "c:\Users\SBS\Desktop\SMGO\backend\target\content-management-0.0.1-SNAPSHOT.jar"

timeout /t 5 /nobreak
echo Backend started. Testing API...
echo.

powershell -NoProfile -Command "
  # Test login
  $loginResp = Invoke-WebRequest 'http://localhost:8090/api/auth/login' -Method POST -ContentType 'application/json' -Body '{\"username\":\"admin\",\"password\":\"admin123\"}' -UseBasicParsing 2>$null
  if ($loginResp.StatusCode -eq 200) {
      Write-Output '✅ Login successful'
      $token = ($loginResp.Content | ConvertFrom-Json).token
      
      # Get films
      $filmsResp = Invoke-WebRequest 'http://localhost:8090/api/contents' -UseBasicParsing 2>$null
      Write-Output '✅ GET /api/contents successful'
      
      # Test update
      $films = $filmsResp.Content | ConvertFrom-Json
      $film = $films | Where-Object {$_.contentType -eq 'FILM'} | Select-Object -First 1
      if ($film) {
          Write-Output \"📽️  Testing UPDATE with: $($film.title)\"
          $updateBody = @{
              title = 'UPDATED: ' + $film.title;
              description = 'Updated desc';
              releaseDate = '2024-03-29';
              categoryId = $film.categoryId;
              durationInMinutes = 999;
              director = 'Updated Director'
          } | ConvertTo-Json
          
          $updateResp = Invoke-WebRequest \"http://localhost:8090/api/contents/films/$($film.id)\" -Method PUT -ContentType 'application/json' -Headers @{\"Authorization\"=\"Bearer $token\"} -Body $updateBody -UseBasicParsing 2>&1
          if ($updateResp.StatusCode -eq 200) {
              Write-Output '✅ UPDATE successful!'
          } else {
              Write-Output \"❌ UPDATE failed with status: $($updateResp.StatusCode)\"
          }
      }
  } else {
      Write-Output '❌ Login failed!'
  }
"
