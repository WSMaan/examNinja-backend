# Use OpenJDK 17 base image
FROM openjdk:17

# Expose port 8080
EXPOSE 8080

# Add your JAR file to the Docker image
ADD target/docker-jenkins-integration-sample.jar /docker-jenkins-integration-sample.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/docker-jenkins-integration-sample.jar"]
