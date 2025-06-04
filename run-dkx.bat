@echo off
echo Starting DKX Cryptocurrency Website...
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Testing Java...
"%JAVA_HOME%\bin\java.exe" --version

if %ERRORLEVEL% NEQ 0 (
    echo Java not found! Please check installation.
    pause
    exit /b 1
)

echo Java found! Starting Maven Wrapper...
"%CD%\mvnw.cmd" spring-boot:run

pause 