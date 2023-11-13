# Use a maven image for building the project
FROM maven:3.8.6-amazoncorretto-8 AS build

# Copy the entire project to the container
COPY . /app

# Set the working directory
WORKDIR /app

# Build the project
RUN --mount=type=cache,target=/root/.m2 mvn clean install

# Use a lightweight image for running the application
FROM amazoncorretto:8

# Copy the JAR file from the build stage to the current stage
COPY --from=build /app/target/ /app

WORKDIR /app

# Expose the port that the SOAP service will run on
EXPOSE 8081

# Run the SOAP service
CMD java -cp /app/SOAP-Service-1.0-SNAPSHOT.jar org.saranghaengbok.Main
