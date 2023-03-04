FROM openjdk:17.0.2

EXPOSE 8443

CMD ["./gradlew", "bootJar"]

ENV spring.profiles.active="docker"

COPY "build/libs/user-service-0.0.1-SNAPSHOT.jar" "user.jar"

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "user.jar"]
