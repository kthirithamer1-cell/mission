# Setup JAVA_HOME and run Spring Boot application
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

Write-Host "Using Java from: $env:JAVA_HOME" -ForegroundColor Green

# Run the Spring Boot application
mvn spring-boot:run
