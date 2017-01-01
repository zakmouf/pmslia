
create database bluetree;
create user 'bluetree'@'localhost';
alter user 'bluetree'@'localhost' identified by 'mysql';
grant all on bluetree.* to 'bluetree'@'localhost';
