delete from orders;

insert into orders(id, status,approved, confirmed, finished, user_id, book_id, for_return)
values (1, 'Finished', true, true, true, 2, 3, false),
       (2, 'Finished', true, true, true, 3, 4, false),
       (3, 'Finished', true, true, true, 3, 5, false),
       (4, 'Pending Approval', false, false, false, 2, 1, false),
       (5, 'Pending Approval', false, false, false, 3, 1, false),
       (6, 'Approved', true, false, false, 3, 2, false),
       (7, 'Finished', true, true, true, 3, 1, true),
       (8, 'Pending Approval', false, true, false, 3, 5, true);

alter sequence hibernate_sequence restart with 10;
