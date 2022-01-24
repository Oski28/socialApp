/* DELETE */

DELETE FROM message; ALTER TABLE message AUTO_INCREMENT = 1;
DELETE FROM notice; ALTER TABLE notice AUTO_INCREMENT = 1;
DELETE FROM report; ALTER TABLE report AUTO_INCREMENT = 1;
DELETE FROM user_chat; ALTER TABLE user_chat AUTO_INCREMENT = 1;
DELETE FROM request_to_join; ALTER TABLE request_to_join AUTO_INCREMENT = 1;
DELETE FROM participant_event_list; ALTER TABLE participant_event_list AUTO_INCREMENT = 1;
DELETE FROM event_category; ALTER TABLE event_category AUTO_INCREMENT = 1;
DELETE FROM event; ALTER TABLE event AUTO_INCREMENT = 1;
DELETE FROM chat; ALTER TABLE chat AUTO_INCREMENT = 1;
DELETE FROM register_token; ALTER TABLE register_token AUTO_INCREMENT = 1;
DELETE FROM refresh_token; ALTER TABLE refresh_token AUTO_INCREMENT = 1;
DELETE FROM user_role; ALTER TABLE user_role AUTO_INCREMENT = 1;
DELETE FROM black_list_token; ALTER TABLE black_list_token AUTO_INCREMENT = 1;
DELETE FROM password_token; ALTER TABLE password_token AUTO_INCREMENT = 1;
DELETE FROM user; ALTER TABLE user AUTO_INCREMENT = 1;
DELETE FROM category; ALTER TABLE category AUTO_INCREMENT = 1;
DELETE FROM role; ALTER TABLE role AUTO_INCREMENT = 1;



/* Roles */
INSERT INTO role(role) VALUE ("ROLE_USER"),("ROLE_MODERATOR"),("ROLE_ADMIN");

/* Categories */
INSERT INTO category(name) VALUE ("Sport"),("Rozrywka"),("Podróże"),("Kinematografia"),("Inne");

/* Users */
INSERT INTO user(add_date,blocked,date_of_birth,email,enabled,firstname,lastname,password,username) VALUE
(CURRENT_TIMESTAMP() ,"0","1989-07-19","jankowalski@gmail.com","1","Jan","Kowalski",
"$2a$10$T3kTt667dJDWjGvYE.KTlOfrhaTXHZApORlbGY1YwHb35DTZF7xOS","User"),/*H: User123*/
(CURRENT_TIMESTAMP(),"0","1995-02-04","piotrnowak@gmail.com","1","Piotr","Nowak",
"$2a$10$hruixh2Ca.gHi7zSGlrIn.c3aEds6Rn5uAgys9impBh2wq0gNZZoy","Mod"), /*H: Mod123*/
(CURRENT_TIMESTAMP(),"0","1998-01-09","krzysztofkot@gmail.com","1","Krzysztof","Kot",
"$2a$10$tlXWEsp0yBA6MBAd4v0kOO96t3tkOhVuj.0JgyCsqOzv8lyq/Jsae","Admin"),/*H: Admin123*/
(CURRENT_TIMESTAMP(),"0","1996-02-09","user1@mail.com","1","User","User1",
"$2a$10$2eI0FJh1iptsFieE9hGR5u3yclVW8jbiZe3GzH6sH10u5i8vP7Raa","User1"),/*H: Userr1*/
(CURRENT_TIMESTAMP(),"0","1995-03-09","user2@mail.com","1","User","User2",
"$2a$10$Y6/btoIS3PaHgBTikdbuvOYBsyqpeAagKTO.OuxANqfi9DGwotWAC","User2"),/*H: Userr2*/
(CURRENT_TIMESTAMP(),"0","1993-04-09","user3@mail.com","1","User","User3",
"$2a$10$R1G7PwvPhSlnp2Iwo1AL3.Wx/d5eO3AZTAOQYvjiG7ktuwJ7sVpCS","User3"),/*H: Userr3*/
(CURRENT_TIMESTAMP(),"0","2004-05-09","user4@mail.com","1","User","User4",
"$2a$10$OPd0/CDxbs5FMvWs8YCzSuYP5J8VKYYgLJat2/sQHO7UcQmN2ZRNi","User4"),/*H: Userr4*/
(CURRENT_TIMESTAMP(),"0","2004-07-04","user5@mail.com","1","User","User5",
"$2a$10$uICaNND2v8SaoKkzFQBQ6eLChkSPgU6T0v7ihnhOfFjMJJGAz3T6W","User5"),/*H: Userr5*/
(CURRENT_TIMESTAMP(),"0","1989-08-29","user6@mail.com","1","User","User6",
"$2a$10$wj/WuZUROnJeW5BRwrSb5eAkEv09R83zzJBNxNthWpM9JkyfC0oGC","User6"),/*H: Userr6*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user7@mail.com","1","User","User7",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User7"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user8@mail.com","1","User","User8",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User8"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user9@mail.com","1","User","User9",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User9"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user10@mail.com","1","User","User10",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User10"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user11@mail.com","1","User","User11",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User11"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user12@mail.com","1","User","User12",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User12"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user13@mail.com","1","User","User13",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User13"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user14@mail.com","1","User","User14",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User14"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user15@mail.com","1","User","User15",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User15"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user16@mail.com","1","User","User16",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User16"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user17@mail.com","1","User","User17",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User17"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user18@mail.com","1","User","User18",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User18"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user19@mail.com","1","User","User19",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User19"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user20@mail.com","1","User","User20",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User20"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user21@mail.com","1","User","User21",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User21"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user22@mail.com","1","User","User22",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User22"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user23@mail.com","1","User","User23",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User23"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user24@mail.com","1","User","User24",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User24"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user25@mail.com","1","User","User25",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User25"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user26@mail.com","1","User","User26",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User26"),/*H: Userr7*/
(CURRENT_TIMESTAMP(),"0","1969-09-19","user27@mail.com","1","User","User27",
"$2a$10$M0mc/4ICXIM7XM/UlASA4ueZ3JTas9n5IeAkbF/P/ZzbO5cXvcMOm","User27");/*H: Userr7*/

