delete from user_role;
delete from usr;

insert into usr(id,is_active,password,username)values
(1,true,'21232f297a57a5a743894a0e4a801fc3','admin'),
(2,true,'21232f297a57a5a743894a0e4a801fc3','mike');

insert into user_role(user_id,roles)values
(1,'USER'),(1,'ADMIN'),
(2,'USER');

