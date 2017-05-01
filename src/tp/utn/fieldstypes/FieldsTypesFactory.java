package tp.utn.fieldstypes;

import java.lang.reflect.Field;
import tp.utn.ann.Id;
import tp.utn.ann.Column;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.OneToMany;

public class FieldsTypesFactory {

	public static <T> AbstractField buildfieldType(Field f, Class<T> dtoClass) {
		if (f.getAnnotation(Column.class) != null) {
			Column column = f.getAnnotation(Column.class);
			return new PrimitiveField(f, column.name(), dtoClass);
		} else if (f.getAnnotation(ManyToOne.class) != null) {
			ManyToOne manyToOneAttr = f.getAnnotation(ManyToOne.class);
			return new ManyToOneField(f, manyToOneAttr.name(), dtoClass);
		} else if (f.getAnnotation(OneToMany.class) != null) {
			OneToMany oneToManyAttr = f.getAnnotation(OneToMany.class);
			PrimitiveField id = getIdAttribute(dtoClass);
			PrimitiveField joinedId = getIdAttribute(oneToManyAttr.type());
			return new OneToManyField(f, dtoClass, oneToManyAttr.type(), id, joinedId);
		}
		return null;
	}

	public static <T> PrimitiveField getIdAttribute(Class<T> dtoClass) {
		for (Field f : dtoClass.getDeclaredFields()) {
			Id idAttribute = f.getAnnotation(Id.class);
			if (idAttribute != null) {
				Column column = f.getAnnotation(Column.class);
				return new PrimitiveField(f, column.name(), dtoClass);
			}
		}
		return null;
	}
}
