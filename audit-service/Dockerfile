FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir -p /var/log/securebank
RUN addgroup -S securebank && adduser -S securebank -G securebank
USER securebank
COPY --from=builder /app/target/audit-service-1.0.0.jar app.jar
EXPOSE 8088
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -q --spider http://localhost:8088/actuator/health || exit 1
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
