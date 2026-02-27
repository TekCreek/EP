
# Sample REST API Example with Docker 

```
    Note: Point to the RestEx folder and run maven clean package
    > mvn clean package
    
    Change to docker folder 
    > cd docker
    
    Inside the docker folder run docker compose 
    > docker compose up --build -d
```

## CURL commands for User Operations 

```shell

    # Create a new user 
    curl -X POST http://localhost:8080/api/v1/users/signup -H "Content-Type: application/json" -d '{"username":"Sample","email":"x@y.com", "password":"123456"}' 
    
```

```shell
    # Login and get JWT token 
    curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"Sample","password":"123456"}'
    
```


```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzQ4ODYsImV4cCI6MTc3MjE3ODQ4Nn0.Te73U3ls6J7vhjrkXWZNylLj4GqCVkHqxtymHIwXBgo1dJrNWmu5DUwV9tGDR-Dn"
     
    # Get all users (authentication required use the token obtained after login  
    curl -X GET http://localhost:8080/api/v1/users -H "Authorization: Bearer $TOKEN"

```


## CURL commands for Product Operations with JWT Token 

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzU0ODAsImV4cCI6MTc3MjE3OTA4MH0.s8_sKrQpu-drJiabVUlBJ5a49E-l7_txyGXY-P9di1HVQ9FQnrfwkbKTT2F5E3C2"

    # Get all products (    
    curl -v -X GET http://localhost:8080/api/v1/products -H "Authorization: Bearer $TOKEN"
```    

## Good resource for learning docker 

[Docker Basics](https://www.youtube.com/watch?v=zJ6WbK9zFpI)