/* User role */
INSERT INTO user_role(id_user,id_role) VALUE ("1","1"),("2","1"),("2","2"),("3","1"),("3","2"),("3","3"),
("4","1"),("5","1"),("6","1"),("7","1"),("8","1"),("9","1"),("10","1"),
("11","1"),("12","1"),("13","1"),("14","1"),("15","1"),("16","1"),("17","1"),
("18","1"),("19","1"),("20","1"),("21","1"),("22","1"),("23","1"),("24","1"),
("25","1"),("26","1"),("27","1"),("28","1"),("29","1"),("30","1");

/* Register token */
INSERT INTO register_token(created_date, token, id_user) VALUE (CURRENT_TIMESTAMP(),
"aa3547a0-001d-11ec-9a03-0242ac130003","1"),(CURRENT_TIMESTAMP(),
"aa354a0c-001d-11ec-9a03-0242ac130003","2"),(CURRENT_TIMESTAMP(),
"aa354afc-001d-11ec-9a03-0242ac130003","3"),(CURRENT_TIMESTAMP(),
"aa3547a1-001d-11ec-9a03-0242ac130003","4"),(CURRENT_TIMESTAMP(),
"aa354a1c-001d-11ec-9a03-0242ac130003","5"),(CURRENT_TIMESTAMP(),
"aa354afd-001d-11ec-9a03-0242ac130003","6"),(CURRENT_TIMESTAMP(),
"aa3547a2-001d-11ec-9a03-0242ac130003","7"),(CURRENT_TIMESTAMP(),
"aa354a2c-001d-11ec-9a03-0242ac130003","8"),(CURRENT_TIMESTAMP(),
"aa354afe-001d-11ec-9a03-0242ac130003","9"),(CURRENT_TIMESTAMP(),
"aa3547a3-001d-11ec-9a03-0242ac130003","10");

/* Chat */
INSERT INTO chat(name) VALUE ("Piłkarzyki"),("Gambit królowej"),("Kinomaniacy"),("Królowie Bieszczadu"),("Wataha");

