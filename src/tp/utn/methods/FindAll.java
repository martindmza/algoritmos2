package tp.utn.methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tp.utn.ann.Column;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.ManyToOneField;
import tp.utn.fieldstypes.PrimitiveField;

public class FindAll extends Utn {

	// Retorna: una todasa las filas de la tabla representada por dtoClass
	// Invoca a: query
	public static <T> List<T> findAll(Connection con, Class<T> dtoClass)
	{
		return query(con,dtoClass,"");
	}
}
