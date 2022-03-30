drop table travel;
create table travel(caseId VARCHAR(15), HolidayType VARCHAR(20), Price INTEGER, NumberOfPersons INTEGER, regionId bigint not null, Transportation VARCHAR(30), Duration INTEGER, Season VARCHAR(30), Accommodation VARCHAR(30), Hotel VARCHAR(50));
insert into travel values('Journey11','City',1978,2,1,'Plane',7,'April','ThreeStars','Hotel Victoria, Cairo');
insert into travel values('Journey21','Recreation',1568,2,2,'Car',14,'May','TwoStars','Hotel Ostend, Belgium');
insert into travel values('Journey38','Active',2739,3,3,'Plane',14,'July','FourStars','Hotel Burgas, Sunny Beach');
insert into travel values('Journey39','Bathing',2399,2,3,'Plane',14,'September','FourStars','Grandhotel Varna, Albena');
insert into travel values('Journey40','Bathing',2273,3,4,'Car',14,'August','HolidayFlat','H.Flat Bornholm');
insert into travel values('Journey41','Bathing',1729,5,4,'Car',7,'July','HolidayFlat','H.Flat Bornholm');
insert into travel values('Journey55','Recreation',648,2,5,'Car',7,'June','TwoStars','Gasthaus Meier, Allgaeu');
insert into travel values('Journey56','Wandering',972,4,5,'Car',7,'July','TwoStars','Gasthaus Alpenland, Allgaeu');


create table region(regionId bigint not null primary key, RegionName VARCHAR(30), NearestCity VARCHAR(50), Airport VARCHAR(50), Currency VARCHAR(50));
insert into region values(1, 'Cairo', 'Cairo', 'Cairo Airport', 'Egyptian Pound');
insert into region values(2, 'Belgium', 'Brussels', 'Brussels National Airport', 'Euro');
insert into region values(3, 'Bulgaria', 'Sofia', 'Sofia Airport', 'Lev');
insert into region values(4, 'Bornholm', 'Ronne', 'Bornholm Airport', 'Danish krone');
insert into region values(5, 'Allgaeu', 'Munich', 'Munich Airport', 'Euro');
