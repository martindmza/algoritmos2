
DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
	id_persona INT(11) NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
	id_direccion INT(11) DEFAULT NULL,
	id_ocupacion INT(11) DEFAULT NULL,
	PRIMARY KEY (`id_persona`)
);
INSERT INTO persona VALUES (1,'Tincho',1,10);
INSERT INTO persona VALUES (2,'Daniela',1,20);
INSERT INTO persona VALUES (3,'Victoria',2,10);
INSERT INTO persona VALUES (4,'Carla',2,20);
INSERT INTO persona VALUES (5,'Jony',3,30);


DROP TABLE IF EXISTS direccion;
CREATE TABLE direccion (
	id_direccion INT(11) NOT NULL AUTO_INCREMENT,
	calle VARCHAR(255) NOT NULL,
	numero INT(11) DEFAULT NULL,
	PRIMARY KEY (`id_direccion`)
);
INSERT INTO direccion VALUES (1,'Carlos Tejedor',1455);
INSERT INTO direccion VALUES (2,'Tapalque',883);
INSERT INTO direccion VALUES (3,'Republica',5929);


DROP TABLE IF EXISTS ocupacion;
CREATE TABLE ocupacion (
	id_ocupacion INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	id_tipoocupacion INT(11) NOT NULL,
	PRIMARY KEY (`id_ocupacion`)
);
INSERT INTO ocupacion VALUES (10,'Estudiante');
INSERT INTO ocupacion VALUES (20,'Empleado');
INSERT INTO ocupacion VALUES (30,'Independiente');
INSERT INTO ocupacion VALUES (10,'Estudiante');
INSERT INTO ocupacion VALUES (10,'Estudiante');


DROP TABLE IF EXISTS tipo_ocupacion;
CREATE TABLE tipo_ocupacion (
	id_tipoocupacion INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id_tipoocupacion`)
);
INSERT INTO id_tipoocupacion VALUES (10,'Estudiante');
INSERT INTO id_tipoocupacion VALUES (20,'Empleado');
INSERT INTO id_tipoocupacion VALUES (30,'Independiente');