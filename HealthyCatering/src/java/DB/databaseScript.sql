DROP TABLE dishes_stored; 
DROP TABLE dishes_ordered;
DROP TABLE sub_dish;
DROP TABLE private;
DROP TABLE orders;
DROP TABLE subscriptionplan;
DROP TABLE customer;
DROP TABLE dish;
DROP TABLE salesman;
DROP TABLE chef;
DROP TABLE driver;
DROP TABLE worker;
DROP TABLE administration;
DROP TABLE employee;
DROP TABLE roles;
DROP TABLE users;
DROP TABLE postal_no;
DROP TABLE munincipial_no;
DROP TABLE county_no;
DROP TABLE message;

CREATE TABLE county_no (
    id SMALLINT,
    county_name VARCHAR(100) NOT NULL,
    CONSTRAINT county_no_pk PRIMARY KEY (id)
);

CREATE TABLE munincipial_no (
    id SMALLINT,
    county_id SMALLINT NOT NULL,
    munincipial_name VARCHAR(100) NOT NULL,
    CONSTRAINT munincipial_no_pk PRIMARY KEY (id),
    CONSTRAINT munincipial_no_fk FOREIGN KEY (county_id) REFERENCES county_no (id)
); 

CREATE TABLE postal_no (
    zip SMALLINT,
    place VARCHAR(100) NOT NULL,
    munincipial_id SMALLINT NOT NULL,
    CONSTRAINT postal_no_pk PRIMARY KEY (zip),
    CONSTRAINT postal_no_fk FOREIGN KEY (munincipial_id) REFERENCES munincipial_no (id)
);

CREATE TABLE users(
    username VARCHAR(20) NOT NULL,
    password VARCHAR(40) NOT NULL,
    firstname VARCHAR(40),
    surname VARCHAR(40),
    address VARCHAR(40) NOT NULL,
    mobileNr VARCHAR(20),
    email VARCHAR(40) NOT NULL UNIQUE,
    postalCode SMALLINT,
    CONSTRAINT users_pk PRIMARY KEY(username),
    CONSTRAINT users_fk1 FOREIGN KEY(postalCode) REFERENCES postal_no(zip)
);
CREATE TABLE roles(
    roleName VARCHAR(20),
    username VARCHAR(20),
    CONSTRAINT roles_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT roles_pk PRIMARY KEY(username,roleName)
);
CREATE TABLE employee(
    username VARCHAR(20) NOT NULL,
    employee_nr INTEGER NOT NULL,
    CONSTRAINT employee_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT employee_pk PRIMARY KEY(username)
);
CREATE TABLE worker(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT worker_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT worker_pk PRIMARY KEY(username)
);

CREATE TABLE driver(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT driver_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT driver_pk PRIMARY KEY(username)
);

CREATE TABLE salesman(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT salesman_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT salesman_pk PRIMARY KEY(username)
);

CREATE TABLE chef(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT chef_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT chef_pk PRIMARY KEY(username)
);

CREATE TABLE administration(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT administration_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT administration_pk PRIMARY KEY(username)
);
CREATE TABLE customer(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT customer_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT customer_pk PRIMARY KEY(username)
);
CREATE TABLE private(
    username VARCHAR(20) NOT NULL,
    CONSTRAINT private_fk FOREIGN KEY(username) REFERENCES users(username),
    CONSTRAINT private_pk PRIMARY KEY(username)
);
CREATE TABLE subscriptionplan(
    subscriptionId INTEGER AUTO_INCREMENT,
    startDate date,
    endDate date,
    timeofdelivery time,
    deliveryaddress varchar(30),
    totalprice decimal,
    weekday smallint,
    postalcode SMALLINT,
    description varchar(30),
    companyusername varchar(30),
    CONSTRAINT subscriptionplan_pk PRIMARY KEY(subscriptionId)
); 

CREATE TABLE orders(
    orderId INTEGER AUTO_INCREMENT,
    timeOfDelivery time,
    deliveryAddress VARCHAR (50),
    status INTEGER,
    userNameCustomer VARCHAR(20),
    subscriptionId INTEGER,
    postalCode SMALLINT,
    dates date,
    description varchar(30),
    totalPrice DECIMAL,
    CONSTRAINT orders_pk PRIMARY KEY(orderId),
    CONSTRAINT orders_fk1 FOREIGN KEY(userNameCustomer) REFERENCES Customer(username),
    CONSTRAINT orders_fk2 FOREIGN KEY(subscriptionId) REFERENCES Subscriptionplan(subscriptionid),
    CONSTRAINT orders_fk3 FOREIGN KEY(postalCode) REFERENCES postal_no(zip)
);
CREATE TABLE dish(
    dishId INTEGER NOT NULL AUTO_INCREMENT,
    dishName VARCHAR(50) NOT NULL,
    dishPrice DECIMAL NOT NULL,
    dishImagePath VARCHAR(50),
    CONSTRAINT dish_pk PRIMARY KEY(dishId)
);
CREATE TABLE sub_dish(
    dishId INTEGER NOT NULL,
    subId INTEGER NOT NULL,
    dishCount INTEGER NOT NULL,
    CONSTRAINT subdish_fk FOREIGN KEY(subId) REFERENCES Subscriptionplan(subscriptionId),
    CONSTRAINT subdish_fk2 FOREIGN KEY(dishId) REFERENCES dish(dishId),
    CONSTRAINT subdish_pk PRIMARY KEY(subId,dishId)
);
CREATE TABLE dishes_ordered(
    dishId INTEGER NOT NULL,
    orderId INTEGER NOT NULL,
    dishCount INTEGER NOT NULL,
    salesmanusername VARCHAR(20) NOT NULL,
    CONSTRAINT dishes_ordered_fk FOREIGN KEY(orderId) REFERENCES orders(orderID),
    CONSTRAINT dishes_ordered_fk2 FOREIGN KEY(dishId) REFERENCES dish(dishId),
    CONSTRAINT dishes_ordered_pk PRIMARY KEY(orderId,dishId)
);
CREATE TABLE dishes_stored(
    dishId INTEGER NOT NULL,
    orderId INTEGER NOT NULL,
    dishCount INTEGER NOT NULL,
    totalPrice decimal, 
    dates date,
    postalcode SMALLINT,
    salesmanusername VARCHAR(40),
    CONSTRAINT dishes_stored_fk1 FOREIGN KEY(dishId) REFERENCES dish(dishId),
    CONSTRAINT dishes_stores_pk PRIMARY KEY(orderId,dishId)
);

CREATE TABLE message(
    messageId INTEGER NOT NULL AUTO_INCREMENT,
    message VARCHAR(100),
    CONSTRAINT message_pk PRIMARY KEY(messageId)
);
