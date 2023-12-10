FROM eclipse-temurin:17.0.9_9-jdk-focal as build

COPY . /src
WORKDIR /src
RUN ./gradlew build --exclude-task test

FROM eclipse-temurin:17.0.9_9-jre-focal

COPY --from=build /src/build/libs/elevate-api-*.jar /bin/runner/app.jar
WORKDIR /bin/runner

CMD ["java","-jar","app.jar"]
EXPOSE 3000
