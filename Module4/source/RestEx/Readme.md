# REST API with JPA combination example 


## Setup 

1. Install Docker and docker dashboard in your machine. 
2. To bring up MySQL Server

```shell
cd docker
docker compose up -d
```

3. Connect to MySQL database using any of the client tools e.g. MySQL Workbench or Dbeaver
4. In case of Dbeaver 
5. Choose New Database Connection Icon 
6. Select MySQL and Next Button 
7. ServerHost: localhost, Port :3306, Username: root, Password: password
8. Test Connection and select ok.
9. Note: (If prompted for driver download then choose mysql-connector-j for MySQL8 or later)
10. Open SQL folder and copy the script.sql content and use it to setup the DB.
11. Open DBeaver, New SQL Script, Paste the above SQL from step(10) and run the file.
12. Refresh the DBeaver database explorer and see if the database is present or not.

## Run the Program

Note: Database connection details are present in `application.properties` file. Please verify the same before running the program.
Server should be listening on port 8080. You can verify the same in the console logs.

Use the below cUrl commands to test the ProductController endpoints. Make sure to replace the product IDs in the URLs with actual IDs from your database.

### In case of below exception : 
Exception in thread "main" java.sql.SQLNonTransientConnectionException: Public Key Retrieval is not allowed
Check if your MySQL JDBC URL Contains `?useSSL=false&allowPublicKeyRetrieval=true`

## cUrl Commands for Testing ProductController Endpoints

Below are the cUrl commands for testing the ProductContoller endpoints.

1. Get all products:

```bash
curl -X GET http://localhost:8080/api/v1/products
```

2. Get a product by ID (replace {id} with the actual product ID):

```bash
curl -X GET http://localhost:8080/api/v1/products/1
```

3. Create a new product:

```bash
curl -X POST http://localhost:8080/api/v1/products \
-H "Content-Type: application/json" \
-d '{
  "name": "New Product",
  "price": 19.99
}'
```

4. Update an existing product (replace {id} with the actual product ID):

```bash
curl -X PUT http://localhost:8080/api/v1/products/2 \
-H "Content-Type: application/json" \
-d '{
  "name": "iPhone",
  "price": 29.99
}'
``` 

5. Delete a product (replace {id} with the actual product ID):

```bash
curl -X DELETE http://localhost:8080/api/v1/products/3
``` 

6. Find products by name pattern:

```bash
curl -X POST http://localhost:8080/api/v1/products/find \
-H "Content-Type: application/json" \
-d '{
  "pattern":"phone"
}'
```

7. Get paginated and sorted products:

```bash
curl -X GET "http://localhost:8080/api/v1/products/sorted?page=0&size=2&sortBy=price&direction=DESC"
``` 

```bash
curl -X GET "http://localhost:8080/api/v1/products/sorted?page=0&size=2&sortBy=price&direction=ASC"
``` 

```bash
curl -X GET "http://localhost:8080/api/v1/products/sorted?page=1&size=2"
``` 