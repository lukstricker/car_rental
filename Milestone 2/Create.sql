/*
*   Group Members:  Lukas Stricker 14433, lsustricker@unibz.it 
*                   Philipp Scomparin 14109, Philipp.Scomparin@stud-inf.unibz.it
*/ 

CREATE table Damages (
    damages_id SERIAL primary key,
    description varchar(80),
    position_part varchar(80)
);

CREATE table Car_Brands (
    car_brands_id SERIAL primary key,
    company_name varchar(60)
);

CREATE table Insurances (
    insurances_id SERIAL primary key,
    company_name varchar(60),
    fee integer DEFAULT 0
);

CREATE table Vehicles (
    license_plate varchar(80) unique primary key, --Assumption: There are no cars with the same license_plate.
    initial_registration date NOT NULL,
    price_class varchar(25),
    capacity integer NOT NULL,
    price_day integer,
    price_km integer,
    model varchar(30),
    car_brands_id integer,
    insurances_id integer,
    foreign key(car_brands_id) REFERENCES Car_Brands(car_brands_id) on delete RESTRICT, --no brands should be deleted as long as we have vehicles of that brand
    foreign key(insurances_id) REFERENCES Insurances(insurances_id) on delete RESTRICT -- no insurances should be deleted as long we use cars that have this insurance
);

CREATE table Addresses (
    addresses_id SERIAL primary key,
    city varchar(50),
    cap integer,
    street varchar(50),
    country varchar(50)
);

CREATE table Clients (
    clients_id SERIAL primary key,
    first_name varchar(40)NOT NULL,
    last_name varchar(40) NOT NULL,
    phone integer,
    driving_license_number varchar(50) NOT NULL,
    addresses_id integer,
    foreign key(addresses_id) REFERENCES Addresses(addresses_id) on update CASCADE --there should always be the current address of the client
);

CREATE table Bills (
    bills_id SERIAL primary key,
    payment_method varchar(80) NOT NULL,
    bill text NOT NULL,
    date date DEFAULT CURRENT_DATE,
    total_price integer NOT NULL,
    date_of_payment date
);
--The relation between Reservations and Bills is N:1 by the assumption that one Bill can contain many reservations and one reservation has only one bill.
CREATE table Reservations (
    reservations_id SERIAL primary key,
    date_starttime TIMESTAMP,
    date_endtime TIMESTAMP,
    km_at_start integer NOT NULL,
    km_at_return integer,
    license_plate varchar(15),
    clients_id integer,
    bills_id integer,
    foreign key(license_plate) REFERENCES Vehicles(license_plate) on update CASCADE, --if the license_plate changes it should also change here.
    foreign key(clients_id) REFERENCES Clients(clients_id) on delete RESTRICT, 
    foreign key(bills_id) REFERENCES Bills(bills_id) on delete RESTRICT --no bill of a reservation can be deleted.
);

CREATE table Extra_Equipment (
    extra_equipments_id SERIAL primary key,
    description varchar(80) NOT NULL,
    price integer DEFAULT 0,
    total_quantity integer DEFAULT 0
);

CREATE table Equipment (
    equipments_id SERIAL primary key,
    description varchar(50) NOT NULL
);

CREATE table Vehicles_Equipment (
    license_plate varchar(80) REFERENCES Vehicles(license_plate) on update CASCADE on delete CASCADE, --if the license_plate changes it should also change here and if a vehicle is deleted it should also be removed from this table.
    equipments_id integer REFERENCES Equipment(equipments_id) on delete RESTRICT,
    primary key(license_plate, equipments_id)
);

CREATE table Reservations_Damages (
    damages_id integer REFERENCES Damages(damages_id) on delete RESTRICT,
    reservations_id integer REFERENCES Reservations(reservations_id) on delete RESTRICT,
    fine integer DEFAULT 0,
    primary key(damages_id, reservations_id)
);

CREATE table Reservations_ExtraEquipment (
    extra_equipments_id integer REFERENCES Extra_Equipment(extra_equipments_id) on delete RESTRICT,
    reservations_id integer REFERENCES Reservations(reservations_id) on delete RESTRICT,
    quantity integer NOT NULL,
    primary key(extra_equipments_id, reservations_id)    
);