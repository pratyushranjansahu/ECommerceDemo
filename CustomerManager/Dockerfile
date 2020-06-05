FROM openjdk:8-jdk-alpine
ARG JAR_FILE=CustomerManager/target/*.jar
COPY ${JAR_FILE} customer.jar
ENTRYPOINT ["java","-jar","/customer.jar"]
EXPOSE 8095