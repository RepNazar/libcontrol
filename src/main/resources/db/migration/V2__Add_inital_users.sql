insert into users (id, username, password, active)
values (1, 'testDirector', 'test', true),
       (2, 'testManager', 'test', true),
       (3, 'testLibrarian', 'test', true),
       (4, 'testClient', 'test', true);

insert into user_role (user_id, roles)
values (1, 'ROLE_DIRECTOR'),
       (2, 'ROLE_MANAGER'),
       (3, 'ROLE_LIBRARIAN'),
       (4, 'ROLE_CLIENT');