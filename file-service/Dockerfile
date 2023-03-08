FROM openjdk:17.0.2

EXPOSE 8445

CMD ["./gradlew", "bootJar"]

ENV spring.profiles.active="docker"

COPY "/build/libs/file-service-0.0.1-SNAPSHOT.jar" "bucket.jar"

CMD ["java", "-jar", "bucket.jar"]
