package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tp.utn.ann.OneToMany;
import tp.utn.ann.Table;

public class OneToManyField extends AbstractField {
	
	protected String relatedTableName;
	protected PrimitiveField id;
	protected PrimitiveField joinedId;

	public <T> OneToManyField(Field attribute, Class<T> dtoClass, Class<?> relatedClass, PrimitiveField id, PrimitiveField joinedId) {
		super(attribute, "", dtoClass);
		this.relatedTableName = relatedClass.getAnnotation(Table.class).name();
		this.id = id;
		this.joinedId = joinedId;
	}


	@Override
	public Object getParamForSetter(ResultSet rs, Connection con) throws SQLException {
		OneToMany oneToMany = this.attribute.getAnnotation(OneToMany.class);
		
		String middleTableName = this.tableName + "_" + relatedTableName;
		String thisIdField =  this.tableName + "_id";
		String joinedIdField = this.relatedTableName + "_id";
		String sql = "SELECT * FROM " + middleTableName + " WHERE " + thisIdField + " = " ;
		
		PreparedStatement pstm = null;
		ResultSet rs2 = null;
		try {
			pstm = con.prepareStatement(sql);
			rs2 = pstm.executeQuery();
			while (rs2.next()) {
				int thisId = rs2.getInt(this.columnName);
				int joinedId = rs2.getInt(this.columnName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return null;
	}
}
