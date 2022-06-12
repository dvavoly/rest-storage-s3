# password: admin
insert into users (first_name, last_name, email, secret_password, user_role)
values ('Eward', 'Kelling', 'a@admin.com', '$2a$12$o.AcBiktNGNyKVtZfoipge86yJKQ.2P/HPSJRDrSY.vuVP1aHf9.C', 0);
# password: moderator
insert into users (first_name, last_name, email, secret_password, user_role)
values ('Phoebe', 'Bauduccio', 'm@moderator.com', '$2a$12$Mq4zteBo/96yK/Ln/NiJ1.nFWX2jPYyTGpBnJQsSV.aCTQjcrj23.', 1);
insert into users (first_name, last_name, email, secret_password, user_role)
# password: user
values ('Jeanna', 'Gergely', 'u@user.com', '$2a$12$V9p55h8nqArVTzh4Vy3PVerZGANZ.UBog552F/ZrbpWX2vAEQrzTe', 2);

insert into files (fileName, location, user_id)
values ('AcNulla.mp3', 'https://amazon.de/integer/ac/leo/pellentesque/ultrices.html', 1);
insert into files (fileName, location, user_id)
values ('AliquetPulvinar.mov', 'https://walmart.com/nunc/purus/phasellus/in/felis/donec.jpg', 2);
insert into files (fileName, location, user_id)
values ('SagittisNamCongue.gif', 'https://bravesites.com/tortor.jpg', 2);
insert into files (fileName, location, user_id)
values ('Libero.avi', 'https://webs.com/dapibus.jsp', 3);
insert into files (fileName, location, user_id)
values ('EleifendQuam.txt', 'https://nytimes.com/quisque/ut/erat/curabitur/gravida.png', 3);
insert into files (fileName, location, user_id)
values ('JustoSollicitudinUt.tiff', 'https://wisc.edu/in.jpg', 3);

insert into events (upload_time, user_id, file_id)
values ('2021-08-15 06:12:04', 1, 1);
insert into events (upload_time, user_id, file_id)
values ('2022-05-01 11:35:10', 2, 2);
insert into events (upload_time, user_id, file_id)
values ('2022-02-07 14:33:15', 2, 3);
insert into events (upload_time, user_id, file_id)
values ('2021-07-20 03:53:26', 3, 4);
insert into events (upload_time, user_id, file_id)
values ('2022-04-02 14:03:29', 3, 5);
insert into events (upload_time, user_id, file_id)
values ('2021-11-15 04:28:38', 3, 6);
