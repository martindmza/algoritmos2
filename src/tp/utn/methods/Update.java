package tp.utn.methods;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;

import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Update extends Utn {
	
	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	public static <T> String _update(Class<T> dtoClass, String xql, Object... args) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();

		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = "WHERE " + xql;
		}
		
		String sets = "";
		for (Object arg : args) {
			sets += " ?, ";
		}
		sets = sets.substring(0, sets.length() - 1);

		String sql = "UPDATE " + tableName + " SET " + sets + " " + where + " ;";
		System.out.println(sql);

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
	public static int update(Connection con, Object dto) throws SQLException, Exception {
		List<AbstractField> classFields = new ArrayList<AbstractField>();
		for (Field f : dto.getClass().getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dto.getClass());
			if (field != null) {
				classFields.add(field);
			}
		}
		
		String sql = _update(dto.getClass(), " id + ? ", classFields );
		
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dto.getClass());
		Integer id = (int) idAttribute.getGetter().invoke(dto);
		
		PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		pstm.setInt(1, id);
		
		int count = 2;
		for (AbstractField f : classFields) {
			String type = f.getAttribute().getType().getSimpleName();
			if (type.equals("Integer") || type.equals("int")) {
				int value = (int) f.getGetter().invoke(dto);
				pstm.setInt(count, value);
			} else if (type.equals("String")) {
				String value = (String) f.getGetter().invoke(dto);
				pstm.setString(count, value);
			} else if (type.equals("Boolean") || type.equals("boolean")) {
				Boolean value = (Boolean) f.getGetter().invoke(dto);
				pstm.setBoolean(count, value);
			} else if (type.equals("Date")) {
				Date value = (Date) f.getGetter().invoke(dto);
				pstm.setDate(count, value);
			} else {
				Object value = f.getGetter().invoke(dto);
				if (f.getAttribute().getAnnotation(OneToMany.class) != null) {
					int id2 = update(con, value);
					pstm.setInt(count, id2);
				}
			}
			count++;
		}
		
		System.out.println(((JDBC4PreparedStatement)pstm).asSql());
		int result = pstm.executeUpdate();
		return result;
	}
}
