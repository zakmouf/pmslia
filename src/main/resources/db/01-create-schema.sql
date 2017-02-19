
drop database if exists bluetree;
create database bluetree character set 'utf8';
drop user if exists 'bluetree'@'localhost';
create user 'bluetree'@'localhost';
set password for 'bluetree'@'localhost' = password ('mysql');
grant all privileges on bluetree.* to 'bluetree'@'localhost' with grant option;
