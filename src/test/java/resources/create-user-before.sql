delete from user_role;
delete from users;


insert into users(id, activation_code, active, email, password, username) values
(1, true, '$2a$08$z1SLP8dsCkct1PnzFKj0YunnVZ4zxX/uuZPiHfg9aHUYamjNSwNNq', 'kolya');

insert into users(id, activation_code, active, email, password, username) values
(2, true, '$2a$08$z1SLP8dsCkct1PnzFKj0YunnVZ4zxX/uuZPiHfg9aHUYamjNSwNNq', 'Nekolya');


insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');