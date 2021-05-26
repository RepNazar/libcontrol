delete
from user_role;
delete
from users;

insert into users(id, username, password, email, active)
values (1, 'testLibrarian', 'testLib', 'u1@u1.u1', true),
       (2, 'mike', '1', 'u2@u2.u2', true),
       (3, 'testClient', '2', 'u3@u3.u3', true),
       (4, 'testManager', '3', 'u4@u4.u4', true),
       (5, 'testDirector', '4', 'u5@u5.u5', true);

create extension if not exists pgcrypto;
update users
set password = crypt(password, gen_salt('bf', 8));

insert into user_role(user_id, roles)
values (1, 'ROLE_LIBRARIAN'),
       (2, 'ROLE_CLIENT'),
       (3, 'ROLE_CLIENT'),
       (4, 'ROLE_MANAGER'),
       (5, 'ROLE_DIRECTOR');
