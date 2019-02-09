create table if not exists client (
id bigint identity primary key ,
first_name varchar(50) not null,
second_name varchar(50) not null,
patronymic varchar(50) not null,
number varchar(11) not null
);

create table if not exists mechanic (
id bigint identity primary key ,
first_name varchar(50) not null,
second_name varchar(50) not null,
patronymic varchar(50) not null,
taxes int not null
);

create table if not exists contract (
id bigint identity primary key ,
client_id bigint not null foreign key references client(id),
mechanic_id bigint not null foreign key references mechanic(id),
description varchar(500) not null,
start_date date not null,
end_date date not null,
price int not null,
status varchar(50) not null
);