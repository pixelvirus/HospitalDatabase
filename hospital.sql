/*drop tables*/
drop table PUserHasA;
drop table SUserHasA;
drop table PatientUsers;
drop table StaffUsers;
drop table HasAOffice;
drop table Offices;
drop table Performs;
drop table Prescribes;
drop table Medications;
drop table AdmittedTo;
drop table BookingForProcedures;
drop table Procedures;
drop table BookingReservesRecoveryRooms;
drop table RecoveryRoomBeds;
drop table RecoveryRooms;
drop table BookingReservesOperatingRooms;
drop table OperatingRooms;
drop table Bookings;
drop table Doctors;
drop table Patients;

/*create tables*/
create table Patients
( 
	pt_id integer not null primary key,
	pt_name varchar(20) not null,
	phone varchar(12),
	address varchar(40),
	city varchar(20),
	province varchar(2),
	zip varchar(7),
	insurance varchar(20)
);

create table Doctors
( 
	do_id int not null primary key,
	do_name varchar(20),
	specialization varchar(20) 
);

create table Bookings
(
	bk_id int not null primary key,
	bk_startdate date,
	bk_enddate date,
	constraint bookingdate
	check(bk_startdate<bk_enddate)
);

create table OperatingRooms
( 
	oproom_id int not null primary key,
	spEquip varchar(40)
);

create table BookingReservesOperatingRooms
(
	bk_id int not null,
	oproom_id int,
	primary key (bk_id,oproom_id),
	foreign key (bk_id) references Bookings,
	foreign key (oproom_id) references OperatingRooms
);
  
create table RecoveryRooms
( 
	recoveryroom_id int not null primary key,
	recoveryroom_noBeds int
);

create table RecoveryRoomBeds
(	
	recoveryroom_id int,
	recoveryroombed_bedNo int not null,
	primary key (recoveryroom_id,recoveryroombed_bedNo),
	foreign key (recoveryroom_id) references RecoveryRooms on delete cascade	
);
 
create table BookingReservesRecoveryRooms
(
	bk_id int not null,
	recoveryroom_id int,
	recoveryroombed_bedNo int not null,
	primary key (bk_id,recoveryroom_id,recoveryroombed_bedNo),
	foreign key (bk_id) references Bookings,
	foreign key (recoveryroom_id,recoveryroombed_bedNo) references RecoveryRoomBeds(recoveryroom_id,recoveryroombed_bedNo)
);  

create table Procedures
(
	proc_id int not null primary key,
	proc_name varchar(20),
	description varchar(40),
	cost float 
);
 
create table BookingForProcedures
(
	bk_id int,
	proc_id int,
	do_id int,
	primary key (bk_id,proc_id,do_id),
	foreign key (bk_id) references Bookings,
	foreign key (proc_id) references Procedures,
	foreign key (do_id) references Doctors
  );

 create table AdmittedTo
( 
	pt_id int,
	recoveryroom_id int,
	recoveryroombed_bedNo int not null,
	primary key (pt_id, recoveryroom_id, recoveryroombed_bedNo),
	foreign key (pt_id) references Patients,
	foreign key (recoveryroom_id,recoveryroombed_bedNo) references RecoveryRoomBeds(recoveryroom_id,recoveryroombed_bedNo)
);
	
  create table Medications
( 
	med_id integer not null primary key,
	med_name varchar(20),
	cost float 
);
  
create table Prescribes
( 
	pt_id int,
	do_id int,
	med_id integer,
	prescribes_date date,
	prescribes_dosage varchar(20),
	primary key (pt_id, do_id, med_id, prescribes_date),
	foreign key (pt_id) references Patients,
	foreign key (do_id) references Doctors,
	foreign key (med_id) references Medications 
);

create table Performs
( 
	pt_id int,
	do_id int,
	proc_id int,
	oproom_id int,
	performs_date date,
	primary key (pt_id, do_id, proc_id, oproom_id, performs_date),
	foreign key (pt_id) references Patients,
	foreign key (do_id) references Doctors,
	foreign key (oproom_id) references OperatingRooms,
	foreign key (proc_id) references Procedures 
);

