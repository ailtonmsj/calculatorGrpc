FROM openjdk:11.0-jdk-slim

ARG JAR_FILE=build/libs/*-all.jar

COPY ${JAR_FILE} calculator.jar

EXPOSE 50081

ENTRYPOINT ["java","-jar","/calculator.jar"]