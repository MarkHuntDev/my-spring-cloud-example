# Celebrity Greeting System
####  It's an example of Microservice Architecture based on Spring Cloud

Functional microservices have 2 independent base spring-profiles:
 - **stand-alone**: a microservice interacts with stubs (without Service Discovery, Config Server and so on)
 - **dependent**: a microservice locates within microservices environment (with Service Discovery and other services)

and several fine-grained profiles for combining them with base profiles:
 - **secure**: a secured microservice
 - **no-security**: a microservice just ignores security
 - **test**: uses by tests
