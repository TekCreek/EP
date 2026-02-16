create schema `DemoDB`;

create user `demoUser` identified by 'demoPwd';

use `DemoDB`;
grant all privileges on `DemoDB`.* to `demoUser`@'%';


create table `account` (
	`id` integer PRIMARY KEY,
    `name` varchar(20),
    `balance` numeric(10,2)
);

ALTER TABLE DemoDB.account MODIFY COLUMN id int auto_increment NOT NULL;

insert into `account` values(1, 'a', 1000);
insert into `account` values(2, 'ab', 2000);
insert into `account` values(3, 'c', 3000);


select * from `account`;