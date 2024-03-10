FROM openjdk:17-jdk-slim
COPY build/docker/tradex.jar /app/tradex.jar
CMD ["java", "-jar", "/app/tradex.jar"]
