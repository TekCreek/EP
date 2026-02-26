
# Spring Cache Demo

## Test this app

```shell

curl -X GET http://localhost:8080/api/v1/answer?qno=123

```

```shell

curl -X GET http://localhost:8080/api/v1/answer?qno=125

```

## Basic redis tutorial 

```shell
redis-cli

# set a key
set mykey "Hello, Redis!"

# get the value of the key
get mykey

# delete the key
del mykey

# set a key with an expiration time of 60 seconds
set tempkey "This is temporary" EX 60

# check if the key exists
exists tempkey

# list all keys
keys *

# Working with hashtables
# set a hash
hset user:1 name "Alice" age 30

# get a field from the hash
hget user:1 name

# get all fields and values from the hash
hgetall user:1

```