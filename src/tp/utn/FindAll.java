package tp.utn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import tp.utn.ann.Column;
import tp.utn.ann.Table;
import tp.utn.demo.domain.Persona;
import java.lang.reflect.Method;

public class FindAll {
	

	// Retorna: una todasa las filas de la tabla representada por dtoClass
	// Invoca a: query
	public static <T> List<T> findAll(Connection con, Class<T> dtoClass)
	{
		//obtengo el nombre de la tabla de la anotation @Table
		Table table = dtoClass.getAnnotation(Table.class);
		String tableName = table.name();
		
		//obtengo los atributos que son columnas de la tabla usando la anotation @Column
		String tableFields = "";
		for (Field f: dtoClass.getDeclaredFields()) {
			   Column column = f.getAnnotation(Column.class);
			   if (column != null) {
				   tableFields += column.name() + "," ;
			   }
		}
		tableFields = tableFields.substring(0, tableFields.length()-1);
		
		//obtengo los metodos setters de la clase para usarlos por cada columna de las filas obtenidas de la base de datos
		Method methods[] = dtoClass.getDeclaredMethods();
		List<Method> setters = new ArrayList<Method>();
		for(Method method: methods) {
			if (method.getName().startsWith("set")) {
				setters.add(method);
			}
		}
		
		//armo el string sql
		String sql = "SELECT " + tableFields + " FROM " + tableName + ";";
		
		
		//ejecuto el comando sql
		List<Persona> result = new ArrayList<Persona>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			
			//construyo un objeto de la clase Persona por cada fila encontrada
			Constructor ctor = dtoClass.getConstructor();
			while( rs.next() ) {
				for (Method setter : setters) {
					//TODO invocar los setters dinamicamente
				}
				Object obj = ctor.newInstance();
				Persona p = (Persona) obj;
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				result.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return (List<T>) result;
	}
}
