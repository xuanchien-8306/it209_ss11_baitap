# Build stage: build jar từ source code
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy gradle wrapper và build files trước (tận dụng Docker layer cache)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Cấp quyền thực thi cho gradlew
RUN chmod +x gradlew

# Download dependencies trước (cache layer riêng)
RUN ./gradlew dependencies --no-daemon

# Copy source code và build
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# Runtime stage: chỉ cần JRE để chạy
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar từ builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port ứng dụng
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
