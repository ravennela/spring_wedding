FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# give permission to gradlew
RUN chmod +x gradlew

# build jar
RUN ./gradlew build -x test

EXPOSE 8080

CMD ["sh", "-c", "java -jar build/libs/*.jar"]