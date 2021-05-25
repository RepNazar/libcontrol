delete from books;

insert into books(id, code, name, genre, in_stock, owner_id)
values (1, '01', '011', 'aw', true, null),
       (2, '02', '021', 'ws', true, null),
       (3, '03', '031', 'gf', false, 2),
       (4, '11', '141', 'fv', false, 3),
       (5, '241', '253', 'fn', false, 3);

alter sequence hibernate_sequence restart with 10;
