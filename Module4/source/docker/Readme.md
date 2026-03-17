# Run the docker compose 

1. Install docker desktop
2. Open terminal move to the docker folder in source
```shell
    cd Module4
    cd source/docker 

    docker compose up -d
```

3. Check the containers in the docker dashboard
4. For connecting with MySQL you need MySQL Client, mysql workbench or choose DBeaver (pref) or anyother.

5. Open dbeaver 
6. Create new connection, choose MySQL, if prompted for downloading the driver proceed with it. If there is any issue with public key retrieval use the driver properties table to set it to false/true. 
7. Test connection.