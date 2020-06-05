FROM openjdk:8-jdk-alpine
ARG JAR_FILE=CatalogueManager/target/*.jar
COPY ${JAR_FILE} catalog.jar
ENTRYPOINT ["java","-jar","/catalog.jar"]
EXPOSE 8090