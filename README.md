# ScalaAutist 1.0

ScalaAutist is developed with Scala.

ScalaAutist's primary function is the assignment of work shifts in a public transport company.
Moreover it allows user to manage different aspects in the working life of the company, 
providing functionality to meet user needs.

There are three type of user: 
 - System Manager: SM manage terminals and work shifts
 - Human Resource: HR manage employee, and their problem like illness and holidays 
 - Driver: D can watch his work shifts and his salary
 
## Note to work
 
## Client
Client is the sub-system that allows user to interface to the system and make operation that he needs.
To run the client application with the local server:
```bash
scala client.jar
```
or with
```bash
java -jar client.jar
```
Note tha if you run the client locally you need to start a local instance of the server.
To run the client with a connection to a remote server:
```bash
scala client.jar remote
```
or with 
```bash
java -jar client.jar remote
```

## Server
Server is the sub-system that provide to fulfill user's requests.
To run the server application:
```bash
scala server.jar
```
or with:
```bash
java -jar server.jar
```
If you like you can also pull a docker image containing an istance of the server.
```bash
pull DOCKER DEL SERVER
```
This instance is meant to work locally as a substitute of the server.jar, you can run it
with:
```bash
docker run -p8080:8080 dockerimage
```
There is also a remote docker container with an istance of the server already running, you can interact with it
simply running the client in remote mode.



## Team members

Fabia Andres Aspee Encina: fabianAspeeEncina@studio.unibo.it

Giovanni Mormone: giovanni.mormone@studio.unibo.it

Francesco Cassano: francesco.cassano2@studio.unibo.it
 
 