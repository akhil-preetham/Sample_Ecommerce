@echo off
setlocal

mvn clean install || exit /b 1
pushd frontend
npm install || exit /b 1
npm run build || exit /b 1
popd

docker compose up -d --build || exit /b 1

call :wait_for "http://admin:admin@localhost:8761/actuator/health" "Eureka" || exit /b 1
call :wait_for "http://admin:admin@localhost:8888/actuator/health" "Config Server" || exit /b 1
call :wait_for "http://localhost:8080/actuator/health" "API Gateway" || exit /b 1
call :wait_for "http://localhost:4200" "Frontend" || exit /b 1

start "" "http://localhost:4200"
echo Platform is ready at http://localhost:4200
exit /b 0

:wait_for
set "URL=%~1"
set "LABEL=%~2"
echo Waiting for %LABEL%...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "$ProgressPreference='SilentlyContinue';" ^
  "for($i=0;$i -lt 120;$i++){" ^
  "  try { Invoke-WebRequest -Uri '%URL%' -UseBasicParsing | Out-Null; exit 0 } catch { Start-Sleep -Seconds 5 }" ^
  "}" ^
  "exit 1"
if errorlevel 1 exit /b 1
exit /b 0
