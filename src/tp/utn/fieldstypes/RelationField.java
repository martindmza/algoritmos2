package tp.utn.fieldstypes;

import java.lang.reflect.Field;

public class RelationField extends AbstractField {

	public <T> RelationField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		super(attribute, columnName, dtoClass, tableAlias);
	}

}
