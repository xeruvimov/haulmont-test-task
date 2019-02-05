INSERT INTO "PUBLIC"."CLIENT"
( "FIRST_NAME", "SECOND_NAME", "PATRONYMIC", "NUMBER" ) VALUES
('Sergey', 'Kheruvimov', 'Alexandrovich', '89022350412'),
('William', 'Belsham', 'None', '88005553535'),
('Leonard', 'Read', 'None', '89879820327'),
('Murray', 'Rothbard', 'None', '88007751181'),
('Robert', 'Nozick', 'None', '438146');

insert into MECHANIC
(FIRST_NAME, SECOND_NAME, PATRONYMIC, TAXES) VALUES
('Karl', 'Marx', 'Heinrich', 100),
('Friedrich', 'Engels', 'Friedrich', 200),
('Vladimir', 'Lenin', 'Ilyich', 300),
('Joseph', 'Stalin', 'Vissarionovich', 400),
('Leon', 'Trotsky', 'Davidovich', 500);

insert into ORDR
(CLIENT_ID, MECHANIC_ID, DESCRIPTION, START_DATE, END_DATE, PRICE, STATUS) VALUES
(0, 0, 'Libertarianism', '2019-11-11', '2019-11-13', 1000, 'PLANNED'),
(1, 1, 'Anarcho-capitalism', '2019-11-11', '2019-11-13', 2000, 'DONE'),
(2, 2, 'Christian anarchism', '2019-11-11', '2019-11-13', 3000, 'ACCEPTED'),
(3, 3, 'Anti-authoritarianism', '2019-11-11', '2019-11-13', 4000, 'DONE'),
(4, 4, 'Federalism', '2019-11-11', '2019-11-13', 5000, 'PLANNED');