ARG PROFILE
ARG TINKOFF_TOKEN
ARG JWT_SECRET
ARG AWS_DB_PASSWORD
ARG AWS_DB_CONNECT

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/docker/tradex.jar /app/tradex.jar

ENV TINKOFF_TOKEN=${TINKOFF_TOKEN}
ENV JWT_SECRET=${JWT_SECRET}
ENV AWS_DB_PASSWORD=${AWS_DB_PASSWORD}
ENV PROPERTIES_FILE=${PROFILE}
ENV AWS_DB_CONNECT=${AWS_DB_CONNECT}

EXPOSE 8080
CMD ["java", "-jar", "/app/tradex.jar"]
