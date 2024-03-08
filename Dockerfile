FROM --platform=linux/amd64 maven:3.9.6-eclipse-temurin-21-alpine

# Create a working directory
WORKDIR /BookAndSalesApplication

# Copy the POM file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the Maven project
RUN mvn verify clean --fail-never

# Expose port 8080
EXPOSE 8080

# Set timezone
ENV TZ=Europe/Amsterdam
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Command to run the application
ENTRYPOINT ["mvn", "spring-boot:run"]