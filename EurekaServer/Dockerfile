FROM openjdk:8-jdk-alpine
ARG JAR_FILE=EurekaServer/target/*.jar
COPY ${JAR_FILE} eureka.jar
ENTRYPOINT ["java","-jar","/eureka.jar"]
EXPOSE 8010