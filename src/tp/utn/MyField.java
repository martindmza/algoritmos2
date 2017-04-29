package tp.utn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyField {

	public Field attribute;
	public String columnName;
	public Method setter;
	public Method getter;
	
	public <T> MyField (Field attribute, String columnName, Class<T> dtoClass) {
		
		this.attribute = attribute;
		this.columnName = columnName;
		
		Method methods[] = dtoClass.getDeclaredMethods();
		
		//obtengo el metodo setter de este atributo
		String setterName = "set" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(0, 1).substring(1);
		for(Method method: methods) {
			if (method.getName().equals(setterName)) {
				this.setter = method;
			}
		}

		//obtengo el metodo getter de este atributo
		String getterName = "get"  + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(0, 1).substring(1);
		for(Method method: methods) {
			if (method.getName().equals(getterName)) {
				this.getter = method;
			}
		}
	}
	
}
