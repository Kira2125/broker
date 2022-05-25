Required to run the application:
- jdk11+
- maven
- intellijIdea

Simple message broker with in-memory data store

Functions:

- create topic

- register devices/subscribers

- send messages:

        - device to device
	
        - device to topic
	
        - broadcast (all devices)
        
What needs to be added?

- database store

- acknowledgment from subscribers about message consuming

- batch consuming to avoid subscriber overloading
