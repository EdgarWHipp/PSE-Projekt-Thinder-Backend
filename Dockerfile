FROM gradle:7.4.2-jdk17-alpine as builder

WORKDIR /Thinder

COPY --chown=gradle:gradle . /Thinder
RUN ./gradlew build


FROM openjdk:17-alpine

COPY --from=builder /Thinder/build/libs/backend-0.0.1-SNAPSHOT.jar /thinder.jar

ENTRYPOINT ["java", "-Dserver.port=$PORT","-jar", "/thinder.jar"]
