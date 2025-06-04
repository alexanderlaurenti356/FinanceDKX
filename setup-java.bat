@echo off
echo Setting up Java environment for DKX website...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME set to: %JAVA_HOME%
echo Testing Java installation...
java --version

echo.
echo Java is ready! Now running DKX website...
mvn spring-boot:run 