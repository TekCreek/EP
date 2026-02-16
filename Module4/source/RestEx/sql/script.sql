
CREATE DATABASE `DemoDB`;

USE `DemoDB`;

CREATE TABLE `Product` (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(100) UNIQUE NOT NULL,
	price NUMERIC(10,2) NOT NULL
);

INSERT INTO `Product` (name,price)
VALUES
 ('x',10.00),
 ('y',20.00),
 ('z',30.00),
 ('ab',291.00),
 ('ac',30.00),
 ('ad',40.00),
 ('pq',50.00);

SELECT * FROM `Product`;