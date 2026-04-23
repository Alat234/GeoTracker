

FROM  maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle
COPY gradlew.bat .
COPY gradlew .

RUN chmod +x ./gradlew\
    && ./gradlew dependencies
COPY src ./src
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

VOLUME app/upload-dir

ENTRYPOINT ["java", "-jar", "app.jar"]


