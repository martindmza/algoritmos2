package tp.utn.methods;

import java.sql.Connection;
import java.util.List;

public class Find extends Utn {

	// Retorna: una fila identificada por id o null si no existe
	// Invoca a: query
	public static <T> T find(Connection con, Class<T> dtoClass, Object id) {
		List<T> result = query(con,dtoClass, "$id = ? ", id);
		
		if (result.size() == 0) {
			return null;
		} 
		return result.get(0);
	}

}
