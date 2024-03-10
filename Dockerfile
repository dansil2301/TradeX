FROM openjdk
EXPOSE 8080
ADD target/tradex tradex.jar
ENTRYPOINT ["java", "-jar", "/tradex.jar"]