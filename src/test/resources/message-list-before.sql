delete from message;

insert into message(id,text,tag,user_id)values
(1,'first','my-tag',1),
(2,'second','my-tag',1),
(3,'third','my-tag',1),
(4,'fourth','my-tag',1);

insert into hibernate_sequence values ( 10 );