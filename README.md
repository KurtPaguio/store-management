//If it cannot run using Docker

Setting up for a Springboot Project

What to download and install?
- Java 8 SDK
- Latest IntelliJ IDEA Community Edition
- PostgreSQL Version 13
- Maven Version 3.6.3

After installing, add them to the environment path variables. Import the store-management-sql to the PostgreSQL.

To run the application, right click the project folder then click 'Git Bash'.
Here are the commands:
* mvn clean install -DskipTests - install without running the tests
* mvn clean install - install and run tests of the project
* mvn spring-boot:run - run the application
