package tp.utn.methods;

import java.sql.Connection;
import java.util.List;

import tp.utn.exceptions.EntityNotFoundException;
import tp.utn.fieldstypes.FieldsTypesFactory;
import tp.utn.fieldstypes.PrimitiveField;

public class Find extends Utn {

	// Retorna: una fila identificada por id o null si no existe
	// Invoca a: query
	public static <T> T find(Connection con, Class<T> dtoClass, Object id) throws EntityNotFoundException {
		PrimitiveField idAttribute = FieldsTypesFactory.getIdAttribute(dtoClass, getAlias(dtoClass));
		String idAttrName = idAttribute.getAttribute().getName();

		List<T> result = query(con, dtoClass, "$" + idAttrName + "= ? ", id);

		if (result.size() == 0) {
			throw new EntityNotFoundException();
		}
		return result.get(0);
	}

}
