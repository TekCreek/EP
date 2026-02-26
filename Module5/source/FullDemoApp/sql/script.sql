
CREATE DATABASE `sampledb`;

USE `sampledb`;

CREATE TABLE `User` (
	id INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(100) UNIQUE NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(100) NULL
);


CREATE TABLE `Product` (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(100) UNIQUE NOT NULL,
	price NUMERIC(10,2) NOT NULL
);

INSERT INTO `User`(username, password)
VALUES('a', 'a@b.com', 'pwd');

INSERT INTO `Product` (name,price)
VALUES
 ('x',10.00),
 ('y',20.00),
 ('z',30.00),
 ('ab',291.00),
 ('ac',30.00),
 ('ad',40.00),
 ('pq',50.00);

