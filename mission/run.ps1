# Setup JAVA_HOME and run Spring Boot application
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Using Java from: $env:JAVA_HOME" -ForegroundColor Green

# Run the Spring Boot application
mvn spring-boot:run
