INSERT INTO "PUBLIC"."CLIENT"
( "FIRST_NAME", "SECOND_NAME", "PATRONYMIC", "NUMBER" ) VALUES
('test0', 'test0', 'test0', 'test0'),
('test1', 'test1', 'test1', 'test1'),
('test2', 'test2', 'test2', 'test2'),
('test3', 'test3', 'test3', 'test3'),
('test4', 'test4', 'test4', 'test4');

insert into MECHANIC
(FIRST_NAME, SECOND_NAME, PATRONYMIC, TAXES) VALUES
('mec0', 'mec0', 'mec0', 0),
('mec1', 'mec1', 'mec1', 1),
('mec2', 'mec2', 'mec2', 2),
('mec3', 'mec3', 'mec3', 3),
('mec4', 'mec4', 'mec4', 4);

insert into ORDR
(CLIENT_ID, MECHANIC_ID, DESCRIPTION, START_DATE, END_DATE, PRICE, STATUS) VALUES
(0, 0, 'desc0', '2019-11-11', '2019-11-13', 100, 'PLANNED'),
(1, 1, 'desc1', '2019-11-11', '2019-11-13', 200, 'DONE'),
(2, 2, 'desc2', '2019-11-11', '2019-11-13', 300, 'ACCEPTED'),
(3, 3, 'desc3', '2019-11-11', '2019-11-13', 400, 'DONE'),
(4, 4, 'desc4', '2019-11-11', '2019-11-13', 500, 'PLANNED');