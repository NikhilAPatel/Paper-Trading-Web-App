drop database if exists SalStocks;
create database SalStocks;
use SalStocks;
create table User(
	user_id int primary key not null auto_increment,
    username varchar(255),
    password varchar(1023),
    email varchar(255),
	google_user boolean,
    balance float
);

create table Stock(
	stock_id int primary key not null auto_increment,
    ticker varchar(255) unique,
    name varchar(255) 
);

create table Owned_Stock(
	os_id int primary key not null auto_increment,
    user_id int not null,
	stock_id int not null,
    quantity int not null,
    purchase_price float not null,
    timestamp varchar(255) not null,
	foreign key fk1(user_id) references User(user_id),
	foreign key fk2(stock_id) references Stock(stock_id)
);

create table Favorite(
	favorite_id int primary key not null auto_increment,
    user_id int not null,
    stock_id int not null,
	foreign key fk1(user_id) references User(user_id),
    foreign key fk2(stock_id) references Stock(stock_id)
);