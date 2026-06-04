$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
$mavenBin = "C:\Users\MEDIA INFOPLUS\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin"

if ($env:PATH -notlike "*$mavenBin*") {
    $env:PATH = "$mavenBin;$env:PATH"
}

mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
