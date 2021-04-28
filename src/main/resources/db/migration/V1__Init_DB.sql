create sequence hibernate_sequence start 1 increment 1;
create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);

create table users
(
    id              int8 not null,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255),
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
);

create table books
(
    id       int8 not null,
    code     varchar(255),
    in_stock boolean not null,
    name     varchar(255),
    primary key (id)
);

create table orders
(
    id        int8 not null,
    approved  boolean not null,
    confirmed boolean not null,
    date      timestamp,
    finished  boolean not null,
    status    varchar(255),
    book_id   int8,
    user_id   int8,
    primary key (id)
);

alter table if exists orders
    add constraint orders_user_fk foreign key (book_id) references books;

alter table if exists orders
    add constraint orders_book_fk foreign key (user_id) references users;

alter table if exists user_role
    add constraint user_role_user_fk foreign key (user_id) references users