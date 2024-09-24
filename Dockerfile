# Step 1: Use a base image with Java runtime
FROM openjdk:17-jdk

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the jar file from the local machine to the working directory in the container
COPY target/examninja-0.0.1.jar examninja.jar

# Step 4: Expose the port the application will run on
EXPOSE 8081

# Step 5: Specify the command to run the application
ENTRYPOINT ["java", "-jar", "examninja.jar"]
