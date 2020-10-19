FROM openjdk:15
COPY target/lunch-api-0.0.1.jar lunch-api-0.0.1.jar
CMD ["java","-jar","lunch-api-0.0.1.jar"]