/* Event */
INSERT INTO event(age_limit,date,description,free_join,location,max_number_of_participant,
name,chat_id,organizer_id) VALUE ("15","2022-02-14 19:00:00","Poszukujemy chętnych osób do wspólnej gry w piłkę na hali.",
true,"Hala Podpromie Rzeszów","14","Gra w piłkę nożną na hali","1","1"), ("12","2022-02-18 10:00:00","Poszukujemy chętnych
osób do turnieju towarzyskiego w szachach. Turniej w systemie każdy z każdym, mecze po 20 minut na zawodnika.",true,
"Nestor - Politechnika Rzeszowska","10","Turniej szachowy","2","1"), ("16","2022-03-14 22:00:00","Poszukiwane chętne osoby na seans grupowy
najnowszej części Gwiezdych Wojen",true,"Helios Rzeszów","120","Seans Star Wars","3","2"), (null ,"2022-05-01 06:00:00","Rozpoczynamy sezon
cotygodniowych weekendowych wyjazdów w najpiękniejsze góry w Polsce.",false,"Bieszady",null ,"Bieszczadowe szaleństwo","4","2"),
 ("18","2022-06-11 06:00:00","Chcesz poczuć się jak bohater serialu Wataha i odwiedzić wszystkie lokacje z popularnego serialu? Nasze
 ogłoszenie jest właśnie dla Ciebie!",true,"Cała Polska","15","Szlakami serialu Wataha","5","3");

 /* Event category */
 INSERT INTO event_category(id_event,id_category) VALUE ("1","1"),("2","1"),("2","2"),("3","2"),("3","4"),("4","3"),("4","1"),
 ("5","4"),("5","2"),("5","3");

 /* Participant event list */
 INSERT INTO participant_event_list(id_event,id_user) VALUE ("1","1"),("1","10"),("1","9"),("1","4"),("1","5"),("1","6"),
 ("1","7"),("1","8"),("2","1"),("2","4"),("2","5"),("2","6"),("2","7"),("2","8"),("3","2"),("3","4"),("3","5"),("3","6"),
 ("3","7"),("3","9"),("4","2"),("4","9"),("4","8"),("4","10"),("5","10"),("5","3"),("5","4"),("4","4");

 /* Request to join */
 INSERT INTO request_to_join(status,event_id,user_joining_id,add_date) VALUE ("REJECTED","4","5",CURRENT_TIMESTAMP()),("WAITING","4","6",CURRENT_TIMESTAMP())
 ,("WAITING","4","15",CURRENT_TIMESTAMP()),("ACCEPTED","4","4",CURRENT_TIMESTAMP()),("WAITING","4","3",CURRENT_TIMESTAMP());

 /* User chat */
 INSERT INTO user_chat(id_chat,id_user) VALUE ("1","1"),("1","10"),("1","9"),("1","4"),("1","5"),("1","6"),
 ("1","7"),("1","8"),("2","1"),("2","4"),("2","5"),("2","6"),("2","7"),("2","8"),("3","2"),("3","4"),("3","5"),("3","6"),
 ("3","7"),("3","9"),("4","2"),("4","9"),("4","8"),("4","10"),("5","10"),("5","3"),("5","4"),("4","4");

 /* Report */
 INSERT INTO report(add_date,reason,received,id_reported_event, id_reported_user) VALUE (CURRENT_TIMESTAMP() ,"Złamanie regulaminu","0","1","1"),
 (CURRENT_TIMESTAMP(),"Wulgaryzmy","1","2","1"), (CURRENT_TIMESTAMP(),"Wulgaryzmy","0","3","10");

/* Notice */
INSERT INTO notice(content,received,id_user) VALUE ("Do Twojego wydarzenia dołączył nowy użytkownik","0","1"),
("Użytkownik opuścił Twoje wydarzenie","0","3"),("Użytkownik wysłał prośbę o dołączenie do Twojego wydarzenie","1","3");

/* Message */
INSERT INTO message(content,write_date,chat_id,sender_id,file_name) VALUE
("Mess1",CURRENT_TIMESTAMP(),"1","1",null),("Mess2",CURRENT_TIMESTAMP(),"1","10",null),
("Mess3",CURRENT_TIMESTAMP(),"1","4",null),("Mess4",CURRENT_TIMESTAMP(),"1","1",null),
("Mess5",CURRENT_TIMESTAMP(),"1","1",null),("Mess6",CURRENT_TIMESTAMP(),"1","10",null),
("Mess7",CURRENT_TIMESTAMP(),"1","4",null),("Mess8",CURRENT_TIMESTAMP(),"1","1",null),
("Mess9",CURRENT_TIMESTAMP(),"1","1",null),("Mess10",CURRENT_TIMESTAMP(),"1","10",null),
("Mess11",CURRENT_TIMESTAMP(),"1","4",null),("Mess12",CURRENT_TIMESTAMP(),"1","1",null),
("Mess13",CURRENT_TIMESTAMP(),"1","1",null),("Mess14",CURRENT_TIMESTAMP(),"1","10",null),
("Mess15",CURRENT_TIMESTAMP(),"1","4",null),("Mess16",CURRENT_TIMESTAMP(),"1","1",null),
("Mess17",CURRENT_TIMESTAMP(),"1","1",null),("Mess18",CURRENT_TIMESTAMP(),"1","10",null),
("Mess19",CURRENT_TIMESTAMP(),"1","4",null),("Mess20",CURRENT_TIMESTAMP(),"1","1",null),
("Cześć, kto ma piłkę?",CURRENT_TIMESTAMP(),"1","1",null),("Ja, tylko potrzeba pompkę :/?",CURRENT_TIMESTAMP(),"1","10",null),
("Ja wezmę pompkę :)",CURRENT_TIMESTAMP(),"1","4",null),("Git! :D",CURRENT_TIMESTAMP(),"1","1",null);
