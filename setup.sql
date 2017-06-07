
DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
	id INT(11) NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
	id_direccion INT(11) DEFAULT NULL,
	id_ocupacion INT(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
);
INSERT INTO persona VALUES (1,'Tincho',1,5);
INSERT INTO persona VALUES (2,'Daniela',1,4);
INSERT INTO persona VALUES (3,'Carla',2,2);
INSERT INTO persona VALUES (4,'Jony',3,1);
INSERT INTO persona VALUES (5,'Victoria',2,3);

DROP TABLE IF EXISTS direccion;
CREATE TABLE direccion (
	id INT(11) NOT NULL AUTO_INCREMENT,
	calle VARCHAR(255) NOT NULL,
	numero INT(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
);
INSERT INTO direccion VALUES (1,'Carlos Tejedor',1455);
INSERT INTO direccion VALUES (2,'Tapalque',883);
INSERT INTO direccion VALUES (3,'Republica',5929);


DROP TABLE IF EXISTS ocupacion;
CREATE TABLE ocupacion (
	id INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	id_tipo_ocupacion INT(11) NOT NULL,
	PRIMARY KEY (`id`)
);
INSERT INTO ocupacion VALUES (1,'Carpintero',30);
INSERT INTO ocupacion VALUES (2,'Atencion Al Publico',20);
INSERT INTO ocupacion VALUES (3,'Estudiante',10);
INSERT INTO ocupacion VALUES (4,'Administrativo',20);
INSERT INTO ocupacion VALUES (5,'Desarrollador',20);


DROP TABLE IF EXISTS tipo_ocupacion;
CREATE TABLE tipo_ocupacion (
	id INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
);
INSERT INTO tipo_ocupacion VALUES (10,'Estudiante');
INSERT INTO tipo_ocupacion VALUES (20,'Empleado');
INSERT INTO tipo_ocupacion VALUES (30,'Independiente');


DROP TABLE IF EXISTS department;
CREATE TABLE department (
	id INT(11) NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	loc VARCHAR(255) ,
	PRIMARY KEY (`id`)
);
INSERT INTO department VALUES (1,'Legales',"b");
INSERT INTO department VALUES (2,'Recursos Humanos',"c");
INSERT INTO department VALUES (3,'Desarrollo',"a");


DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
	id INT(11) NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	hired_date Date ,
	department_id INT(11) NOT NULL,
	PRIMARY KEY (`id`)	
);
INSERT INTO employee VALUES (1,'martin-amaya'      ,'2014-02-02',3);
INSERT INTO employee VALUES (2,'juan-baragli'      ,'2014-02-02',3);
INSERT INTO employee VALUES (3,'estefania-devoto'  ,'2016-02-02',1);
INSERT INTO employee VALUES (4,'melisa-nose'       ,'2005-02-02',2);
