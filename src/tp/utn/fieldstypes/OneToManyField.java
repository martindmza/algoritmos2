package tp.utn.fieldstypes;

import java.lang.reflect.Field;

public class OneToManyField extends AbstractField {
	
	protected Class<?> relatedClass;
	protected String joinColumn;
	protected PrimitiveField idField;

	public <T> OneToManyField(Field attribute, Class<T> dtoClass, String tableAlias, Class<?> relatedClass, PrimitiveField idField, String joinColumn) {
		super(attribute, "", dtoClass, tableAlias);
		this.relatedClass = relatedClass;
		this.idField = idField;
		this.joinColumn = joinColumn;
	}
}
