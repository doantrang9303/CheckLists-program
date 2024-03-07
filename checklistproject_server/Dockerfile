##syntax = docker/dockerfile:1
#FROM openjdk:23-oraclelinux8
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN chmod +x ./mvnw
#RUN sh -c ./mvnw dependency:go-offline
#COPY src ./src
#RUN ./mvnw clean package spring-boot:repackage
#EXPOSE 8088
#CMD ["./mvnw", "spring-boot:run"]

# syntax=docker/dockerfile:1
FROM openjdk:17
#working directory
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]