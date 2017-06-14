package tp.utn.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	String NONE = "";
	int LAZY=1;
	int EAGER=2;
	
	int fetchType() default EAGER; 
	Class<?> type();
	String att() default NONE;
}

