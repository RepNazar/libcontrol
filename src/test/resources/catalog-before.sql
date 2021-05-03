delete from books;

insert into books(id, code, name, in_stock, owner_id)
values (1, '01', '011', true, null),
       (2, '02', '021', true, null),
       (3, '03', '031', false, 2),
       (4, '11', '141', false, 3),
       (5, '241', '253', false, 3);

alter sequence hibernate_sequence restart with 10;
