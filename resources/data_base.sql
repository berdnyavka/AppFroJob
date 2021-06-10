drop table if exists Payment;
drop table if exists Receipt;
drop table if exists Contractor;
drop table if exists Budget;
drop table if exists Project;
drop table if exists Bank;
drop table if exists Company;

CREATE TABLE Company
(
    Id SERIAL not null PRIMARY KEY,
	Date date default CURRENT_TIMESTAMP,
    Name varchar(30) not null UNIQUE,
	sum_receipt decimal,
	sum_pay_true decimal,
	sum_pay_false decimal
);

CREATE TABLE Project
(
    Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
    Id serial not null primary key,
	Date date default CURRENT_TIMESTAMP,
    Name varchar(30) not null,
	sum_receipt decimal,
	sum_pay_true decimal,
	sum_pay_false decimal

    UNIQUE (name,company_id)
);

CREATE TABLE Budget
(
    Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
    Project_id serial references Project(Id) ON DELETE RESTRICT,
    Id SERIAL not null PRIMARY KEY,
	Date date default CURRENT_TIMESTAMP,
    Name varchar(30) not null,
    Plan decimal,
    Fact decimal,

    UNIQUE (name, Project_id)

);

CREATE TABLE Contractor
(
    Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
    Project_id serial references Project(Id) ON DELETE RESTRICT,
    Budget_id serial REFERENCES Budget(Id) ON DELETE RESTRICT,
    Id SERIAL not null PRIMARY KEY,
	Date date default CURRENT_TIMESTAMP,
    Name varchar(30) not null,

    UNIQUE (name, Budget_id)

);


CREATE TABLE Payment
(
	Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
	Project_id serial references Project(Id) ON DELETE RESTRICT,
	Budget_id serial REFERENCES Budget(Id) ON DELETE RESTRICT,
	Contractor_id serial references Contractor(Id) ON DELETE RESTRICT,
	Id SERIAL not null PRIMARY KEY,
	Date date default CURRENT_TIMESTAMP,
	Sum DECIMAL NOT NULL,
	Purpose VARCHAR(30) not null,
	Source VARCHAR(30) not null,
	Confirmation boolean,
    Advice VARCHAR(30),
    Comment VARCHAR(300)

);

CREATE TABLE Receipt
(
    Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
	Project_id serial references Project(Id) ON DELETE RESTRICT,
	Budget_id serial REFERENCES Budget(Id) ON DELETE RESTRICT,
	Contractor_id serial references Contractor(Id) ON DELETE RESTRICT,
	Id SERIAL not null PRIMARY KEY,
	Date date default CURRENT_TIMESTAMP,
	Sum DECIMAL NOT NULL,
	Purpose VARCHAR(30) not null,
	Source VARCHAR(30) not null,
    Comment VARCHAR(300)

);

create table Bank
(
    Company_id serial REFERENCES Company(Id) ON DELETE RESTRICT,
    Id serial not null primary key,
	Date date default CURRENT_TIMESTAMP,
    Name varchar(30) not null,
    Account decimal

);



 insert into Company (id,name) values
 (11,'Катя'),
 (12,'Вероника'),
 (13,'Компания3'),
 (14,'Компания4');

 insert into Project (company_id,id,name) values
  (11,51,'Здоровье'),
  (11,52,'Питание'),
  (11,53,'Одежда'),
  (11,54,'Жилье');


 insert into Budget (company_id,project_id,id,name,plan) values
 (11,51,21,'зубы',30000.00),
 (11,51,22,'кожа',40000.50),
 (11,51,23,'позвоночник',5000.00),
 (11,51,24,'витамины',15000.90);


insert into Contractor (company_id,project_id,budget_id,id,name) values
 (11,51,21,61,'Стоматология№1'),
 (11,51,21,62,'Добрый врач'),
 (11,51,21,63,'Витэк'),
 (11,51,21,64,'СД');


 insert into Payment (company_id,project_id,budget_id,contractor_id,purpose,sum,advice,confirmation,date,source,comment) values
 (11,51,21,61,'за брекеты',13000.00,'реком','true','2021-06-20','касса','проверить'),
 (11,51,21,61,'за снятие ретейнера',14000.00,'реком','true','2021-06-20','касса','comment1'),
 (11,51,21,61,'оплата каппы',15000.00,'реком','false','2021-06-20','касса','comment2'),
 (11,51,21,61,'профессиональная чистка зубов',26000.00,'реком2','false','2021-06-18','р/с','нужно спросить'),
 (11,51,21,61,'профилактический осмотр',10000.90,'реком3','true','2021-06-05','касса','позвонить'),
 (11,51,21,61,'контейнер для каппы',28278.50,'реком4','false','2021-06-24','р/с','записать телефон');



insert into Receipt (company_id,project_id,budget_id,contractor_id,purpose,sum,date,source,comment) values
  (11,51,21,61,'на брекеты',13000.00,'2021-06-20','касса','проверить'),
 (11,51,21,61,'на чистку зубов',14000.00,'2021-06-20','касса','comment1'),
 (11,51,21,61,'на ретейнер',15000.00,'2021-06-20','касса','comment2');


insert into Bank (company_id,id,name,account) values
 (11,41,'сбербанк',111000),
 (12,42,'тиньков',222000),
 (13,43,'дом.ру',333000),
 (14,44,'втб',444000);
