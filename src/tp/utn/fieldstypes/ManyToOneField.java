package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import tp.utn.Find;
import tp.utn.ann.ManyToOne;

public class ManyToOneField extends AbstractField {

	public <T> ManyToOneField(Field attribute, String columnName, Class<T> dtoClass) {
		super(attribute, columnName, dtoClass);
	}

	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		ManyToOne manyToOne = this.attribute.getAnnotation(ManyToOne.class);
		Integer idColumn = rs.getInt(this.columnName);

		Object relationObject = Find.find(con, manyToOne.type(), idColumn);
		return relationObject;
	}

}
