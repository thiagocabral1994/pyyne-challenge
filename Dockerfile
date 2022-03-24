FROM maven:3.8.3-openjdk-16 AS maven_build

COPY pom.xml /tmp/

COPY src /tmp/src/

WORKDIR /tmp/

RUN mvn package

FROM openjdk

EXPOSE 8080

CMD java -jar /data/challenge-0.1.0.jar

COPY --from=maven_build /tmp/target/challenge-0.1.0.jar /data/challenge-0.1.0.jar
