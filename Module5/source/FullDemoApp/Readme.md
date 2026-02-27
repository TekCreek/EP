
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
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzY1MjksImV4cCI6MTc3MjE4MDEyOX0.9Go-DNk8PWzRlPb0jxNhxS9IisW-SNNp12f788FV6DwT1QjHkWyO9ikhksCmH1YK"
     
    # Get all users (authentication required use the token obtained after login  
    curl -X GET http://localhost:8080/api/v1/users -H "Authorization: Bearer $TOKEN"

```


## CURL commands for Product Operations with JWT Token 

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzY1MjksImV4cCI6MTc3MjE4MDEyOX0.9Go-DNk8PWzRlPb0jxNhxS9IisW-SNNp12f788FV6DwT1QjHkWyO9ikhksCmH1YK"

    # Get all products (    
    curl -v -X GET http://localhost:8080/api/v1/products -H "Authorization: Bearer $TOKEN"
```    

## Good resource for learning docker 

[Docker Basics](https://www.youtube.com/watch?v=zJ6WbK9zFpI)