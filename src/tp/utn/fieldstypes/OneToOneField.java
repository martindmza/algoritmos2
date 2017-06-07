package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OneToOneField extends AbstractField {

	public <T> OneToOneField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		super(attribute, columnName, dtoClass, tableAlias);
	}

	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
