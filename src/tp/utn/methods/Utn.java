package tp.utn.methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.JDBC4PreparedStatement;

import tp.utn.ann.Column;
import tp.utn.ann.JoinColumn;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Utn {
	
	protected static <T,R> String join(Class<T> dtoClass, Class<R> dtoTargetClass, String targetTableAlias, String originId, String targetId) {
		String alias = getAlias(dtoClass);
		
		Table targetTable = dtoTargetClass.getAnnotation(Table.class);
		String targetTableName = targetTable.name();
		
		String join = 	"JOIN " + targetTableName + " AS " + targetTableAlias +
						" ON " + alias + "." + originId + " = " + targetTableAlias + "." + targetId + " " ;
		
		for (Field f : dtoTargetClass.getDeclaredFields()) {
			if (f.getAnnotation(ManyToOne.class) != null) {
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				PrimitiveField subJoinTargetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type());
				String targetSubJoinAlias = getAlias(manyToOneColumn.type());
				join += " " + join(dtoTargetClass, manyToOneColumn.type(), targetSubJoinAlias, manyToOneColumn.name(), subJoinTargetId.getColumnName());
			}
		}
		
		return join;
	}
	
	protected static <T> String getClassFieldsNames(Class<T> dtoClass) {
		String alias = getAlias(dtoClass);
		String tableFields = "";
		for (Field f : dtoClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += alias + "." + column.name() + ",";
			}
		}
		return tableFields;
	}

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	protected static <T> String _query(Class<T> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		
		String tableFields = "";
		String joins = "";
		for (Field f : dtoClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += getAlias(dtoClass) + "." + column.name() + ",";
			} else if (f.getAnnotation(ManyToOne.class) != null ) {
				//join
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type());
				String targetAlias = getAlias(manyToOneColumn.type());
				joins += " " + join(dtoClass, manyToOneColumn.type(), targetAlias, manyToOneColumn.name(), targetId.getColumnName());
				
				//select field
				tableFields += getClassFieldsNames(manyToOneColumn.type());
			} else if (f.getAnnotation(OneToMany.class) != null) {
				//join
				OneToMany oneToManyColumn = f.getAnnotation(OneToMany.class);
				JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);				
				String targetAlias = getAlias(oneToManyColumn.type());
				PrimitiveField idColumn = FieldsTypesFactory.getIdAttribute(dtoClass);
				joins += " " + join(dtoClass, oneToManyColumn.type(), targetAlias, idColumn.getColumnName(), joinColumn.name());
				
				//select field
				tableFields += getClassFieldsNames(oneToManyColumn.type());
			} else if (f.getAnnotation(OneToMany.class) != null) {
				//join
				
				//select field
				
			}
			
		}
		tableFields = tableFields.substring(0, tableFields.length() - 1);
		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = "WHERE " + xql;
		}

		String sql = "SELECT " + tableFields + " FROM " + tableName + " AS " + getAlias(dtoClass) + " " + joins + " " + where + ";";
		System.out.println(sql);
		System.exit(1);
		return sql;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) {
		String sql = _query(dtoClass, xql);

		List<AbstractField> classFields = new ArrayList<AbstractField>();
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass);
		for (Field f : dtoClass.getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dtoClass);
			if (field != null) {
				classFields.add(field);
			}
		}

		List<T> result = new ArrayList<T>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = con.prepareStatement(sql);
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

			rs = pstm.executeQuery();
			System.out.println(((JDBC4PreparedStatement) pstm).asSql());

			Constructor ctor = dtoClass.getConstructor();
			while (rs.next()) {
				T entity = (T) ctor.newInstance();
				Object id = idAttribute.getGetter().invoke(entity);

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
	
	private static <T> String getAlias (Class<T> dtoClass) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		String alias = table.alias();
		if (alias.trim().equals("")) {
			alias = tableName;
		}
		
		return alias;
	}
}
