delete from message;
insert into message(id, filename, tag, text, user_id) values
(1, null, 'my-tag', 'first', 1),
(2, null, 'more', 'second', 1),
(3, null, 'my-tag', 'third', 1),
(4, null, 'another', 'fourth', 2);

alter sequence hibernate_sequence restart with 10;
