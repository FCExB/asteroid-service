ARG JAVA_IMAGE=eclipse-temurin:19

FROM $JAVA_IMAGE as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Force maven to download all dependencies so that they are cached
RUN ./mvnw verify --fail-never
COPY src ./src

FROM base as test
RUN ./mvnw test

FROM base as dev
CMD ./mvnw spring-boot:run

FROM base as build
RUN ./mvnw package

FROM $JAVA_IMAGE as prod
EXPOSE 8080
COPY --from=build /app/target/asteroid-service-*.jar /asteroid-service.jar
CMD java -jar asteroid-service.jar
