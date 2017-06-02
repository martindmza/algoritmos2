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

import tp.utn.JoinedElement;
import tp.utn.ann.Column;
import tp.utn.ann.JoinColumn;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.OneToMany;
import tp.utn.ann.OneToOne;
import tp.utn.ann.Table;
import tp.utn.fieldstypes.AbstractField;
import tp.utn.fieldstypes.AbstractFieldsMapping;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Utn {
	
	private static List<AbstractFieldsMapping> mappedFields = new ArrayList<AbstractFieldsMapping>();

	// Retorna: el SQL correspondiente a la clase dtoClass acotado por xql
	protected static <T> String _query(Class<T> dtoClass, String xql) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		
		String tableFields = "";
		String joins = "";
		for (Field f : dtoClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += getAlias(dtoClass) + "." + column.name() + " AS '" + getAlias(dtoClass) + "." + column.name() + "' ,";
			} else if (f.getAnnotation(ManyToOne.class) != null ) {
				//join
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				String targetAlias = getAlias(manyToOneColumn.type());
				PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type(), targetAlias);
				JoinedElement joinedElement = join(dtoClass, manyToOneColumn.type(), targetAlias, manyToOneColumn.name(), targetId.getColumnName()); 
				joins += " " + joinedElement.joinPart;
				
				//select field
				tableFields += joinedElement.fieldsPart;
				tableFields += getClassFieldsNames(manyToOneColumn.type());
			} else if (f.getAnnotation(OneToMany.class) != null) {
				//join
				OneToMany oneToManyColumn = f.getAnnotation(OneToMany.class);
				JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);				
				String targetAlias = getAlias(oneToManyColumn.type());
				PrimitiveField idColumn = FieldsTypesFactory.getIdAttribute(dtoClass, targetAlias);
				JoinedElement joinedElement = join(dtoClass, oneToManyColumn.type(), targetAlias, idColumn.getColumnName(), joinColumn.name()); 
				joins += " " + joinedElement.joinPart; 
				
				//select field
				tableFields += joinedElement.fieldsPart;
				tableFields += getClassFieldsNames(oneToManyColumn.type());
			} else if (f.getAnnotation(OneToOne.class) != null) {
				//join
				OneToOne OneToOneColumn = f.getAnnotation(OneToOne.class);
				String targetAlias = getAlias(OneToOneColumn.type());
				PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(OneToOneColumn.type(), targetAlias);
				JoinedElement joinedElement = join(dtoClass, OneToOneColumn.type(), targetAlias, OneToOneColumn.name(), targetId.getColumnName());
				joins += " " + joinedElement;
				
				//select field
				tableFields += joinedElement.fieldsPart;
				tableFields += getClassFieldsNames(OneToOneColumn.type());
			}
			
		}
		tableFields = tableFields.substring(0, tableFields.length() - 1);
		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = "WHERE " + xql;
		}

		String sql = "SELECT " + tableFields + " FROM " + tableName + " AS " + getAlias(dtoClass) + " " + joins + " " + where + ";";
		System.out.println(sql);

		return sql;
	}

	// Invoca a: _query para obtener el SQL que se debe ejecutar
	// Retorna: una lista de objetos de tipo T
	public static <T> List<T> query(Connection con, Class<T> dtoClass, String xql, Object... args) {
		String sql = _query(dtoClass, xql);
		addAbstractFields(dtoClass);

		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass, getAlias(dtoClass));
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
			//System.out.println(((JDBC4PreparedStatement) pstm).asSql());

			Constructor ctor = dtoClass.getConstructor();
			List<String> proccessedIds = new ArrayList<String>();
			while (rs.next()) {
				T entity = (T) ctor.newInstance();
				List<Object> entities = new ArrayList<Object>();
				for (AbstractFieldsMapping<T> mapping : mappedFields) {
					if (mapping.getClassType().getSimpleName().equals(dtoClass.getSimpleName())) {
						for (AbstractField myField : mapping.getAbstracFields()) {
							if (myField.getSetter() != null) {
								myField.getSetter().invoke(entity, myField.getParamForSetter(rs, con));
							}
						}
						continue;
					} 
					
					Constructor joinCtor = mapping.getClassType().getConstructor();
					Object joinedEntity = joinCtor.newInstance();
					for (AbstractField myField : mapping.getAbstracFields()) {
						if (myField.getSetter() != null) {
							myField.getSetter().invoke(joinedEntity, myField.getParamForSetter(rs, con));
						}
					}
					entities.add(joinedEntity);
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
	
	protected static <T,R> JoinedElement join(Class<T> dtoClass, Class<R> dtoTargetClass, String targetTableAlias, String originId, String targetId) {
		String alias = getAlias(dtoClass);
		addAbstractFields(dtoTargetClass);
		
		Table targetTable = dtoTargetClass.getAnnotation(Table.class);
		String targetTableName = targetTable.name();
		
		String join = 	"JOIN " + targetTableName + " AS " + targetTableAlias +
						" ON " + alias + "." + originId + " = " + targetTableAlias + "." + targetId + " " ;
		
		String tableFields = "";		
		for (Field f : dtoTargetClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += getAlias(dtoTargetClass) + "." + column.name() + " AS '" + getAlias(dtoTargetClass) + "." + column.name() + "' ,";
			} else if (f.getAnnotation(ManyToOne.class) != null) {
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				PrimitiveField subJoinTargetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type(), alias);
				String targetSubJoinAlias = getAlias(manyToOneColumn.type());
				JoinedElement joinedElement = join(dtoTargetClass, manyToOneColumn.type(), targetSubJoinAlias, manyToOneColumn.name(), subJoinTargetId.getColumnName()); 
				join += " " + joinedElement.joinPart; 
				tableFields += joinedElement.fieldsPart;
			}
		}
		
		return new JoinedElement(join,tableFields);
	}
	
	protected static <T> String getClassFieldsNames(Class<T> dtoClass) {
		String alias = getAlias(dtoClass);
		String tableFields = "";
		for (Field f : dtoClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += alias + "." + column.name() + " AS '" + alias + "." + column.name() + "' ,";
			}
		}
		return tableFields;
	}
	
	private static <T> void addAbstractFields(Class<T> dtoClass) {
		if (mappedFields.contains(dtoClass)) {
			return;
		}
		String alias = getAlias(dtoClass);
		List<AbstractField> abstracFields = new ArrayList<AbstractField>();
		for (Field f : dtoClass.getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dtoClass, alias);
			if (field != null && field instanceof PrimitiveField) {
				abstracFields.add(field);
			}
		}
		mappedFields.add(new AbstractFieldsMapping(dtoClass,abstracFields));
	}
}
