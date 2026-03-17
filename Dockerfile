# ---------- Build Stage ----------
FROM gradle:9.3-jdk25 AS builder

WORKDIR /build

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

COPY . .

RUN gradle bootJar --no-daemon


# ---------- Runtime Stage ----------
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

# create non-root user
RUN useradd -ms /bin/bash springuser

COPY --from=builder /build/build/libs/*.jar app.jar

# security
RUN chown springuser:springuser app.jar
USER springuser

EXPOSE 8080

# JVM tuning for container
ENTRYPOINT ["java", \
"-XX:MaxRAMPercentage=80", \
"-XX:+UseG1GC", \
"-jar", \
"/app/app.jar"]