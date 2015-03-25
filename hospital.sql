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