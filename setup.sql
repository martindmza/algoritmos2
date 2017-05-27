
DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
	id_persona INT(11) NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
	id_direccion INT(11) DEFAULT NULL,
	id_ocupacion INT(11) DEFAULT NULL,
	PRIMARY KEY (`id_persona`)
);


DROP TABLE IF EXISTS direccion;
CREATE TABLE direccion (
	id_direccion INT(11) NOT NULL AUTO_INCREMENT,
	calle VARCHAR(255) NOT NULL,
	numero INT(11) DEFAULT NULL,
	PRIMARY KEY (`id_direccion`)
);

DROP TABLE IF EXISTS ocupacion;
CREATE TABLE ocupacion (
	id_ocupacion INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	id_tipoocupacion INT(11) NOT NULL,
	PRIMARY KEY (`id_ocupacion`)
);

DROP TABLE IF EXISTS tipo_ocupacion;
CREATE TABLE tipo_ocupacion (
	id_tipoocupacion INT(11) NOT NULL AUTO_INCREMENT,
	descripcion VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id_tipoocupacion`)
);




