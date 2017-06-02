package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import tp.utn.ann.Column;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.Table;
import tp.utn.methods.Find;

public abstract class AbstractField {

	protected Field attribute;
	protected String columnName;
	protected Method setter;
	protected Method getter;
	protected String tableName;
	protected String tableAlias;

	public <T> AbstractField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		this.attribute = attribute;
		this.columnName = columnName;
		this.tableName = dtoClass.getAnnotation(Table.class).name();
		this.tableAlias = tableAlias;

		Method methods[] = dtoClass.getDeclaredMethods();

		// obtengo el metodo setter de este atributo
		String setterName = "set" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1);
		for (Method method : methods) {
			if (method.getName().equals(setterName)) {
				this.setter = method;
				break;
			}
		}

		// obtengo el metodo getter de este atributo
		String getterName = "get" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1);
		for (Method method : methods) {
			if (method.getName().equals(getterName)) {
				this.getter = method;
				break;
			}
		}
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public Field getAttribute() {
		return attribute;
	}

	public void setAttribute(Field attribute) {
		this.attribute = attribute;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}


	public abstract Object getParamForSetter(ResultSet rs, Connection con) throws SQLException;

}