create table Offices
( 
	office_id integer not null primary key,
	sqFoot int
);
  
create table HasAOffice
( 
	do_id int not null,
	office_id int not null,
	primary key (do_id, office_id),
	foreign key (do_id) references Doctors on delete cascade,
	foreign key (office_id) references Offices on delete cascade
);

create table StaffUsers
(
	userName varchar(20) primary key,
	password varchar(20)
);
  
create table PatientUsers
(
	userName varchar(20) primary key,
	password varchar(20)
);
 
create table SUserHasA
(
	do_id int,
	userName varchar(20),
	primary key (do_id, userName),
	foreign key (do_id) references Doctors,
	foreign key (userName) references StaffUsers
);
  
create table PUserHasA
(
	pt_id int,
	userName varchar(20),
	primary key (pt_id, userName),
	foreign key (pt_id) references Patients,
	foreign key (userName) references PatientUsers
);

/*Patients*/
insert into patients
values('10001', 'John Smith', '778-895-0451', '5959 Student Union Blvd',
'Vancouver','BC','V6T-1K2','Blue Cross');

insert into patients
values('15602', 'Jane Doe', '778-489-0235', '33 Suncrest Dr',
'Delta','BC','V4C-2N1' ,'Great West');

insert into patients
values('79856', 'Anita Chen', '778-332-0346', '599 Water Street',
'Vancouver','BC','V6B-4R3' ,null);

insert into patients
values('97640', 'Bob Sutherland', '604-825-6325', '33 Alexander Street',
'Vancouver','BC','V6A-2S5' ,'Blue Cross');

insert into patients
values('56841', 'David Nathan', '604-865-1564', '17 Powell Street',
'Vancouver','BC','V6A-2S3' ,'Sun Life');

/*Doctors*/
insert into doctors
values('469','Eric Cheng','cardiology');

insert into doctors
values('713','David Song','cardiology');

insert into doctors
values('231','Ben Byers','dermatology');

insert into doctors
values('198','Matt Galloway','emergency');

insert into doctors
values('876','Lindsay Burnett','oncology');

insert into doctors
values('555','Katie Freund','rheumatology');
	
/*Bookings*/
insert into bookings
values('111',
TO_DATE('12:00-02-01-2015','HH24:MI-DD-MM-YYYY'),
TO_DATE('14:00-02-01-2015','HH24:MI-DD-MM-YYYY'));

insert into bookings
values('222',
TO_DATE('13:00-23-12-2015','HH24:MI-DD-MM-YYYY'),
TO_DATE('15:00-23-12-2015','HH24:MI-DD-MM-YYYY'));

insert into bookings
values('333',
TO_DATE('16:00-11-10-2014','HH24:MI-DD-MM-YYYY'),
TO_DATE('18:30-11-10-2014','HH24:MI-DD-MM-YYYY'));

insert into bookings
values('444',
TO_DATE('19:30-02-12-2015','HH24:MI-DD-MM-YYYY'),
TO_DATE('21:00-02-12-2015','HH24:MI-DD-MM-YYYY'));

insert into bookings
values('555',
TO_DATE('9:00-12-01-2015','HH24:MI-DD-MM-YYYY'),
TO_DATE('12:00-15-01-2015','HH24:MI-DD-MM-YYYY'));

insert into bookings
values('123',
TO_DATE('02-01-2015','DD-MM-YYYY'),
TO_DATE('05-01-2015','DD-MM-YYYY'));

insert into bookings
values('234',
TO_DATE('23-12-2015','DD-MM-YYYY'),
TO_DATE('25-12-2015','DD-MM-YYYY'));

insert into bookings
values('345',
TO_DATE('11-10-2014','DD-MM-YYYY'),
TO_DATE('14-10-2014','DD-MM-YYYY'));

insert into bookings
values('456',
TO_DATE('02-12-2015','DD-MM-YYYY'),
TO_DATE('03-12-2015','DD-MM-YYYY'));

