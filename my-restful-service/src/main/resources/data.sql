insert into users(id, join_date, name, password, ssn) values(9001, now(), 'User1', 'test111', '701010-1111110');
insert into users(id, join_date, name, password, ssn) values(9002, now(), 'User2', 'test222', '801010-1111110');
insert into users(id, join_date, name, password, ssn) values(9003, now(), 'User3', 'test333', '901010-1111110');

insert into post(description, user_id) values('My first post', 9001);
insert into post(description, user_id) values('My second post', 9001);