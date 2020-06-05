FROM openjdk:8-jdk-alpine
ARG JAR_FILE=ZuulServer/target/*.jar
COPY ${JAR_FILE} zuul.jar
ENTRYPOINT ["java","-jar","/zuul.jar"]
EXPOSE 8011