# Step 1: Use a base image with Java and Maven installed
FROM maven:3.8.6-openjdk-17 AS build

# Step 2: Set the working directory inside the container for the build stage
WORKDIR /app

# Step 3: Copy the pom.xml and other Maven configuration files
COPY pom.xml .

# Step 4: Download dependencies (this step is separate so Docker can cache it)
RUN mvn dependency:go-offline

# Step 5: Copy the entire project source code
COPY src ./src

# Step 6: Build the application
RUN mvn clean package -DskipTests

# Step 7: Use a lightweight JDK runtime for running the application
FROM openjdk:17-jdk-slim

# Step 8: Set the working directory for the final image
WORKDIR /app

# Step 9: Copy the jar file from the build stage to the runtime image
COPY --from=build /app/target/examninja-0.0.1.jar examninja.jar

# Step 10: Expose the port the application will run on
EXPOSE 8081

# Step 11: Specify the command to run the application
ENTRYPOINT ["java", "-jar", "examninja.jar"]
