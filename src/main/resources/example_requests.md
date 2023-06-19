###Postamn example requests for cinema-app

GET: http://localhost:8080/cinema-app/login  
Choose 'Authorization' Type: Basic Auth then login with 'Admin' credentials in 'Usage Example'.  

Add some movies, cinema halls and movie-sessions:  
(choose 'Body' raw JSON)  
POST: http://localhost:8080/cinema-app/movies

```java
{"title": "The Little Marmaid, 2023", "description": "The Little Mermaid is a 2023 American musical fantasy film directed by Rob Marshall"}
{"title": "Mavka. The Forest Song", "description": "Mavka. The Forest Song is a 2023 Ukrainian computer-animated fantasy comedy-drama film directed by Oleh Malamuzh and Oleksandra Ruban. Based on the poetic play The Forest Song by poet Lesya Ukrainka."}
```
POST: http://localhost:8080/cinema-app/cinema-halls

```java
{"capacity": 200, "description": "Multiplex"}
{"capacity": 150, "description": "Pioneer"}
```
POST: http://localhost:8080/cinema-app/movie-sessions

```java
{"movieId": 2, "cinemaHallId": 1, "showTime":"2023-06-01T12:30:00"}
{"movieId": 1, "cinemaHallId": 2, "showTime":"2023-06-01T16:00:00"}
{"movieId": 2, "cinemaHallId": 1, "showTime":"2023-06-01T16:00:00"}
```

As 'Admin' you can change sessions and delete them, get user by email:  
PUT: http://localhost:8080/cinema-app/movie-sessions/{id}

```java
{"movieId": 2, "cinemaHallId": 1, "showTime":"2023-05-18T12:30:00"}
```
DELETE: http://localhost:8080/cinema-app/movie-sessions/{id}  
GET: http://localhost:8080/cinema-app/users/by-email/?email={email}

Let's add new user type 'User':
POST: http://localhost:8080/cinema-app/register

```java
{"email":"your_email@domain","password":"your_password","repeatPassword":"your_password"}
```
All users can get list of available movies, cinema-halls and sessions:  
GET: http://localhost:8080/cinema-app/movies  
GET: http://localhost:8080/cinema-app/cinema-halls  
GET: http://localhost:8080/cinema-app/movie-sessions/available?movieId={id}&date={dd.MM.yyyy}  

Choose 'Authorization' and set 'User' credentials.  
Then simulate tickets order:  
PUT: http://localhost:8080/cinema-app/shopping-carts/movie-sessions?movieSessionId={id}  
GET: http://localhost:8080/cinema-app/shopping-carts/by-user  
POST: http://localhost:8080/cinema-app/orders/complete  
GET: http://localhost:8080/cinema-app/orders
