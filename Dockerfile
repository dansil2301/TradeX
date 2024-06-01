ARG TINKOFF_TOKEN
ARG JWT_SECRET

FROM openjdk:17-jdk-slim
COPY build/docker/tradex.jar /app/tradex.jar

ENV TINKOFF_TOKEN=${TINKOFF_TOKEN}
ENV JWT_SECRET=${JWT_SECRET}

CMD ["java", "-jar", "/app/tradex.jar"]
