delete from user_role;
delete from users;


insert into users(id, activation_code, active, email, password, username) values
(1, null, true, 'mixom89461@delotti.com', '$2a$08$kaQkIWKPmrt5UI0sfSB/3eJD0yhKRtqCkFpZTRcDo/laA8z1NO0C.', 'first');

insert into users(id, activation_code, active, email, password, username) values
(2, null, true, 'tinagi5935@youlynx.com' ,'$2a$08$z1SLP8dsCkct1PnzFKj0YunnVZ4zxX/uuZPiHfg9aHUYamjNSwNNq', 'tinagi5935@youlynx.com');


insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');