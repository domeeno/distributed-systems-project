FROM openjdk:17.0.2

EXPOSE 8082

CMD ["./gradlew", "bootJar"]

COPY "/build/libs/course-service-0.0.1-SNAPSHOT.jar" "course.jar"
COPY src/main/resources/application-docker.yml application.yml

CMD ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "course.jar", "--spring.config.location=application.yml"]