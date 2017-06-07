package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import tp.utn.ann.Id;
import tp.utn.ann.JoinColumn;
import tp.utn.ann.Column;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.OneToMany;

public class FieldsTypesFactory {

	public static <T> AbstractField buildfieldType(Field f, Class<T> dtoClass, String tableAlias) {
		if (f.getAnnotation(Column.class) != null) {
			Column column = f.getAnnotation(Column.class);
			return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
		} else if (f.getAnnotation(ManyToOne.class) != null) {
			ManyToOne manyToOneAttr = f.getAnnotation(ManyToOne.class);
			return new ManyToOneField(f, manyToOneAttr, dtoClass, tableAlias);
		} else if (f.getAnnotation(OneToMany.class) != null) {
			OneToMany oneToManyAttr = f.getAnnotation(OneToMany.class);
			JoinColumn joinColumn = f.getAnnotation(JoinColumn.class);
			String joinColumnName = joinColumn.name(); 
			return new OneToManyField(f, dtoClass, tableAlias, oneToManyAttr.type(), getIdAttribute(dtoClass, tableAlias), joinColumnName);
		}
		return null;
	}

	public static <T> PrimitiveField getIdAttribute(Class<T> dtoClass, String tableAlias) {
		for (Field f : dtoClass.getDeclaredFields()) {
			Id idAttribute = f.getAnnotation(Id.class);
			if (idAttribute != null) {
				Column column = f.getAnnotation(Column.class);
				return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
			}
		}
		return null;
	}

	public static <T> PrimitiveField buildPrimitiveField(Field f, Class<T> dtoClass, String tableAlias) {
		if (f.getAnnotation(Column.class) != null) {
			Column column = f.getAnnotation(Column.class);
			return new PrimitiveField(f, column.name(), dtoClass, tableAlias);
		}
		return null;
	}
}
