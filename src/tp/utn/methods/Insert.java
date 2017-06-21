package tp.utn.methods;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.mysql.jdbc.Statement;

import tp.utn.ann.Id;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;
import tp.utn.exceptions.EntityNotFoundException;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Insert extends Utn {

	// Retorna: el SQL correspondiente a la clase dtoClass
	public static String _insert(Object dto) {
		Table table = dto.getClass().getAnnotation(Table.class);
		String tableName = table.name();
		String inserts = "";
		String values = "";

		try {
			for (Field f : dto.getClass().getDeclaredFields()) {
				AbstractField field = FieldsTypesFactory.buildfieldType(f, dto.getClass(), getAlias(dto.getClass()));
				if (field != null && field.getGetter().invoke(dto) != null) {
					inserts += field.getColumnName() + ",";
					values += "?,";
				}
			}
			inserts = inserts.substring(0, inserts.length() - 1);
			values = values.substring(0, values.length() - 1);

		} catch (IllegalAccessException e) {

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "INSERT INTO " + tableName + " ( " + inserts + " ) VALUES ( " + values + " );";
		System.out.println(sql);
		return sql;
	}

	// Invoca a: _insert para obtener el SQL que se debe ejecutar
	// Retorna: la cantidad de filas afectadas luego de ejecutar el SQL
	public static int insert(Connection con, Object dto) throws EntityNotFoundException ,Exception {
		String sql = _insert(dto);
		PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		int count = 1;
		for (Field attribute : dto.getClass().getDeclaredFields()) {
			AbstractField f = FieldsTypesFactory.buildfieldType(attribute, dto.getClass(), getAlias(dto.getClass()));
			if (f != null && f.getGetter().invoke(dto) != null) {
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
					if (value != null && f.getAttribute().getAnnotation(ManyToOne.class) != null) {
						ManyToOne manyToOne = f.getAttribute().getAnnotation(ManyToOne.class);
						PrimitiveField valueIdField = FieldsTypesFactory.getIdAttribute(manyToOne.type(), getAlias(manyToOne.type()));
						if (valueIdField.getGetter().invoke(value) != null) {
							int id = (int) valueIdField.getGetter().invoke(value); 
							Find.find(con, value.getClass(), id);
							pstm.setInt(count, id);
						} else {
							int id = insert(con, value);
							pstm.setInt(count, id);
						}
					}
				}
				count++;
			}
		}
		
		pstm.executeUpdate();
		
		ResultSet rs = pstm.getGeneratedKeys();
		Integer insertedId = 0;
		if (rs.next()) {
			insertedId = rs.getInt(1);
		}
		
		PrimitiveField idClassField = FieldsTypesFactory.getIdAttribute(dto.getClass(), getAlias(dto.getClass()));
		idClassField.getSetter().invoke(dto, insertedId);

		return insertedId;
	}
}
