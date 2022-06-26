FROM gradle:7.4.2-jdk17-alpine as builder

WORKDIR /Thinder

COPY --chown=gradle:gradle . /Thinder
RUN ./gradlew bootJar


FROM openjdk:17-alpine

COPY --from=builder /Thinder/build/libs/*.jar /thinder.jar

EXPOSE ${PORT}
ENTRYPOINT ["java","-jar", "/thinder.jar"]