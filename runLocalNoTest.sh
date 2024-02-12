./gradlew assemble

export DATASOURCE_USERNAME="root"
export DATASOURCE_PASSWORD="rootadmin"
export DATASOURCE_URL="jdbc:mysql://localhost:3306/crmDBtest?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false"
export JAR_SOURCE="./build/libs/crm-app-0.0.1-SNAPSHOT.jar"

java -jar -Dspring.datasource.username="$DATASOURCE_USERNAME" \
    -Dspring.datasource.password="$DATASOURCE_PASSWORD" \
    -Dspring.datasource.url="$DATASOURCE_URL" \
    $JAR_SOURCE

# java -jar -Dspring.config.location=./src/main/resources/application.properties -Dspring.datasource.username=root -Dspring.datasource.password=rootadmin -Dspring.datasource.url="jdbc:mysql://localhost:3306/crmDBtest?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false" ./build/libs/crm-app-0.0.1-SNAPSHOT.jar

# ./runLocalNoTest.sh
