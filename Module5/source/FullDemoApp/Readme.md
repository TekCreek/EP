# Sample REST API Example with Docker 

## Project Setup 

### Docker Setup

1. Install Docker desktop
2. Open terminal move to the docker folder in source
3. Run the docker compose command to start the containers

```shell

    # Change to the docker folder under Module5 and run docker compose. 
    cd docker
    
    # Inside the docker folder run docker compose 
    docker compose up -d
```

Note: This step will start the MySQL, Redis and ActiveMQ containers. You can check the status of the containers in the Docker dashboard.

### Setup the database 

1. For connecting with MySQL you need MySQL Client, mysql workbench or choose DBeaver (pref) or anyother.
2. Open dbeaver 
3. Create new connection, choose MySQL, if prompted for downloading the driver proceed with it. If there is any issue with public key retrieval use the driver properties table to set it to false/true. 
4. Test connection and it should be successful.
5. Find the script.sql in the sql folder and execute the script to create the database and tables.

### Application Setup

1. Open the project in your IDE (IntelliJ)
2. Open the terminal in the IDE and run the following command to build the project and create the jar file.

```shell
    # Clean package creates a jar file in the target folder
    mvn clean package -Dmaven.test.skip=true
```
3. After the jar file is created, make sure you can find the jar file in the target folder.

4. Run the application using the java -jar command.

```shell
    # Change to the target folder where the jar file is located
    cd target
    
    # Run the application using java -jar command
    java -jar FullDemoApp-0.0.1-SNAPSHOT.jar    
```

5. Test the application by sending requests to the endpoints using Postman or CURL commands.

```shell

    # Create a new user 
    curl -X POST http://localhost:8080/api/v1/users/signup \
     -H "Content-Type: application/json" \
     -d '{"username":"Demo3","email":"demo3@test.com", "password":"123456"}' 
    
```

6. If this is successful you can terminate the application through ctrl + c.

7. Create a contaner for your application using dockerfile and run the application in the container using the docker-compose.yml in the docker folder. Open terminal and navigate to the docker folder under FullDemoApp folder and run the following command to build the image and run the container.

```shell

    # Change to the docker folder under FullDemoApp and run docker compose.
    cd docker
    
    # Build the image and run the container using docker compose
    docker compose up -d --build    

    # The above --build will use the dockerfile to build the image and then run the container. If you make any changes to the code you can use the same command to rebuild the image and restart the container with the new changes.

```

You should see the running FullDemo App container in the Docker dashboard. You can also check the logs of the container to see if the application is running successfully.

### Test The APP

#### CURL commands for User Operations 

1. Signup Operation

```shell

    # Create a new user 
    curl -X POST http://localhost:8080/api/v1/users/signup \
     -H "Content-Type: application/json" \
     -d '{"username":"Sample","email":"s@y.com", "password":"123456"}' 
    
```

Note: Check the log in the container to see the signup email log demonstrating the ActiveMQ integration. You can also check the ActiveMQ web console to see the message in the queue.

2. Login Operation to get JWT token for authentication

```shell
    # Login and get JWT token 
    curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"Sample", "password":"123456"}'
    
```

3. Replace the below token with the token obtained after login and use it for authentication in the subsequent requests. 

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzQ2MjY2MzEsImV4cCI6MTc3NDYzMDIzMX0.OKc7TTRIQHDILnBRiF0rmm1bH91a9kxzNMrhQSa4MYMBgWLenL04VbxAnQDZ-7wT"
     
    # Get all users (authentication required use the token obtained after login
      
    curl -X GET http://localhost:8080/api/v1/users -H "Authorization: Bearer $TOKEN"

```

### Other CURL commands for Product Operations with JWT Token

*NOTE: Replace the below token with the token obtained after login and use it for authentication in the subsequent requests.*

1. Get All Products

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzIxNzY1MjksImV4cCI6MTc3MjE4MDEyOX0.9Go-DNk8PWzRlPb0jxNhxS9IisW-SNNp12f788FV6DwT1QjHkWyO9ikhksCmH1YK"

    # Get all products (    
    curl -v -X GET http://localhost:8080/api/v1/products -H "Authorization: Bearer $TOKEN"
```    

2. Get the product with id

Note:- Hit this operation twice, first time you should see the logger
and the second time you should not as the request is served from the cache.

```shell
    export TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJTYW1wbGUiLCJpYXQiOjE3NzI2ODA3MzgsImV4cCI6MTc3MjY4NDMzOH0.8BflmUuYsmPfz0NULIXoEhWaPTX4zTg1v2VV6EAjohrFFKliIsy9ZwLpf9xP0t9h"

    # Get all products (    
    curl http://localhost:8080/api/v1/products/1 -H "Authorization: Bearer $TOKEN"
```    


## Good resource for learning docker 

[Docker Basics](https://www.youtube.com/watch?v=zJ6WbK9zFpI)