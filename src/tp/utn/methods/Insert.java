package tp.utn.methods;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;

import tp.utn.ann.Id;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Insert extends Utn {

	// Retorna: el SQL correspondiente a la clase dtoClass
	public static String _insert(Class<?> dtoClass) {
		return null;
	}

	// Invoca a: _insert para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int insert(Connection con, Object dto) throws Exception {
		Table table = dto.getClass().getAnnotation(Table.class);
		String tableName = table.name();

		List<AbstractField> classFields = new ArrayList<AbstractField>();
		PrimitiveField idClassField = null; 
		for (Field f : dto.getClass().getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dto.getClass());
			if (field != null) {
				classFields.add(field);
			}
		}

		String inserts = "";
		String values = "";
		List<AbstractField> bindFields = new ArrayList<AbstractField>();
		for (AbstractField f : classFields) {
			if (f.getAttribute().getAnnotation(Id.class) != null) {
				Id idField = f.getAttribute().getAnnotation(Id.class);
				if (idField.strategy() == idField.IDENTITY) {
					idClassField = (PrimitiveField) f;
					continue;
				}
			}
			inserts += f.getColumnName() + ",";
			values += "?,";
			bindFields.add(f);
		}
		inserts = inserts.substring(0, inserts.length() - 1);
		values = values.substring(0, values.length() - 1);

		String sql = "INSERT INTO " + tableName + " ( " + inserts + " ) VALUES ( " + values + " );";

		PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int count = 1;
		for (AbstractField f : bindFields) {
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
					int id = insert(con, value);
					pstm.setInt(count, id);
				}
			}
			count++;
		}

		System.out.println(((JDBC4PreparedStatement)pstm).asSql());
		pstm.executeUpdate();
		
		ResultSet rs = pstm.getGeneratedKeys();
		Integer insertedId = 0;
		if (rs.next()) {
			insertedId = rs.getInt(1);
		}
		idClassField.getSetter().invoke(dto,insertedId);

		return insertedId;
	}
}
