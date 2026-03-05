
# Sample REST API Example with Docker 

```shell
    # Clean package creates a jar file in the target folder
    mvn clean package -Dmaven.test.skip=true
```

```shell
    # Change to the docker folder and run docker compose. 
    cd docker
    
    # Inside the docker folder run docker compose 
    docker compose up --build -d
```



## CURL commands for User Operations 

```shell

    # Create a new user 
    curl -X POST http://localhost:8080/api/v1/users/signup \
     -H "Content-Type: application/json" \
     -d '{"username":"Sample","email":"s@y.com", "password":"123456"}' 
    
```

```shell
    # Login and get JWT token 
    curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"Sample", "password":"123456"}'
    
```


```shell
    export TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzgwNTEsImV4cCI6MTc3MjE4MTY1MX0.7PDOxgM9u-4sMLftytFx2yAZIscpTyx8j16HBFvwcIg"
     
    # Get all users (authentication required use the token obtained after login
      
    curl -X GET http://localhost:8080/api/v1/users -H "Authorization: Bearer $TOKEN"

```


## CURL commands for Product Operations with JWT Token 

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzY1MjksImV4cCI6MTc3MjE4MDEyOX0.9Go-DNk8PWzRlPb0jxNhxS9IisW-SNNp12f788FV6DwT1QjHkWyO9ikhksCmH1YK"

    # Get all products (    
    curl -v -X GET http://localhost:8080/api/v1/products -H "Authorization: Bearer $TOKEN"
```    

## Get the product with id

Note:- Hit this operation twice, first time you should see the logger
and the second time you should not as the request is served from the cache.


```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzI2ODA3MzgsImV4cCI6MTc3MjY4NDMzOH0.8BflmUuYsmPfz0NULIXoEhWaPTX4zTg1v2VV6EAjohrFFKliIsy9ZwLpf9xP0t9h"

    # Get all products (    
    curl http://localhost:8080/api/v1/products/1 -H "Authorization: Bearer $TOKEN"
```    


## Good resource for learning docker 

[Docker Basics](https://www.youtube.com/watch?v=zJ6WbK9zFpI)