insert into bookings
values('567',
TO_DATE('12-01-2015','DD-MM-YYYY'),
TO_DATE('17-01-2015','DD-MM-YYYY'));

/*OperatingRooms*/
insert into operatingrooms
values('1000',null);

insert into operatingrooms
values('2000','laser');

insert into operatingrooms
values('3000',null);

insert into operatingrooms
values('4000',null);

insert into operatingrooms
values('5000','suture, needle holder, thread');

/*BookingReservesOperatingRooms*/
insert into BookingReservesOperatingRooms
values('111','3000');

insert into BookingReservesOperatingRooms
values('222','1000');

insert into BookingReservesOperatingRooms
values('333','5000');

insert into BookingReservesOperatingRooms
values('444','4000');

insert into BookingReservesOperatingRooms
values('555','2000');

/*RecoveryRooms*/
insert into recoveryrooms
values('1','1');

insert into recoveryrooms
values('2','2');

insert into recoveryrooms
values('3','3');

/*RecoveryRoomBeds*/
insert into recoveryroombeds
values('1','1');

insert into recoveryroombeds
values('2','1');

insert into recoveryroombeds
values('2','2');

insert into recoveryroombeds
values('3','1');

insert into recoveryroombeds
values('3','2');

insert into recoveryroombeds
values('3','3');

/*BookingReservesRecoveryRooms*/
insert into bookingreservesrecoveryrooms
values('123','1','1');

insert into bookingreservesrecoveryrooms
values('234','2','1');

insert into bookingreservesrecoveryrooms
values('345','2','2');

insert into bookingreservesrecoveryrooms
values('456','3','1');

insert into bookingreservesrecoveryrooms
values('567','3','2');

/*Procedures*/
insert into procedures
values('10101','skin biopsy','take skin for analysis','100');

insert into procedures
values('20202','radiation therapy','apply lasers to cancer','900');

insert into procedures
values('30303','open heart surgery','open chest, fix heart','500');

insert into procedures
values('40404','wart removal','wart is frozen off','50');

insert into procedures
values('50505','stitches','wound is stitched together','30');

/*BookingForProcedures*/
insert into bookingforprocedures
values('111','30303','469');

insert into bookingforprocedures
values('222','10101','231');

insert into bookingforprocedures
values('333','50505','198');

insert into bookingforprocedures
values('444','40404','876');

insert into bookingforprocedures
values('555','20202','555');

/*AdmittedTo*/
insert into admittedto
values('10001','1','1');

insert into admittedto
values('15602','2','1');

insert into admittedto
values('79856','2','2');

insert into admittedto
values('97640','3','1');

insert into admittedto
values('56841','3','2');

/*Medications*/
insert into medications
values('11111111','heart medicine','30');

insert into medications
values('22222222','skin cream','70');

insert into medications
values('33333333','blood pressure','25');

insert into medications
values('44444444','insulin','35');

insert into medications
values('55555555','cancer cocktail','90');

/*Prescribes*/
insert into prescribes
values('10001','469','11111111',TO_DATE('02-01-2015','DD-MM-YYYY'),'2 pills, once/day');

insert into prescribes
values('15602','231','11111111',TO_DATE('23-12-2015','DD-MM-YYYY'),'twice/day');

insert into prescribes
values('79856','198','11111111',TO_DATE('11-10-2014','DD-MM-YYYY'),'1/day');

insert into prescribes
values('97640','876','11111111',TO_DATE('02-12-2015','DD-MM-YYYY'),'750 mL');

insert into prescribes
values('56841','555','11111111',TO_DATE('12-01-2015','DD-MM-YYYY'),'1/week');

insert into prescribes
values('15602','231','22222222',TO_DATE('23-12-2015','DD-MM-YYYY'),'twice/day');

insert into prescribes
values('10001','469','22222222',TO_DATE('23-12-2015','DD-MM-YYYY'),'twice/day');

insert into prescribes
values('79856','198','22222222',TO_DATE('11-10-2014','DD-MM-YYYY'),'1/day');

