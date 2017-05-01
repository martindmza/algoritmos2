package tp.utn.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Utn {

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	protected static <T> String _query(Class<T> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		// String tableFields = "";
		// for (Field f : dtoClass.getDeclaredFields()) {
		// if (f.getAnnotation(Column.class) != null) {
		// Column column = f.getAnnotation(Column.class);
		// tableFields += column.name() + ",";
		// } else if (f.getAnnotation(ManyToOne.class) != null) {
		// ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
		// tableFields += manyToOneColumn.name() + ", ";
		// }
		// }
		// tableFields = tableFields.substring(0, tableFields.length() - 1);
		String where = "";
		if (!xql.trim().equals("")) {
			where = "WHERE " + xql;
		}
		
		String sql =  "SELECT * FROM " + tableName + " " + where + ";";
		System.out.println(sql);
		return sql;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) {
		List<AbstractField> classFields = new ArrayList<AbstractField>();
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass);
		for (Field f : dtoClass.getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dtoClass);
			if (field != null) {
				classFields.add(field);
			}
		}

		String sql = _query(dtoClass, xql);

		List<T> result = new ArrayList<T>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();

			Constructor ctor = dtoClass.getConstructor();
			while (rs.next()) {
				T entity = (T) ctor.newInstance();
				Object id =  idAttribute.getGetter().invoke(entity);
				
				for (AbstractField myField : classFields) {
					if (myField.getSetter() != null) {
						myField.getSetter().invoke(entity, myField.getParamForSetter(rs, con));
					}
				}
				result.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return (List<T>) result;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _update(Class<T> dtoClass, String xql) {
		return null;
	}

	// Invoca a: _update para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int update(Connection con, Class<?> dtoClass, String xql, Object... args) {
		return 0;
	}

	// Invoca a: update
	// Que hace?: actualiza todos los campos de la fila identificada por el id
	// de dto
	// Retorna: Cuantas filas resultaron modificadas (deberia: ser 1 o 0)
	public static int update(Connection con, Object dto) {
		return 0;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static String _delete(Class<?> dtoClass, String xql) {
		return null;
	}

	// Invoca a: _delete para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int delete(Connection con, Class<?> dtoClass, String xql, Object... args) {
		return 0;
	}

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada
	// por id
	// (deberia ser: 1 o 0)
	// Invoca a: delete
	public static int delete(Connection con, Class<?> dtoClass, Object id) {
		return 0;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass
	public static String _insert(Class<?> dtoClass) {
		return null;
	}

	// Invoca a: _insert para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int insert(Connection con, Object dto) {
		return 0;
	}

}
