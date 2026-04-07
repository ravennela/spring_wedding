FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy project
COPY . .

# Give permission to gradlew
RUN chmod +x gradlew

# Build jar
RUN ./gradlew build -x test

# Expose port
EXPOSE 8080

# Run jar
CMD ["sh", "-c", "java -jar build/libs/*.jar"]