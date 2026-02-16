# Database Application using JDBC (Java Database Connectivity)

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
10. Open SQL folder and copy the DBSetupScript.sql content and use it to setup the DB.
11. Open DBeaver, New SQL Script, Paste the above SQL from step(10) and run the file.
12. Refresh the DBeaver database explorer and see if the database is present or not.

## Run the Program

Note: Run the alter script in the `DBSetupScript.sql` file before running the program. This will set the `id` column of the `account` table to auto-increment, which is necessary for the program to work correctly.

Note: Database connection details are present in `application.properties` file. Please verify the same before running the program.

Run the test class `JpaEx1ApplicationTests` and verify the output in the console.

### In case of below exception : 
Exception in thread "main" java.sql.SQLNonTransientConnectionException: Public Key Retrieval is not allowed
Check if your MySQL JDBC URL Contains `?useSSL=false&allowPublicKeyRetrieval=true`