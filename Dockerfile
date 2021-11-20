FROM adoptopenjdk/openjdk15:x86_64-ubuntu-jdk-15.0.2_7 as build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src
COPY data data

RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw package

# ------------------------------------------------------------------------------

FROM adoptopenjdk/openjdk15:x86_64-ubuntu-jre-15.0.2_7

COPY --from=build /app/target/delivery-manager-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/data/deliveries.json deliveries.json
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]