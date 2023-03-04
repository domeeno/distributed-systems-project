FROM openjdk:17.0.2

EXPOSE 8080

CMD ["./gradlew", "bootJar"]

ENV spring.profiles.active="docker"

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "user.jar", "--spring.config.location=application.yml"]