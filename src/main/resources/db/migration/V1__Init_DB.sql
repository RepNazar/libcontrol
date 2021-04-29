create sequence hibernate_sequence start 1 increment 1;

create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);

create table users
(
    id              int8         not null,
    username        varchar(255) not null,
    password        varchar(255) not null,
    email           varchar(255),
    active          boolean      not null,
    activation_code varchar(255),
    primary key (id)
);

create table books
(
    id       int8    not null,
    code     varchar(255),
    name     varchar(255),
    in_stock boolean not null,
    owner_id int8,
    primary key (id)
);

create table orders
(
    id         int8    not null,
    status     varchar(255),
    date       timestamp,
    approved   boolean not null,
    confirmed  boolean not null,
    finished   boolean not null,
    for_return boolean not null,
    book_id    int8,
    user_id    int8,
    primary key (id)
);

alter table if exists orders
    add constraint orders_books_fk foreign key (book_id) references books;

alter table if exists orders
    add constraint orders_users_fk foreign key (user_id) references users;

alter table if exists books
    add constraint books_users_fk foreign key (owner_id) references users;

alter table if exists user_role
    add constraint user_role_users_fk foreign key (user_id) references users