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
import tp.utn.fieldstypes.ManyToOneField;
import tp.utn.fieldstypes.ObjectMappedRow;
import tp.utn.fieldstypes.OneToManyField;
import tp.utn.fieldstypes.OneToOneField;
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
				tableFields += getAlias(dtoClass) + "." + column.name() + " AS '" + getAlias(dtoClass) + "."
						+ column.name() + "' ,";
			} else if (f.getAnnotation(ManyToOne.class) != null) {
				// join
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				String targetAlias = getAlias(manyToOneColumn.type());
				PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type(), targetAlias);
				ManyToOneField manyToOneField = (ManyToOneField) FieldsTypesFactory.buildfieldType(f, dtoClass,
						getAlias(dtoClass));

				JoinedElement joinedElement = join(dtoClass, manyToOneColumn.type(), targetAlias,
						manyToOneColumn.name(), targetId.getColumnName(), manyToOneField);
				joins += " " + joinedElement.joinPart;

				// select field
				tableFields += joinedElement.fieldsPart;
			} else if (f.getAnnotation(OneToMany.class) != null) {
				// join
				OneToMany oneToManyColumn = f.getAnnotation(OneToMany.class);
				JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);
				String targetAlias = getAlias(oneToManyColumn.type());
				PrimitiveField idColumn = FieldsTypesFactory.getIdAttribute(dtoClass, targetAlias);
				OneToManyField oneToManyField = (OneToManyField) FieldsTypesFactory.buildfieldType(f, dtoClass,
						getAlias(dtoClass));

				JoinedElement joinedElement = join(dtoClass, oneToManyColumn.type(), targetAlias,
						idColumn.getColumnName(), joinColumn.name(), oneToManyField);
				joins += " " + joinedElement.joinPart;

				// select field
				tableFields += joinedElement.fieldsPart;
			} else if (f.getAnnotation(OneToOne.class) != null) {
				// join
				OneToOne OneToOneColumn = f.getAnnotation(OneToOne.class);
				String targetAlias = getAlias(OneToOneColumn.type());
				PrimitiveField targetId = FieldsTypesFactory.getIdAttribute(OneToOneColumn.type(), targetAlias);
				OneToOneField oneToOneField = (OneToOneField) FieldsTypesFactory.buildfieldType(f, dtoClass,
						getAlias(dtoClass));

				JoinedElement joinedElement = join(dtoClass, OneToOneColumn.type(), targetAlias, OneToOneColumn.name(),
						targetId.getColumnName(), oneToOneField);
				joins += " " + joinedElement;

				// select field
				tableFields += joinedElement.fieldsPart;
			}

		}
		tableFields = tableFields.substring(0, tableFields.length() - 1);
		String where = "";
		if (!xql.trim().equals("") && xql != null) {
			where = "WHERE " + xql;
		}

		String sql = "SELECT " + tableFields + " FROM " + tableName + " AS " + getAlias(dtoClass) + " " + joins + " "
				+ where + ";";
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
			// System.out.println(((JDBC4PreparedStatement) pstm).asSql());

			Constructor<T> ctor = dtoClass.getConstructor();
			while (rs.next()) {
				T mainEntity = (T) ctor.newInstance();
				List<ObjectMappedRow<?>> entities = new ArrayList<>();
				for (AbstractFieldsMapping<?, ?> mapping : mappedFields) {
					if (mapping.getClassType().getSimpleName().equals(dtoClass.getSimpleName())) {
						for (AbstractField myField : mapping.getPrimitiveFields()) {
							if (myField.getSetter() != null) {
								myField.getSetter().invoke(mainEntity, myField.getParamForSetter(rs, con));
							}
						}
						continue;
					}

					Constructor<?> joinCtor = mapping.getClassType().getConstructor();
					Object joinedEntity = joinCtor.newInstance();
					for (AbstractField myField : mapping.getPrimitiveFields()) {
						if (myField.getSetter() != null) {
							myField.getSetter().invoke(joinedEntity, myField.getParamForSetter(rs, con));
						}
					}
					entities.add(new ObjectMappedRow<Object>(joinedEntity, mapping));
				}

				for (ObjectMappedRow entity : entities) {
					if (entity.getAbstractField().getContainerClass() != null) {
						for (ObjectMappedRow containerEntity : entities) {
							if (entity.getAbstractField().getContainerClass().isInstance(containerEntity.getEntity())) {
								containerEntity.getAbstractField().getContainerClassField().getSetter()
										.invoke(containerEntity, entity);
							}
							break;
						}
					}
				}

				result.add(mainEntity);
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

	private static <T> String getAlias(Class<T> dtoClass) {
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		String alias = table.alias();
		if (alias.trim().equals("")) {
			alias = tableName;
		}

		return alias;
	}

	protected static <T, R> JoinedElement join(Class<T> dtoClass, Class<R> dtoTargetClass, String targetTableAlias,
			String originId, String targetId, AbstractField containerClassField) {
		String alias = getAlias(dtoClass);
		addAbstractFields(dtoTargetClass, dtoClass, alias, containerClassField);

		Table targetTable = dtoTargetClass.getAnnotation(Table.class);
		String targetTableName = targetTable.name();

		String join = "JOIN " + targetTableName + " AS " + targetTableAlias + " ON " + alias + "." + originId + " = "
				+ targetTableAlias + "." + targetId + " ";

		String tableFields = "";
		for (Field f : dtoTargetClass.getDeclaredFields()) {
			if (f.getAnnotation(Column.class) != null) {
				Column column = f.getAnnotation(Column.class);
				tableFields += getAlias(dtoTargetClass) + "." + column.name() + " AS '" + getAlias(dtoTargetClass) + "."
						+ column.name() + "' ,";
			} else if (f.getAnnotation(ManyToOne.class) != null) {
				ManyToOne manyToOneColumn = f.getAnnotation(ManyToOne.class);
				PrimitiveField subJoinTargetId = FieldsTypesFactory.getIdAttribute(manyToOneColumn.type(), alias);
				String targetSubJoinAlias = getAlias(manyToOneColumn.type());
				ManyToOneField manyToOneField = (ManyToOneField) FieldsTypesFactory.buildfieldType(f, dtoClass,
						getAlias(dtoClass));
				
				JoinedElement joinedElement = join(dtoTargetClass, manyToOneColumn.type(), targetSubJoinAlias,
						manyToOneColumn.name(), subJoinTargetId.getColumnName(), manyToOneField);
				join += " " + joinedElement.joinPart;
				tableFields += joinedElement.fieldsPart;
			}
		}

		return new JoinedElement(join, tableFields);
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
		addAbstractFields(dtoClass, null, null, null);
	}

	private static <T, C> void addAbstractFields(Class<T> dtoClass, Class<C> containerClass, String containerAlias,
			AbstractField containerClassField) {
		if (mappedFields.contains(dtoClass)) {
			return;
		}
		String alias = getAlias(dtoClass);
		List<PrimitiveField> primitiveFields = new ArrayList<PrimitiveField>();
		for (Field f : dtoClass.getDeclaredFields()) {
			AbstractField field = FieldsTypesFactory.buildfieldType(f, dtoClass, alias);
			if (field != null && field instanceof PrimitiveField) {
				primitiveFields.add((PrimitiveField) field);
			}
		}
		mappedFields.add(new AbstractFieldsMapping<T, C>(dtoClass, containerClass, primitiveFields, containerClassField,
				containerAlias));
	}
}
