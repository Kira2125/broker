
# Simple Message Broker

Simple message broker with in-memory data store


## Prerequisites:

- jdk11+

- maven



## Run Locally

Clone the project

```bash
  git clone https://github.com/Kira2125/broker.git
```

Go to the project directory

```bash
  cd broker
```

Build Spring Boot Project with Maven

```bash
  mvn package
```

Run Spring Boot app using Maven or with java -jar command

```bash
  mvn spring-boot:run
  java -jar target/simple-broker-0.0.1-SNAPSHOT.jar
```


## Functionality description

- Topics (create, subscribe)
- Devices/subscribers (register, deregister)
- Send messages to devices, topics and in brodcast mode

## Demo

https://eurofunk.kirak.de/broker (postman collection in code - postman/broker_collection.postman_collection.json)

## Upcoming tasks

- add database store
- add batch consuming to avoid subscriber overloading