insert into prescribes
values('97640','876','22222222',TO_DATE('02-12-2015','DD-MM-YYYY'),'750 mL');

insert into prescribes
values('56841','555','22222222',TO_DATE('12-01-2015','DD-MM-YYYY'),'1/week');

insert into prescribes
values('79856','198','33333333',TO_DATE('11-10-2014','DD-MM-YYYY'),'1/day');

insert into prescribes
values('97640','876','44444444',TO_DATE('02-12-2015','DD-MM-YYYY'),'750 mL');

insert into prescribes
values('56841','555','55555555',TO_DATE('12-01-2015','DD-MM-YYYY'),'1/week');

/*Performs*/
insert into performs
values('10001','469','30303','3000',TO_DATE('02-01-2015','DD-MM-YYYY'));

insert into performs
values('15602','469','10101','1000',TO_DATE('23-12-2015','DD-MM-YYYY'));

insert into performs
values('79856','469','50505','5000',TO_DATE('11-10-2014','DD-MM-YYYY'));

insert into performs
values('97640','469','40404','4000',TO_DATE('02-12-2015','DD-MM-YYYY'));

insert into performs
values('56841','469','20202','2000',TO_DATE('12-01-2015','DD-MM-YYYY'));

insert into performs
values('10001','231','30303','3000',TO_DATE('02-01-2015','DD-MM-YYYY'));

insert into performs
values('15602','231','10101','1000',TO_DATE('23-12-2015','DD-MM-YYYY'));

insert into performs
values('79856','231','50505','5000',TO_DATE('11-10-2014','DD-MM-YYYY'));

insert into performs
values('97640','231','40404','4000',TO_DATE('02-12-2015','DD-MM-YYYY'));

insert into performs
values('56841','231','20202','2000',TO_DATE('12-01-2015','DD-MM-YYYY'));

insert into performs
values('10001','469','30303','3000',TO_DATE('02-01-2015','DD-MM-YYYY'));

insert into performs
values('79856','198','50505','5000',TO_DATE('11-10-2014','DD-MM-YYYY'));

insert into performs
values('97640','876','40404','4000',TO_DATE('02-12-2015','DD-MM-YYYY'));

insert into performs
values('56841','555','20202','2000',TO_DATE('12-01-2015','DD-MM-YYYY'));

/*Offices*/
insert into offices
values('100','200');

insert into offices
values('101','200');

insert into offices
values('102','200');

insert into offices
values('103','200');

insert into offices
values('104','200');

/*HasAOffice*/
insert into hasaoffice
values('469','100');

insert into hasaoffice
values('231','101');

insert into hasaoffice
values('198','102');

insert into hasaoffice
values('876','103');

insert into hasaoffice
values('555','104');

/*PatientUsers*/
insert into patientusers
values('Wilitsehey','zei1aekuD');

insert into patientusers
values('Revent','rooGh1ahng9oo');

insert into patientusers
values('Pria1967','eiw8Aedaiqu');

insert into patientusers
values('Preft1955','Veecie4uay');

insert into patientusers
values('Crehose','eiYohvoh6');

/*StaffUsers*/
insert into staffusers
values('Sert1936','ieCohdaph5');

insert into staffusers
values('Givint','wies8Nu5xud');

insert into staffusers
values('Shouressunt','cheeGh5phae');

insert into staffusers
values('Grewle','xahshaeB4');

insert into staffusers
values('Lefordled','nu5EemahQu');

/*PUserHasA*/
insert into puserhasa
values('10001','Wilitsehey');

insert into puserhasa
values('15602','Revent');

insert into puserhasa
values('79856','Pria1967');

insert into puserhasa
values('97640','Preft1955');

insert into puserhasa
values('56841','Crehose');

/*SUserHasA*/
insert into suserhasa
values('469','Sert1936');

insert into suserhasa
values('231','Givint');

insert into suserhasa
values('198','Shouressunt');

insert into suserhasa
values('876','Grewle');

insert into suserhasa
values('555','Lefordled');  
