package tp.utn.fieldstypes;

import java.lang.reflect.Field;

public class OneToOneField extends AbstractField {

	public <T> OneToOneField(Field attribute, String columnName, Class<T> dtoClass, String tableAlias) {
		super(attribute, columnName, dtoClass, tableAlias);
	}
}
