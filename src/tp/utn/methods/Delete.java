package tp.utn.methods;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;
import tp.utn.ann.Table;

public class Delete extends Utn {

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static String _delete(Class<?> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();

		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = "WHERE " + xql;
		}

		String sql = "DELETE FROM " + tableName + " " + where + ";";

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

		System.out.println(((JDBC4PreparedStatement)pstm).asSql());
		int result = pstm.executeUpdate();
		return result;
	}

	// Retorna la cantidad de filas afectadas al eliminar la fila identificada
	// por id
	// (deberia ser: 1 o 0)
	// Invoca a: delete
	public static int delete(Connection con, Class<?> dtoClass, Object id) throws SQLException {
		String sql = _delete(dtoClass, "id = ? ");

		PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setInt(1, (int) id);
		System.out.println(((JDBC4PreparedStatement)pstm).asSql());
		int result = pstm.executeUpdate();

		return result;
	}
}
