package tp.utn.fieldstypes;

import java.lang.reflect.Field;

public class OneToManyField extends AbstractField {
	
	protected Class<?> relatedClass;
	protected PrimitiveField idField;

	public <T> OneToManyField(Field attribute, Class<T> dtoClass, String tableAlias, Class<?> relatedClass, PrimitiveField idField) {
		super(attribute, "", dtoClass, tableAlias);
		this.relatedClass = relatedClass;
		this.idField = idField;
	}
}
