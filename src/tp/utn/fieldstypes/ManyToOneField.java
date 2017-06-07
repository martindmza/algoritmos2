package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import tp.utn.ann.ManyToOne;
import tp.utn.methods.Find;

public class ManyToOneField extends AbstractField {

	public <T> ManyToOneField(Field attribute, ManyToOne manyToOneColumn, Class<T> dtoClass, String tableAlias) {
		super(attribute, manyToOneColumn.name(), dtoClass, tableAlias);
	}
	
	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		return null;
		// ManyToOne manyToOne = this.attribute.getAnnotation(ManyToOne.class);
		// Integer idColumn = rs.getInt(this.columnName);
		//
		// Object relationObject = Find.find(con, manyToOne.type(), idColumn);
		// return relationObject;
	}

}
