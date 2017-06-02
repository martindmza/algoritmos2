package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimitiveField extends AbstractField {

	public <T> PrimitiveField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		super(attribute, columnName, dtoClass, tableAlias);
	}

	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		String type = this.attribute.getType().getSimpleName();		
		if (type.equals("Integer") || type.equals("int")) {
			return rs.getInt(this.getColumnAliasName());
		} else if (type.equals("String")) {
			return rs.getString(this.getColumnAliasName());
		} else if (type.equals("Boolean") || type.equals("boolean")) {
			return rs.getBoolean(this.getColumnAliasName());
		}
		return null;
	}

	private String getColumnAliasName() {
		return this.tableAlias + "." + this.columnName;
	}

}
