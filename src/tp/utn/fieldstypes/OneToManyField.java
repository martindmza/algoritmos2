package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;
import tp.utn.methods.Utn;

public class OneToManyField extends AbstractField {
	
	protected Class<?> relatedClass;
	protected String joinColumn;
	protected PrimitiveField idField;

	public <T> OneToManyField(Field attribute, Class<T> dtoClass, Class<?> relatedClass, PrimitiveField idField, String joinColumn) {
		super(attribute, "", dtoClass);
		this.relatedClass = relatedClass;
		this.idField = idField;
		this.joinColumn = joinColumn;
	}


	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		OneToMany oneToMany = this.attribute.getAnnotation(OneToMany.class);
		
		String xql = joinColumn + " = ?";
		int thisObjectId = rs.getInt(this.idField.columnName);
		
		List<Object> resultSet = new ArrayList<Object>();
		for (Object result : Utn.query(con, relatedClass, xql, thisObjectId)) {
			resultSet.add(result);
		}
		
		return resultSet;
	}
}
