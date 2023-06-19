# cinema-app

This is a simulator of cinema service for reservation tickets, built with Java, Spring, and Hibernate.
There is implemented Spring security authentication.
The project follows REST principles implemented with HTTP methods.  
There implemented 2 types of roles: "User" and "Admin" have different access to controllers.

Functionality:
* login as user or register (for last need to use Postman)
* create and find movies and cinema halls
* create, find and update available movie sessions
* add tickets to movie-session to shopping cart
* complete an order, get orders history

## Project Structure

The project is based on 3-layer architecture:

1. Controllers, which handle requests, call services and send responses
2. Services - there are all business logic
3. DAO, which handle CRUD operations to database

## Technologies

* Java 17
* Spring MVC 5.3.20
* Spring Security 5.6.10
* Hibernate 5.6.14.Final
* MySQL 8.0.22
* Apache Tomcat 9.0
* Tests: (spring-test 5.3.7, mockito-core 5.3.1, hsqldb 2.7.2):  
All coverage 64.8%, tested DAO and service layers - 100%) 

## Installation and Launch
You need to install [JDK](https://www.oracle.com/cis/java/technologies/downloads/), [MySQL](https://dev.mysql.com/downloads/installer/), [Tomcat](https://tomcat.apache.org/download-90.cgi), [Postman desktop version](https://www.postman.com/downloads/), IDE with Java support.

1. Fork this repository.
2. Clone your forked repository.
3. In 'db.properties' specify `MySQL properties`.
4. Launch project with Server.
6. Use Postman for sending requests. (You can use prepared and template requests from  
`resources->example_requests` file)

## Usage Example

Login with credentials below for Admin access:  
Username: admin@i.ua  
Password: admin123