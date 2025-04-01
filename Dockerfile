FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY ./ .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

# Test 단계
FROM builder AS tester
CMD ["./gradlew", "test", "--no-daemon", "-Dspring.profiles.active=test"]


# Run 단계
FROM openjdk:17-jdk-slim AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/api-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]