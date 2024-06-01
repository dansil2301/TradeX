ARG TINKOFF_TOKEN
FROM openjdk:17-jdk-slim
COPY build/docker/tradex.jar /app/tradex.jar
ENV TINKOFF_TOKEN=${TINKOFF_TOKEN}
CMD ["java", "-jar", "/app/tradex.jar"]
