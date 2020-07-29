# ScalaAutist 1.0

ScalaAutist is developed with Scala.

ScalaAutist's primary function is the assignment of work shifts in a public transport company.
Moreover it allows user to manage different aspects in the working life of the company, 
providing functionality to meet user needs.

There are three type of user: 
 - System Manager: SM manage terminals and work shifts
 - Human Resource: HR manage employee, and their problem like illness and holidays 
 - Driver: D can watch his work shifts and his salary
 
## Client
Client is the sub-system that allows user to interface to the system and make operation that he needs.
To run the client application with the local server:
```bash
scala client.jar
```
To run the client application with the another server, you have to specify ip and port:
```bash
scala client.jar ip:port
```

## Server
Server is the sub-system that provide to fulfill user's requests.
To run the server application:
```bash
scala server.jar
```
But there is a docker instance of server
To run the client application with the local server:
```bash
docker client.jar
```

## Team members

Fabia Andres Aspee Encina: fabianAspeeEncina@studio.unibo.it

Giovanni Mormone: giovanni.mormone@studio.unibo.it

Francesco Cassano: francesco.cassano2@studio.unibo.it
 
 