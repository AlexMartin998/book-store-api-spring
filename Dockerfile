# ## Stage 1: Build the JAR file
FROM openjdk:17-jdk-alpine as build

# Add the Maven dependency
RUN apk add --no-cache maven

# Set the working directory to /opt/app
WORKDIR /opt/app

# Copy the Spring Boot application source code to the working directory
COPY . .

# Build the JAR file
RUN mvn clean package -DskipTests



# ## Stage 2: Run the JAR file
FROM openjdk:17-jdk-alpine

# Copy the JAR file from the build stage
COPY --from=build /opt/app/target/*.jar /opt/app/app.jar

# Set the working directory to /opt/app
WORKDIR /opt/app

# Expose the default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]

