FROM maven:3.9.9-eclipse-temurin-21 AS builder

ARG SERVICE_MODULE
WORKDIR /workspace

COPY . .
RUN mvn -pl ${SERVICE_MODULE} -am clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

ARG SERVICE_MODULE
WORKDIR /app

COPY --from=builder /workspace/${SERVICE_MODULE}/target/${SERVICE_MODULE}-1.0.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
