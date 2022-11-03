FROM openjdk:17.0.2

EXPOSE 8080

CMD ["./gradlew", "bootJar"]

COPY "/build/libs/user-service-0.0.1-SNAPSHOT.jar" "user.jar"
COPY src/main/resources/application-docker.yml application.yml

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "user.jar", "--spring.config.location=application.yml"]