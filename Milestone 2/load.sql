/*
*   Group Members:  Lukas Stricker 14433, lsustricker@unibz.it 
*                   Philipp Scomparin 14109, Philipp.Scomparin@stud-inf.unibz.it
*/ 

INSERT into Damages (damages_id, description)
    VALUES (1, 'scratch Front Left Door'),
        (2, 'scratch Front Right Door'),
        (3, 'scratch Back Left Door'),
        (4, 'scratch Back Right Door'),
        (5, 'Flat Tyre Front Left Wheel');

INSERT into Car_Brands (car_brands_id, company_name)
    VALUES (1, 'Volkswagen'),
        (2, 'Audi'),
        (3, 'BMW'),
        (4, 'Lamborghini');

INSERT into Insurances (insurances_id, company_name, fee)
    VALUES (1, 'Allianz', '600'),
        (2, 'AXA', '1200'),
        (3, 'Tirolia', '460');

INSERT into Vehicles (license_plate, initial_registration, price_class, capacity, price_day, price_km, model, car_brands_id, insurances_id)
    VALUES ('CH477KJ', '20-10-2017', 'Sports Car', 2, 900, 1.5, 'Gallardo RS6', 1, 1),
    ('HI99H2D', '10.08.2016', 'Family', 7, 500, 1.1, 'California', 2, 2),
    ('U2JIW23', '01.01.2007', 'Luxury', 4, 700, 1.3, 'Q8 S Line', 3, 3);

INSERT into Addresses (addresses_id, city, cap, street, country)
    VALUES (1, 'Merano', 39012, 'via Roma 12', 'IT'),
        (2, 'BOlzano', 39100, 'via UNiverita 18', 'IT'),
        (3, 'Munich', 10294, 'Vogelwildstr. 34/A', 'DE');


insert into Equipment (equipments_id, description)
    VALUES (1, 'ABS'),
        (2, 'Air conditioner'),
        (3, 'Automatic gearbox'),
        (4, 'Manual gearbox');

insert into Extra_Equipment(extra_equipments_id, description, price, total_quantity)
    VALUES (1, 'Child seat', 50, 1),
        (2, 'roof rack', 70, 2),
        (3, 'Navigation System', 79, 3);


insert into Bills (bills_id, payment_method, bill, date, total_price, date_of_payment) 
    VALUES (1, 'Mastercard', 'Audi + child seat', '2016-06-20', 1000, '2016-06-27'),
        (2, 'VISA', 'Lamborghini', '2017-02-10', 5000, '2017-02-13'),
        (3, 'Cash', 'Volkswagen + Navigation System', '2017-05-24', 1500, '2017-05-30');

insert into Clients (clients_id, first_name, last_name, phone, driving_license_number, addresses_id) 
        VALUES (1, 'Lukas', 'Pfattner', '3345695', '234324D', 2),
        (2, 'Romero', 'Rodriguez', '2344789', '467385E', 3),
        (3, 'Zlatan', 'Bonucci', '4673839', '374857F', 1);

insert into Reservations (reservations_id, date_starttime, date_endtime, km_at_start, km_at_return, license_plate, clients_id, bills_id)
    VALUES (1, '2016-06-10 10:00', '2016-06-12 09:00', 1100, 1200, 'CH477KJ', 1, 1),
    (2, '2017-01-20 10:00', '2017-01-25 09:00', 80000, 82000, 'HI99H2D', 2, 2),
    (3, '2017-05-19 11:00', '2017-05-21 12:00', 29000, 30507, 'U2JIW23', 3, 3);

INSERT into Vehicles_Equipment
    VALUES ('CH477KJ', 1),
        ('HI99H2D', 2);

INSERT into Reservations_Damages
    VALUES (3, 2, 350),
        (1,1, 400);

INSERT into Reservations_ExtraEquipment
    VALUES (1, 1, 2),
        (2, 2, 2);