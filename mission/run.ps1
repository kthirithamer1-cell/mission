# Setup JAVA_HOME and Maven path for this session
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$mavenBin = "C:\Users\MEDIA INFOPLUS\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin"

if ($env:PATH -notlike "*$mavenBin*") {
    $env:PATH = "$mavenBin;$env:PATH"
}

Write-Host "Using Java from: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Using Maven from: $mavenBin" -ForegroundColor Green

# Run the Spring Boot application
mvn spring-boot:run
