FROM openjdk:17-jdk-alpine

# change this based on the `mvn clean package -DskipTests` JAR result
COPY target/inventory-management-0.0.1-SNAPSHOT.jar java-app.jar

ENTRYPOINT ["java", "-jar", "java-app.jar"]
