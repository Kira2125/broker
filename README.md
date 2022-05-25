Simple Message Broker

Prerequisites:
-	jdk11+
-	maven
-	intellijIdea


Running the application:
-	Build Spring Boot Project with Maven:

		- mvn package

-	Run Spring Boot app using Maven or with java -jar command:
	
		- mvn spring-boot:run

		- java -jar target/simple-broker-0.0.1-SNAPSHOT.jar


Description:
Simple message broker with in-memory data store

Functionality description:

- topics (create, subscribe)

- devices/subscribers (register, deregister)

- send messages to devices, topics and in brodcast mode
        
Upcoming tasks:

- add database store

- add batch consuming to avoid subscriber overloading

