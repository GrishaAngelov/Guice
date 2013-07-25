create database if not exists bank;
use bank;
create table if not exists users(username varchar(20) not null primary key, password varchar (10));
insert into users values ("john","password");
create table bank_account(username varchar(20) not null primary key, amount decimal (10,2));
insert into bank_account values ("john",0.00);

create table expire_time(username varchar(100) not null, time date not null,primary key(username))

create database if not exists testbank;
use testbank;
create table if not exists users(username varchar(20) not null primary key, password varchar (10));
create table if not exists bank_account(username varchar(20) not null primary key, amount decimal (10,2));


