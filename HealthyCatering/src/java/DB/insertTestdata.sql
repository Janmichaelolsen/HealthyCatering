INSERT INTO users VALUES ('customer','asd123','kunde','kundesen','adresseveien 3','46257954','martinalmvik@hotmail.com',7046);
INSERT INTO users VALUES ('driver','asd123','sjåfør','sjåførsen','adresseveien 21', '457845512','b@post.no', 7049 );
INSERT INTO users VALUES ('admin','asd123','admin','administratorsen','adresseveien 26', '457812412','c@post.no', 7049);
INSERT INTO users VALUES ('salesman','asd123','selger','selgersen','adresseveien 23', '457845512','d@post.no', 7049);
INSERT INTO users VALUES ('chef','asd123','kokk','administratorsen','adresseveien 26', '457824512','e@post.no', 7049);
INSERT INTO users VALUES ('bedrift','asd123','bedrift','bedriftersen','adresseveien 23', '457845132','f@post.no', 7049);
INSERT INTO users VALUES ('salesman2','asd123','selger','selgersen','adresseveien 23', '457845512','6@post.no', 7049);
INSERT INTO users VALUES ('salesman3','asd123','selger','selgersen','adresseveien 23', '457845512','7@post.no', 7049);
 
INSERT INTO roles VALUES ('admin','admin');
INSERT INTO roles VALUES ('customer','customer');
INSERT INTO roles VALUES ('chef','chef');
INSERT INTO roles VALUES ('driver','driver');
INSERT INTO roles VALUES ('salesman','salesman');
INSERT INTO roles VALUES ('salesman','salesman2');
INSERT INTO roles VALUES ('salesman','salesman3');

INSERT INTO customer VALUES ('customer');
INSERT INTO customer VALUES ('bedrift');
INSERT INTO private VALUES ('customer');

INSERT INTO employee VALUES ('admin', 1);
INSERT INTO employee VALUES ('customer', 1);

INSERT INTO administration VALUES ('admin');

INSERT INTO worker VALUES ('driver');
INSERT INTO worker VALUES ('salesman');
INSERT INTO worker VALUES ('chef');

INSERT INTO driver VALUES ('driver');
INSERT INTO chef VALUES ('chef');
INSERT INTO salesman VALUES ('salesman');
INSERT INTO salesman VALUES ('salesman2');
INSERT INTO salesman VALUES ('salesman3');

INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Suppelapskaus',112.00,'resources/pictures/dishImages/Supperlapskaus.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Salsabiff med peppercoleslaw',274.00,'resources/pictures/dishImages/Salsabiff.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Varm bulgursalat med strimlet svinekjøtt',96.00,'resources/pictures/dishImages/Bulgursalat.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Kalkunbiff med grillet frukt',233.00,'resources/pictures/dishImages/Kalkunbiff.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Minuttbiff med bakte grønnsaker',221.00,'resources/pictures/dishImages/Minuttbiff.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Afrikansk peanøttsuppe',83.00,'resources/pictures/dishImages/Peanottsuppe.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Karrikylling med banan og fennikel',143.00,'resources/pictures/dishImages/Karrikylling.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Kyllingform med kikerter og tomat',132.00,'resources/pictures/dishImages/Kyllingform.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Brennheit kyllinggryte med reker',154.00,'resources/pictures/dishImages/Kyllinggryte.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Svinebiff med sennep og kål',217.00,'resources/pictures/dishImages/Svinebiff.jpg');
INSERT INTO dish (dishname,dishprice,dishImagePath) VALUES ('Chilipølsegryte',108.00,'resources/pictures/dishImages/Chilipolse.jpg');