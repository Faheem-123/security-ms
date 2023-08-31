FROM maven:latest AS maven_build
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -e -Dmaven.test.skip=true

FROM openjdk:latest
EXPOSE 8086
COPY --from=maven_build /tmp/target/*.jar security-ms.jar
ENTRYPOINT ["java", "-Dspring.config=.", "-jar", "/security-ms.jar"]