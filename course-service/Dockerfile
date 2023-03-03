FROM openjdk:17.0.2

EXPOSE 8082

CMD ["./gradlew", "bootJar"]

ENV spring.profiles.active="docker"

COPY "/build/libs/course-service-0.0.1-SNAPSHOT.jar" "course.jar"

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "course.jar"]
