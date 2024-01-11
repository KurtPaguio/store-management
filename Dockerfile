FROM openjdk:8
EXPOSE 8080
COPY ./target/store-management.war store-management.jar
ENTRYPOINT ["java","-jar","/store-management.jar"]