package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimitiveField extends AbstractField {

	public <T> PrimitiveField(Field attribute, String columnName, Class<T> dtoClass) {
		super(attribute, columnName, dtoClass);
	}

	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		String type = this.attribute.getType().getSimpleName();			
		if (type.equals("Integer") || type.equals("int")) {
			return rs.getInt(this.columnName);
		} else if (type.equals("String")) {
			return rs.getString(this.columnName);
		} else if (type.equals("Boolean") || type.equals("boolean")) {
			return rs.getBoolean(this.columnName);
		}
		return null;
	}

}
