package tp.utn.methods;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;
import tp.utn.ann.Table;
import tp.utn.exceptions.EntityNotFoundException;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Delete extends Utn {

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static String _delete(Class<?> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();

		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = buildWhere(xql, dtoClass);
		}

		String sql = "DELETE FROM " + tableName + " AS " + getAlias(dtoClass) + " " + where + ";";
		System.out.println(sql);
		return sql;
	}

	// Invoca a: _delete para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int delete(Connection con, Class<?> dtoClass, String xql, Object... args) throws SQLException {
		String sql = _delete(dtoClass, xql);

		PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int count = 1;
		for (Object arg : args) {
			if (arg instanceof Integer) {
				pstm.setInt(count, (int) arg);
			} else if (arg instanceof Boolean) {
				pstm.setBoolean(count, (boolean) arg);
			} else if (arg instanceof Date) {
				pstm.setDate(count, (Date) arg);
			} else {
				pstm.setString(count, arg.toString());
			}
			count++;
		}

		return pstm.executeUpdate();
	}

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada
	// por id
	// (deberia ser: 1 o 0)
	// Invoca a: delete
	public static int delete(Connection con, Class<?> dtoClass, Object id) throws SQLException {
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass, getAlias(dtoClass));
		String idAttrName = idAttribute.getAttribute().getName();

		return delete(con, dtoClass, "$" + idAttrName + "= ? ", id);
	}
}
